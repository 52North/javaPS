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

import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.GetStatusRequest;
import org.n52.javaps.response.GetStatusResponse;
import org.n52.iceland.ds.GenericOperationHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetStatusHandler implements
        GenericOperationHandler<GetStatusRequest, GetStatusResponse> {

    @Override
    public GetStatusResponse handler(GetStatusRequest request)
            throws OwsExceptionReport {
        return request.getResponse();
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetStatus.toString();
    }


    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections
                .singleton(new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.GetStatus
                                                   .toString()));
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
        /* TODO implement org.n52.javaps.handler.GetStatusHandler.getOperationsMetadata() */
        throw new UnsupportedOperationException("org.n52.javaps.handler.GetStatusHandler.getOperationsMetadata() not yet implemented");
    }

}
