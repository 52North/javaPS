package org.n52.javaps;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.ogc.wps.OutputDefinition;
import org.n52.javaps.ogc.wps.Result;
import org.n52.javaps.ogc.wps.StatusInfo;
import org.n52.javaps.ogc.wps.data.Data;
import org.n52.javaps.ogc.wps.description.ProcessDescription;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface Engine {

    Set<JobId> getJobIdentifiers();

    Set<OwsCode> getProcessIdentifiers();

    Optional<ProcessDescription> getProcessDescription(OwsCode identifier);

    StatusInfo dismiss(JobId identifier);

    JobId execute(OwsCode identifier, List<Data> inputs, List<OutputDefinition> outputs);

    StatusInfo getStatus(JobId jobId);

    Future<Result> getResult(JobId jobId);

}
