/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Destroyable;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.JobStatus;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.data.GroupProcessData;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.data.ReferenceProcessData;
import org.n52.iceland.ogc.wps.data.ValueProcessData;
import org.n52.iceland.ogc.wps.description.ProcessDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedGroupInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescriptionContainer;
import org.n52.javaps.algorithm.IAlgorithm;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.algorithm.RepositoryManager;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.GroupData;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandlerRepository;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class EngineImpl implements Engine, Destroyable {
    private final Logger LOG = LoggerFactory.getLogger(EngineImpl.class);
    private final ExecutorService executor;
    private final RepositoryManager repositoryManager;
    private final Map<JobId, Job> jobs = new ConcurrentHashMap<>(16);
    private final Map<JobId, Cancelable> cancelers = new ConcurrentHashMap<>(16);
    private final InputHandlerRepository inputHandlerRepository;
    private final OutputHandlerRepository outputHandlerRepository;

    @Inject
    public EngineImpl(RepositoryManager repositoryManager,
                      InputHandlerRepository inputHandlerRepository, OutputHandlerRepository outputHandlerRepository) {
        this.executor = createExecutor();
        this.repositoryManager = Objects.requireNonNull(repositoryManager);
        this.inputHandlerRepository = Objects.requireNonNull(inputHandlerRepository);
        this.outputHandlerRepository = Objects.requireNonNull(outputHandlerRepository);
    }

    @Override
    public Set<JobId> getJobIdentifiers() {
        return Collections.emptySet();
    }

    @Override
    public Set<OwsCode> getProcessIdentifiers() {
        return this.repositoryManager.getAlgorithms();
    }

    @Override
    public Optional<ProcessDescription> getProcessDescription(OwsCode identifier) {
        return this.repositoryManager.getProcessDescription(identifier);
    }

    @Override
    public StatusInfo dismiss(JobId identifier) throws JobNotFoundException {
        LOG.info("Canceling {}", identifier);
        Job job = getJob(identifier);
        this.cancelers.get(identifier).cancel();
        return job.getStatus();
    }

    @Override
    public StatusInfo getStatus(JobId identifier) throws JobNotFoundException {
        LOG.info("Getting status {}", identifier);
        return getJob(identifier).getStatus();
    }

    @Override
    public JobId execute(OwsCode identifier, List<ProcessData> inputs, List<OutputDefinition> outputDefinitions)
            throws ProcessNotFoundException, InputDecodingException {
        LOG.info("Executing {}", identifier);
        IAlgorithm algorithm = getProcess(identifier);

        ProcessInputs processInputs = decodeContainer(algorithm.getDescription(), inputs);

        // TODO encode
        Job job = new Job(algorithm, createJobId(), processInputs, outputDefinitions);
        LOG.info("Submitting {}", job.getJobId());
        Future<?> submit = this.executor.submit(job);

        this.cancelers.put(job.getJobId(), () -> submit.cancel(true));

        this.jobs.put(job.getJobId(), job);

        return job.getJobId();
    }

    @Override
    public Future<Result> getResult(JobId identifier) throws JobNotFoundException {
        LOG.info("Getting result {}", identifier);
        return getJob(identifier);
    }

    private ExecutorService createExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("javaps-%d").build();
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool(threadFactory);
        return newCachedThreadPool;
    }

    private JobId createJobId() {
        return new JobId(UUID.randomUUID().toString());
    }

    @Override
    public void destroy() {
        this.executor.shutdownNow();
    }

    private Job getJob(JobId identifier) throws JobNotFoundException {
        return Optional.ofNullable(jobs.get(identifier))
                .orElseThrow(jobNotFound(identifier));
    }

    private IAlgorithm getProcess(OwsCode identifier) throws ProcessNotFoundException {
        return this.repositoryManager.getAlgorithm(identifier)
                .orElseThrow(processNotFound(identifier));
    }

    private Result createResult(TypedProcessDescription description, List<OutputDefinition> outputDefinitions, ProcessOutputs outputs) {
        /* TODO implement org.n52.javaps.EngineImpl.Job.createResult() */
        throw new UnsupportedOperationException("org.n52.javaps.EngineImpl.Job.createResult() not yet implemented");
    }

    private ProcessInputs decodeContainer(TypedProcessInputDescriptionContainer description,
                                          List<ProcessData> processInputs) throws InputDecodingException {

        Map<OwsCode, List<Data<?>>> data = new HashMap<>(processInputs.size());

        for (ProcessData input : processInputs) {

            Data<?> decodedInput = decode(description.getInput(input.getId()), input);

            data.computeIfAbsent(input.getId(), id -> new LinkedList<>()).add(decodedInput);
        }

        return new ProcessInputs(data);
    }

    private Data<?> decode(TypedProcessInputDescription<?> description, ProcessData input) throws InputDecodingException {
        if (input.isGroup()) {
            return decodeGroup(description.asGroup(), input.asGroup());
        } else if (input.isReference()) {
            return decodeRefernce(description, input.asReference());
        } else if (input.isValue()) {
            return decodeValueInput(description, input.asValue());
        } else {
            throw new AssertionError("Unsupported input type: " + input);
        }
    }

    private Data<?> decodeGroup(TypedGroupInputDescription description, GroupProcessData input)
            throws InputDecodingException {
        return new GroupData(decodeContainer(description, input.getElements()));
    }

    private Data<?> decodeRefernce(TypedProcessInputDescription<?> description, ReferenceProcessData input)
            throws InputDecodingException {
        ValueProcessData resolve;
        try {
            resolve = input.resolve();
        } catch (IOException ex) {
            throw new InputDecodingException(input.getId(), ex);
        }
        return decode(description, resolve);
    }

    private Data<?> decodeValueInput(TypedProcessInputDescription<?> description, ValueProcessData input)
            throws InputDecodingException {
        Format format = input.getFormat();
        Class<? extends Data<?>> bindingType = description.getBindingType();

        InputHandler handler = this.inputHandlerRepository
                .getInputHandler(format, bindingType)
                .orElseThrow(noHandlerFound(input.getId()));

        try (InputStream data = input.getData()) {
            return handler.parse(description, data, format);
        } catch (IOException | DecodingException ex) {
            throw new InputDecodingException(input.getId(), ex);
        }
    }

    private static Supplier<JobNotFoundException> jobNotFound(JobId id) {
        return () -> new JobNotFoundException(id);
    }

    private static Supplier<ProcessNotFoundException> processNotFound(OwsCode id) {
        return () -> new ProcessNotFoundException(id);
    }

    private static Supplier<InputDecodingException> noHandlerFound(OwsCode id) {
        return () -> new InputDecodingException(id, new UnsupportedInputFormatException());
    }


    @FunctionalInterface
    private interface Cancelable {
        void cancel();
    }

    private final class Job extends AbstractFuture<Result>
            implements Runnable, ProcessExecutionContext, Future<Result> {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        private final JobId jobId;
        private final ProcessInputs inputs;
        private final ProcessOutputs outputs;
        private final TypedProcessDescription description;
        private final IAlgorithm algorithm;
        private final List<OutputDefinition> outputDefinitions;

        private Short percentCompleted;
        private OffsetDateTime estimatedCompletion;
        private OffsetDateTime nextPoll;
        private JobStatus status;

        Job(IAlgorithm algorithm, JobId jobId, ProcessInputs inputs, List<OutputDefinition> outputDefinitions) {
            this.status = JobStatus.accepted();
            this.jobId = Objects.requireNonNull(jobId, "jobId");
            this.inputs = Objects.requireNonNull(inputs, "inputs");
            this.algorithm = Objects.requireNonNull(algorithm, "algorithm");
            this.description = algorithm.getDescription();
            this.outputs = new ProcessOutputs();
            this.outputDefinitions = Objects.requireNonNull(outputDefinitions, "outputDefinitions");
        }

        @Override
        public OwsCode getProcessId() {
            return getDescription().getId();
        }

        @Override
        public JobId getJobId() {
            return this.jobId;
        }

        @Override
        public List<OutputDefinition> getOutputDefinitions() {
            return Collections.unmodifiableList(this.outputDefinitions);
        }

        @Override
        public ProcessOutputs getOutputs() {
            return this.outputs;
        }

        @Override
        public ProcessInputs getInputs() {
            return this.inputs;
        }

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
                statusInfo.setStatus(status);

                if (status.equals(JobStatus.running())) {
                    statusInfo.setEstimatedCompletion(estimatedCompletion);
                    statusInfo.setPercentCompleted(percentCompleted);
                    statusInfo.setNextPoll(nextPoll);
                } else if (status.equals(JobStatus.succeeded()) || status.equals(JobStatus.failed())) {
                    // TODO statusInfo.setExpirationDate(expirationDate);
                }

            } finally {
                this.lock.readLock().unlock();
            }
            return statusInfo;
        }

        private void setStatus(JobStatus s) {
            this.lock.writeLock().lock();
            try {
                this.status = s;
            } finally {
                this.lock.writeLock().unlock();
            }
        }

        @Override
        public void run() {
            setStatus(JobStatus.running());
            try {
                this.algorithm.execute(this);

                setStatus(JobStatus.succeeded());

                Result result = createResult(this.description, this.outputDefinitions, this.outputs);

                set(result);
            } catch (Throwable t) {
                setStatus(JobStatus.failed());
                setException(t);
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
    }
}
