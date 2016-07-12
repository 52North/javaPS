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
import java.util.Collection;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessExecutionContext {

    JobId getJobId();

    OwsCode getProcessId();

    ProcessInputs getInputs();

    ProcessOutputs getOutputs();

    Collection<OutputDefinition> getOutputDefinitions();

    void setPercentCompleted(Short percentCompleted);

    void setEstimatedCompletion(OffsetDateTime estimatedCompletion);

    void setNextPoll(OffsetDateTime nextPoll);
}
