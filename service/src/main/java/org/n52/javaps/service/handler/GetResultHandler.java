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
package org.n52.javaps.service.handler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerKey;
import org.n52.janmayen.http.MediaType;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.wps.request.GetResultRequest;
import org.n52.shetland.ogc.wps.response.GetResultResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetResultHandler extends AbstractJobHandler implements GenericOperationHandler<GetResultRequest,
        GetResultResponse> {
    private static final OperationHandlerKey KEY = new OperationHandlerKey(WPSConstants.SERVICE,
            WPSConstants.Operations.GetResult);

    @Inject
    public GetResultHandler(Engine engine) {
        super(engine, true);
    }

    @Override
    public GetResultResponse handle(GetResultRequest request) throws OwsExceptionReport {

        String service = request.getService();
        String version = request.getVersion();
        JobId jobId = request.getJobId();

        try {
            Future<Result> result = getEngine().getResult(jobId);
            GetResultResponse response = new GetResultResponse(service, version, result.get());

            if (response.getResult().getResponseMode() == ResponseMode.RAW) {
                response.setContentType(new MediaType());
            }

            return response;
        } catch (JobNotFoundException ex) {
            throw new InvalidParameterValueException(JOB_ID, jobId.getValue()).causedBy(ex);
        } catch (InterruptedException | EngineException ex) {
            throw createNoApplicableCodeExceptionWithHttpStatusInternalServerError(ex);
        } catch (ExecutionException ex) {
            throw createNoApplicableCodeExceptionWithHttpStatusInternalServerError(ex.getCause());
        }

    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetResult.toString();
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    public boolean isSupported() {
        return true;
    }

}
