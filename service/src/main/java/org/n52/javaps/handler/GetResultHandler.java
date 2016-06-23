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


import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.n52.iceland.ds.GenericOperationHandler;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.Engine;
import org.n52.javaps.ogc.wps.Result;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.GetResultRequest;
import org.n52.javaps.response.GetResultResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetResultHandler extends AbstractJobHandler
        implements GenericOperationHandler<GetResultRequest, GetResultResponse> {
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.GetResult);

    public GetResultHandler(Engine engine, boolean discloseJobIds) {
        super(engine, discloseJobIds);
    }

    @Override
    public GetResultResponse handler(GetResultRequest request)
            throws OwsExceptionReport {

        String service = request.getService();
        String version = request.getVersion();

        try {
            Future<Result> result = getEngine().getResult(request.getJobId());

            return new GetResultResponse(service, version, result.get());
        } catch (InterruptedException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        } catch (ExecutionException ex) {
            throw new NoApplicableCodeException().causedBy(ex.getCause());
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

}
