/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.n52.shetland.ogc.wps.ExecutionMode;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerKey;
import org.n52.janmayen.http.MediaType;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.wps.request.ExecuteRequest;
import org.n52.shetland.ogc.wps.response.ExecuteResponse;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsDomain;
import org.n52.shetland.ogc.ows.OwsNoValues;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ExecuteHandler extends AbstractEngineHandler
        implements GenericOperationHandler<ExecuteRequest, ExecuteResponse> {
    private static final String IDENTIFIER = "Identifier";
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.Execute);

    @Inject
    public ExecuteHandler(Engine engine) {
        super(engine);
    }

    @Override
    public ExecuteResponse handle(ExecuteRequest request)
            throws OwsExceptionReport {

        String service = request.getService();
        String version = request.getVersion();
        JobId jobId;
        try {
            jobId = getEngine().execute(request.getId(), request.getInputs(), request.getOutputs(), request.getResponseMode());
        } catch (ProcessNotFoundException ex) {
            throw new InvalidParameterValueException(IDENTIFIER, request.getId().getValue());
        } catch (InputDecodingException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }

        if (request.getExecutionMode() == ExecutionMode.SYNC) {
            try {
                Result result = getEngine().getResult(jobId).get();
                ExecuteResponse response = new ExecuteResponse(service, version, result);

                if (request.getResponseMode() == ResponseMode.RAW) {
                    ProcessData data = response.getResult().get().getOutputs().iterator().next();
                    if (data.isValue()) {
                        response.setContentType(data.asValue().getFormat()
                                .getMimeType().map(MediaType::parse)
                                .orElseGet(MediaType::new));
                    }
                }
                return response;

            } catch (InterruptedException | JobNotFoundException ex) {
                throw new NoApplicableCodeException().causedBy(ex);
            } catch (ExecutionException ex) {
                throw new NoApplicableCodeException().causedBy(ex.getCause());
            } catch (EngineException ex) {
                throw new NoApplicableCodeException().causedBy(ex);
            }
        } else {
            StatusInfo status;
            try {
                status = getEngine().getStatus(jobId);
            } catch (EngineException ex) {
                throw new NoApplicableCodeException().causedBy(ex);
            }
            return new ExecuteResponse(service, version, status);
        }
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Execute.toString();
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected Set<OwsDomain> getOperationParameters(String service, String version) {
        Set<OwsValue> algorithmIdentifiers = getEngine().getProcessIdentifiers().stream()
                .map(OwsCode::getValue).map(OwsValue::new).collect(toSet());
        OwsPossibleValues possibleValues;
        if (algorithmIdentifiers.isEmpty()) {
            possibleValues = OwsNoValues.instance();
        } else {
            possibleValues = new OwsAllowedValues(algorithmIdentifiers);
        }
        return Collections.singleton(new OwsDomain(IDENTIFIER, possibleValues));
    }

}
