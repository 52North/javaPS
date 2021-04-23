/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessExecutionContext {

    JobId getJobId();

    ProcessInputs getInputs();

    ProcessOutputs getOutputs();

    Optional<OutputDefinition> getOutputDefinition(OwsCode output);

    default Optional<OutputDefinition> getOutputDefinition(String output) {
        return getOutputDefinition(new OwsCode(output));
    }

    void setPercentCompleted(Short percentCompleted);

    void setEstimatedCompletion(OffsetDateTime estimatedCompletion);

    void setNextPoll(OffsetDateTime nextPoll);

    TypedProcessDescription getDescription();

    default boolean hasOutputDefinition(String output) {
        return hasOutputDefinition(new OwsCode(output));
    }

    default boolean hasOutputDefinition(OwsCode output) {
        return getOutputDefinition(output).isPresent();
    }

    /**
     * Add an callback that is called once this {@link ProcessExecutionContext} is destroyed.
     *
     * @param runnable The {@link Runnable} to call.
     */
    void onDestroy(Runnable runnable);
}
