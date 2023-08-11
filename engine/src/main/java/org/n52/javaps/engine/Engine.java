/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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
package org.n52.javaps.engine;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.description.ProcessDescription;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface Engine {

    Set<JobId> getJobIdentifiers();

    Set<JobId> getJobIdentifiers(OwsCode identifier);

    default boolean hasJob(JobId jobId) {
        return getJobIdentifiers().contains(jobId);
    }

    Set<OwsCode> getProcessIdentifiers();

    Optional<ProcessDescription> getProcessDescription(OwsCode identifier);

    default boolean hasProcessDescription(OwsCode identifier) {
        return getProcessDescription(identifier).isPresent();
    }

    default Set<ProcessDescription> getProcessDescriptions() {
        return getProcessIdentifiers().stream().map(this::getProcessDescription).map(Optional::get).collect(toSet());
    }

    StatusInfo dismiss(JobId identifier) throws JobNotFoundException;

    JobId execute(OwsCode identifier,
            List<ProcessData> inputs,
            List<OutputDefinition> outputs,
            ResponseMode responseMode) throws ProcessNotFoundException, InputDecodingException;

    StatusInfo getStatus(JobId jobId) throws EngineException;

    Future<Result> getResult(JobId jobId) throws EngineException;

}
