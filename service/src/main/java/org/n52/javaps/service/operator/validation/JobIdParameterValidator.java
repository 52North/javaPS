/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.service.operator.validation;

import org.n52.javaps.engine.EngineException;
import org.n52.shetland.ogc.ows.exception.MissingParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.exception.NoSuchJobException;
import org.n52.shetland.ogc.wps.exception.ResultNotReadyException;
import org.n52.shetland.ogc.wps.request.AbstractJobIdRequest;
import org.n52.shetland.ogc.wps.request.GetResultRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class JobIdParameterValidator extends EngineParameterValidator<AbstractJobIdRequest> {
    private static final String JOB_ID = "JobId";

    @Override
    public void validate(AbstractJobIdRequest request) throws OwsExceptionReport {
        JobId jobId = request.getJobId();
        if (jobId == null || jobId.getValue() == null || jobId.getValue().isEmpty()) {
            throw new MissingParameterValueException(JOB_ID);
        }

        if (!getEngine().hasJob(jobId)) {
            throw new NoSuchJobException(JOB_ID, jobId.getValue());
        }

        boolean resultReady = false;
        try {
            resultReady = getEngine().getResult(jobId).isDone();
        } catch (EngineException e) {
            // do nothing
        }

        if (!resultReady && request instanceof GetResultRequest) {
            throw new ResultNotReadyException(JOB_ID, jobId.getValue());
        }
    }

}
