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

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.JobStatus;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.description.ProcessDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessDescription;
import org.n52.javaps.algorithm.IAlgorithm;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.algorithm.RepositoryManager;

import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class EngineImpl implements Engine, Destroyable {
    private final Logger LOG = LoggerFactory.getLogger(EngineImpl.class);
    private final ExecutorService executor;
    private final RepositoryManager repositoryManager;
    private final Map<JobId, Job> jobs = new ConcurrentHashMap<>(16);
    private final Map<JobId, Cancelable> cancelers = new ConcurrentHashMap<>(16);
    private final ProcessInputDecoder processInputDecoder;
    private final ProcessOutputEncoder processOutputEncoder;
    private final JobIdGenerator jobIdGenerator;

    @Inject
    public EngineImpl(RepositoryManager repositoryManager,
                      ProcessInputDecoder processInputDecoder,
                      ProcessOutputEncoder processOutputEncoder,
                      JobIdGenerator jobIdGenerator) {
        this.executor = createExecutor();
        this.repositoryManager = Objects.requireNonNull(repositoryManager);
        this.processInputDecoder = Objects.requireNonNull(processInputDecoder);
        this.processOutputEncoder = Objects.requireNonNull(processOutputEncoder);
        this.jobIdGenerator = Objects.requireNonNull(jobIdGenerator);
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

        ProcessInputs processInputs = this.processInputDecoder.decode(algorithm.getDescription(), inputs);

        // TODO encode
        JobId jobId = jobIdGenerator.create(algorithm, processInputs, outputDefinitions);

        Job job = new Job(algorithm, jobId, processInputs, outputDefinitions);
        LOG.info("Submitting {}", job.getJobId());
        Future<?> submit = this.executor.submit(job);

        this.cancelers.put(jobId, () -> submit.cancel(true));

        this.jobs.put(jobId, job);

        return jobId;
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
            this.outputDefinitions = Objects.requireNonNull(outputDefinitions, "outputDefinitions");

            this.outputs = new ProcessOutputs();
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
            LOG.info("Executing {}", this.jobId);
            try {
                this.algorithm.execute(this);
                LOG.info("Executed {}, creating result", this.jobId);
                try {
                    set(processOutputEncoder.create(this));
                    LOG.info("Created result for {}", this.jobId);
                    setStatus(JobStatus.succeeded());
                } catch (OutputEncodingException ex) {
                    LOG.error("Failed creating result for {}", this.jobId);
                    setStatus(JobStatus.failed());
                    setException(ex);
                }
            } catch (Throwable ex) {
                LOG.error("{} failed", this.jobId);
                setStatus(JobStatus.failed());
                setException(ex);
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
