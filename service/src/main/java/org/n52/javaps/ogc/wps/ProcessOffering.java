package org.n52.javaps.ogc.wps;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;

import org.n52.javaps.algorithm.ProcessDescription;

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
