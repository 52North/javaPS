package org.n52.javaps;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.ogc.wps.OutputDefinition;
import org.n52.javaps.ogc.wps.Result;
import org.n52.javaps.ogc.wps.StatusInfo;
import org.n52.javaps.ogc.wps.data.Data;
import org.n52.javaps.ogc.wps.description.ProcessDescription;

public class DummyEngine implements Engine {

    @Override
    public Set<JobId> getJobIdentifiers() {
        return Collections.emptySet();
    }

    @Override
    public Set<OwsCode> getProcessIdentifiers() {
        return Collections.emptySet();
    }

    @Override
    public Optional<ProcessDescription> getProcessDescription(OwsCode identifier) {
        return Optional.empty();
    }

    @Override
    public StatusInfo dismiss(JobId identifier)
            throws OwsExceptionReport {
        throw new NoApplicableCodeException().withMessage("Unknown job id");
    }

    @Override
    public JobId execute(OwsCode identifier, List<Data> inputs, List<OutputDefinition> outputs)
            throws OwsExceptionReport {
        throw new NoApplicableCodeException().withMessage("Not yet implemented");
    }

    @Override
    public StatusInfo getStatus(JobId jobId)
            throws OwsExceptionReport {
        throw new NoApplicableCodeException().withMessage("Unknown job id");
    }

    @Override
    public Future<Result> getResult(JobId jobId)
            throws OwsExceptionReport {
        throw new NoApplicableCodeException().withMessage("Unknown job id");
    }

}
