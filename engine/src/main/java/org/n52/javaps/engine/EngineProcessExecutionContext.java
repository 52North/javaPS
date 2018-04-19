/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.JobStatus;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.javaps.description.TypedProcessDescription;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface EngineProcessExecutionContext extends ProcessExecutionContext {

    List<ProcessData> getEncodedOutputs() throws Throwable;

    ResponseMode getResponseMode();

    JobStatus getJobStatus();

    TypedProcessDescription getDescription();

    Map<OwsCode, OutputDefinition> getOutputDefinitions();

    default OwsCode getProcessId() {
        return getDescription().getId();
    }

    @Override
    default Optional<OutputDefinition> getOutputDefinition(OwsCode output) {
        return Optional.ofNullable(getOutputDefinitions().get(output));
    }

}
