/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.engine.impl;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.javaps.algorithm.IAlgorithm;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.algorithm.RepositoryManager;
import org.n52.javaps.description.TypedComplexOutputDescription;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.description.TypedProcessOutputDescriptionContainer;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.EngineProcessExecutionContext;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.JobIdGenerator;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputEncodingException;
import org.n52.javaps.engine.ProcessExecutionContext;
import org.n52.javaps.engine.ProcessInputDecoder;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.engine.ProcessOutputEncoder;
import org.n52.javaps.engine.ResultPersistence;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.JobStatus;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class EngineImpl implements Engine, Destroyable {
    private static final String EXECUTING = "Executing {}";

    private final Logger LOG = LoggerFactory.getLogger(EngineImpl.class);

    private final ExecutorService executor;

    private final RepositoryManager repositoryManager;

    private final Map<JobId, Job> jobs = new ConcurrentHashMap<>(16);

    private final Map<JobId, Cancelable> cancelers = new ConcurrentHashMap<>(16);

    private final ProcessInputDecoder processInputDecoder;

    private final ProcessOutputEncoder processOutputEncoder;

    private final JobIdGenerator jobIdGenerator;

    private final ResultPersistence resultPersistence;

    @Inject
    public EngineImpl(RepositoryManager repositoryManager, ProcessInputDecoder processInputDecoder,
            ProcessOutputEncoder processOutputEncoder, JobIdGenerator jobIdGenerator,
            ResultPersistence resultPersistence) {
        this.executor = createExecutor();
        this.repositoryManager = Objects.requireNonNull(repositoryManager);
        this.processInputDecoder = Objects.requireNonNull(processInputDecoder);
        this.processOutputEncoder = Objects.requireNonNull(processOutputEncoder);
        this.jobIdGenerator = Objects.requireNonNull(jobIdGenerator);
        this.resultPersistence = Objects.requireNonNull(resultPersistence);
    }

    @Override
    public Set<JobId> getJobIdentifiers() {
        return Stream.concat(this.jobs.keySet().stream(), this.resultPersistence.getJobIds().stream()).collect(toSet());
    }

    @Override
    public Set<JobId> getJobIdentifiers(OwsCode identifier) {
        Set<JobId> jobIds = resultPersistence.getJobIds(identifier);
        return jobIds;
    }

    @Override
    public Set<OwsCode> getProcessIdentifiers() {
        return this.repositoryManager.getAlgorithms();
    }

    @Override
    public Optional<ProcessDescription> getProcessDescription(OwsCode identifier) {
        return this.repositoryManager.getProcessDescription(identifier).map(x -> (ProcessDescription) x);
    }

    @Override
    public StatusInfo dismiss(JobId identifier) throws JobNotFoundException {
        LOG.info("Canceling {}", identifier);
        Job job = getJob(identifier);
        this.cancelers.get(identifier).cancel();
        return job.getStatus();
    }

    @Override
    public StatusInfo getStatus(JobId identifier) throws EngineException {
        LOG.info("Getting status {}", identifier);
        Job job = this.jobs.get(identifier);
        if (job != null) {
            return job.getStatus();
        } else {
            return this.resultPersistence.getStatus(identifier);
        }
    }

    @Override
    public JobId execute(OwsCode identifier,
            List<ProcessData> inputs,
            List<OutputDefinition> outputDefinitions,
            ResponseMode responseMode) throws ProcessNotFoundException, InputDecodingException {
        LOG.info(EXECUTING, identifier);
        IAlgorithm algorithm = getProcess(identifier);
        TypedProcessDescription description = algorithm.getDescription();

        List<OutputDefinition> outputDefinitionsOrDefault = outputDefinitions;

        if (outputDefinitionsOrDefault == null || outputDefinitionsOrDefault.isEmpty()) {
            outputDefinitionsOrDefault = createDefaultOutputDefinitions(description);
        } else {
            outputDefinitionsOrDefault.stream().filter(definition -> description.getOutput(definition.getId()).isGroup()
                    && definition.getOutputs().isEmpty()).forEach(definition -> definition.setOutputs(
                            createDefaultOutputDefinitions(description.getOutput(identifier).asGroup())));
        }

        JobId jobId = jobIdGenerator.create(algorithm);

        Job job = new Job(algorithm, jobId, inputs, OutputDefinition.getOutputsById(outputDefinitionsOrDefault),
                responseMode);
        LOG.info("Submitting {}", job.getJobId());
        Future<?> submit = this.executor.submit(job);

        this.cancelers.put(jobId, () -> submit.cancel(true));

        this.jobs.put(jobId, job);

        return jobId;
    }

    private Result onJobCompletion(Job job) throws EngineException {
        this.cancelers.remove(job.getJobId());
        this.resultPersistence.save(job);
        this.jobs.remove(job.getJobId());
        return this.resultPersistence.getResult(job.getJobId());

    }

    @Override
    public Future<Result> getResult(JobId identifier) throws JobNotFoundException {
        LOG.info("Getting result {}", identifier);
        Job job = this.jobs.get(identifier);
        if (job != null) {
            return job;
        } else {
            try {
                return Futures.immediateFuture(this.resultPersistence.getResult(identifier));
            } catch (JobNotFoundException ex) {
                throw ex;
            } catch (EngineException ex) {
                return Futures.immediateFailedFuture(ex);
            }
        }
    }

    private ExecutorService createExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("javaps-%d").build();
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool(threadFactory);
        return newCachedThreadPool;
    }

    @Override
    public void destroy() {
        this.executor.shutdownNow();
    }

    private Job getJob(JobId identifier) throws JobNotFoundException {
        return Optional.ofNullable(jobs.get(identifier)).orElseThrow(jobNotFound(identifier));
    }

    private IAlgorithm getProcess(OwsCode identifier) throws ProcessNotFoundException {
        return this.repositoryManager.getAlgorithm(identifier).orElseThrow(processNotFound(identifier));
    }

    private List<OutputDefinition> createDefaultOutputDefinitions(TypedProcessOutputDescriptionContainer description) {
        return description.getOutputDescriptions().stream().map((TypedProcessOutputDescription<?> x) -> {
            if (!x.isGroup()) {
                return createDefaultOutputDefinition(x);
            } else {
                OutputDefinition outputDefinition = new OutputDefinition(x.getId());
                outputDefinition.setOutputs(createDefaultOutputDefinitions(x.asGroup()));
                return outputDefinition;
            }
        }).collect(toList());
    }

    private OutputDefinition createDefaultOutputDefinition(TypedProcessOutputDescription<?> processOutputDescription) {

        OutputDefinition outputDefinition = new OutputDefinition(processOutputDescription.getId());

        if (processOutputDescription.isComplex()) {
            TypedComplexOutputDescription complexOutputDefinition = processOutputDescription.asComplex();

            Format defaultFormat = complexOutputDefinition.getDefaultFormat();

            outputDefinition.setFormat(defaultFormat);
        }

        return outputDefinition;

    }

    private static Supplier<JobNotFoundException> jobNotFound(JobId id) {
        return () -> new JobNotFoundException(id);
    }

    private static Supplier<ProcessNotFoundException> processNotFound(OwsCode id) {
        return () -> new ProcessNotFoundException(id);
    }

    @FunctionalInterface
    private interface Cancelable {
        void cancel();
    }

    private final class Job extends AbstractFuture<Result> implements Runnable, ProcessExecutionContext,
            EngineProcessExecutionContext, Future<Result> {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private final JobId jobId;

        private final ProcessOutputs outputs;

        private final TypedProcessDescription description;

        private final IAlgorithm algorithm;

        private final Map<OwsCode, OutputDefinition> outputDefinitions;

        private final SettableFuture<List<ProcessData>> nonPersistedResult = SettableFuture.create();

        private Short percentCompleted;

        private OffsetDateTime estimatedCompletion;

        private OffsetDateTime nextPoll;

        private JobStatus jobStatus;

        private final ResponseMode responseMode;

        private final List<ProcessData> inputData;

        private ProcessInputs inputs;

        Job(IAlgorithm algorithm, JobId jobId, List<ProcessData> inputData,
                Map<OwsCode, OutputDefinition> outputDefinitions, ResponseMode responseMode) {

            this.jobStatus = JobStatus.accepted();
            this.jobId = Objects.requireNonNull(jobId, "jobId");
            this.algorithm = Objects.requireNonNull(algorithm, "algorithm");
            this.inputData = inputData;
            this.description = algorithm.getDescription();
            this.outputDefinitions = Objects.requireNonNull(outputDefinitions, "outputDefinitions");
            this.responseMode = Objects.requireNonNull(responseMode, "responseMode");
            this.outputs = new ProcessOutputs();
        }

        @Override
        public JobId getJobId() {
            return this.jobId;
        }

        @Override
        public Map<OwsCode, OutputDefinition> getOutputDefinitions() {
            return Collections.unmodifiableMap(this.outputDefinitions);
        }

        @Override
        public ProcessOutputs getOutputs() {
            return this.outputs;
        }

        @Override
        public ProcessInputs getInputs() {
            return this.inputs;
        }

        @Override
        public TypedProcessDescription getDescription() {
            return this.description;
        }

        public IAlgorithm getAlgorithm() {
            return this.algorithm;
        }

        public StatusInfo getStatus() {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setJobId(jobId);

            this.lock.readLock().lock();
            try {
                statusInfo.setStatus(jobStatus);

                if (jobStatus.equals(JobStatus.accepted()) || jobStatus.equals(JobStatus.running())) {
                    statusInfo.setEstimatedCompletion(estimatedCompletion);
                    statusInfo.setPercentCompleted(percentCompleted);
                    statusInfo.setNextPoll(nextPoll);
                } else if (jobStatus.equals(JobStatus.succeeded()) || jobStatus.equals(JobStatus.failed())) {
                    //TODO use value from configuration
                    OffsetDateTime expirationDate = OffsetDateTime.now().plusDays(30);
                    statusInfo.setExpirationDate(expirationDate);
                }

            } finally {
                this.lock.readLock().unlock();
            }
            return statusInfo;
        }

        private void setJobStatus(JobStatus s) {
            this.lock.writeLock().lock();
            try {
                this.jobStatus = s;
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        @Override
        public JobStatus getJobStatus() {
            return this.jobStatus;
        }

        @Override
        public void run() {
            setJobStatus(JobStatus.running());
            LOG.info(EXECUTING, this.jobId);
            try {
                this.inputs = processInputDecoder.decode(description, inputData);
                this.algorithm.execute(this);
                LOG.info("Executed {}, creating result", this.jobId);
                try {
                    this.nonPersistedResult.set(processOutputEncoder.create(this));
                    LOG.info("Created result for {}", this.jobId);
                    setJobStatus(JobStatus.succeeded());
                } catch (OutputEncodingException ex) {
                    LOG.error("Failed creating result for {}", this.jobId);
                    setJobStatus(JobStatus.failed());
                    this.nonPersistedResult.setException(ex);
                }
            } catch (Throwable ex) {
                LOG.error("{} failed", this.jobId);
                setJobStatus(JobStatus.failed());
                this.nonPersistedResult.setException(ex);
            } finally {
                try {
                    set(onJobCompletion(this));
                } catch (EngineException ex) {
                    setException(ex);
                }
            }
        }

        @Override
        public void setPercentCompleted(Short percentCompleted) {
            this.lock.writeLock().lock();
            try {
                this.percentCompleted = percentCompleted;
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        @Override
        public void setEstimatedCompletion(OffsetDateTime estimatedCompletion) {
            this.lock.writeLock().lock();
            try {
                this.estimatedCompletion = estimatedCompletion;
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        @Override
        public void setNextPoll(OffsetDateTime nextPoll) {
            this.lock.writeLock().lock();
            try {
                this.nextPoll = nextPoll;
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        @Override
        public List<ProcessData> getEncodedOutputs() throws Throwable {
            try {
                return this.nonPersistedResult.get();
            } catch (ExecutionException ex) {
                throw ex.getCause();
            }
        }

        @Override
        public ResponseMode getResponseMode() {
            return this.responseMode;
        }
    }
}
