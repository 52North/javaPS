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
package org.n52.javaps.handler;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsNoValues;
import org.n52.iceland.ogc.ows.OwsPossibleValues;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.iceland.ogc.wps.ExecutionMode;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.WPSConstants;
import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerKey;
import org.n52.javaps.Engine;
import org.n52.javaps.EngineException;
import org.n52.javaps.InputDecodingException;
import org.n52.javaps.JobNotFoundException;
import org.n52.javaps.ProcessNotFoundException;
import org.n52.javaps.request.ExecuteRequest;
import org.n52.javaps.response.ExecuteResponse;

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
        // TODO response mode

        String service = request.getService();
        String version = request.getVersion();
        JobId jobId;
        try {
            jobId = getEngine().execute(request.getId(), request.getInputs(), request.getOutputs());
        } catch (ProcessNotFoundException ex) {
            throw new InvalidParameterValueException(IDENTIFIER, request.getId().getValue());
        } catch (InputDecodingException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }

        if (request.getExecutionMode() == ExecutionMode.SYNC) {
            try {
                Result result = getEngine().getResult(jobId).get();
                return new ExecuteResponse(service, version, result);
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
        Set<OwsValue> algorithmIdentifiers = getEngine().getProcessIdentifiers().stream().map(OwsCode::getValue)
                .map(OwsValue::new).collect(toSet());
        OwsPossibleValues possibleValues;
        if (algorithmIdentifiers.isEmpty()) {
            possibleValues = OwsNoValues.instance();
        } else {
            possibleValues = new OwsAllowedValues(algorithmIdentifiers);
        }
        return Collections.singleton(new OwsDomain(IDENTIFIER, possibleValues));
    }

}
