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
package org.n52.javaps.ogc.wps;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

import org.n52.javaps.description.ProcessDescription;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessOffering {
    private static final String DEFAULT_PROCESS_MODEL = "native";

    private final Collection<JobControlOption> jobControlOptions = new LinkedHashSet<>(2);
    private final Collection<DataTransmissionMode> outputTransmissionModes = new LinkedHashSet<>(2);
    private Optional<String> processVersion;
    private Optional<String> processModel = Optional.of(DEFAULT_PROCESS_MODEL);

    private ProcessDescription processDescription;

    public Optional<String> getProcessVersion() {
        return this.processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = Optional.ofNullable(Strings.emptyToNull(processVersion));
    }

    public Optional<String> getProcessModel() {
        return processModel;
    }

    public void setProcessModel(String processModel) {
        this.processModel = Optional.ofNullable(Strings.emptyToNull(processModel));
    }

    public ProcessDescription getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(ProcessDescription processDescription) {
        this.processDescription = Objects.requireNonNull(processDescription);
    }

    public Collection<JobControlOption> getJobControlOptions() {
        return Collections.unmodifiableCollection(jobControlOptions);
    }

    public void addJobControlOptions(JobControlOption option) {
        this.jobControlOptions.add(Objects.requireNonNull(option));
    }

    public void addJobControlOptions(String option) {
        addJobControlOptions(new JobControlOption(option));
    }

    public Collection<DataTransmissionMode> getOutputTransmissionModes() {
        return Collections.unmodifiableCollection(outputTransmissionModes);
    }

    public void addOutputTransmissionMode(DataTransmissionMode mode) {
        this.outputTransmissionModes.add(Objects.requireNonNull(mode));
    }
}
