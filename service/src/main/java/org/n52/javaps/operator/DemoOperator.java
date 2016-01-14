/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.operator;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ds.OperationHandlerRepository;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.operator.RequestOperator;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.javaps.WPSConstants;
import org.n52.javaps.handler.DemoHandler;
import org.n52.javaps.request.DemoRequest;

/**
 * The operator mapps an incoming request to the handler.
 *
 * The operator and handler layers allow to separate parameter validation in different operators, but then internally use the same handlers.
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class DemoOperator implements RequestOperator {

    private static final Logger log = LoggerFactory.getLogger(DemoOperator.class);

    private static final RequestOperatorKey KEY
            = new RequestOperatorKey(
                    WPSConstants.SERVICE,
                    WPSConstants.VERSION,
                    WPSConstants.OPERATION_DEMO);

    private OperationHandlerRepository operationHandlerRepository;

    @Inject
    public void setOperationHandlerRepository(OperationHandlerRepository repo) {
        this.operationHandlerRepository = repo;
    }

    @Override
    public AbstractServiceResponse receiveRequest(AbstractServiceRequest<?> request) throws OwsExceptionReport {
        log.debug("Receiving request: {}", request);
        return getHandler(request).demo((DemoRequest) request);
    }

    private DemoHandler getHandler(AbstractServiceRequest<?> request) {
        String service = request.getService();
        String operationName = request.getOperationName();
        return getHandler(service, operationName);
    }

    private DemoHandler getHandler(String service, String operationName) {
        DemoHandler handler = (DemoHandler) operationHandlerRepository.getOperationHandler(service, operationName);
        log.trace("Returning handler for service/operation {}/{}: {}", service, operationName, handler);
        return handler;
    }

    @Override
    public OwsOperation getOperationMetadata(String service, String version) throws OwsExceptionReport {
        log.trace("Creaging metadata for service {} in version {}", service, version);
        return getHandler(service, KEY.getOperationName()).getOperationsMetadata(service, version);
    }

    @Override
    public Set<RequestOperatorKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
