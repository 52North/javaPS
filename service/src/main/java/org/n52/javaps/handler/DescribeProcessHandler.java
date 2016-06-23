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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.ds.GenericOperationHandler;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsDCP;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsMetadata;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.javaps.algorithm.RepositoryManager;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.ogc.wps.ProcessOfferings;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.DescribeProcessRequest;
import org.n52.javaps.response.DescribeProcessResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessHandler extends AbstractHandler
        implements GenericOperationHandler<DescribeProcessRequest, DescribeProcessResponse> {
    private static final String IDENTIFIER = "identifier";
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE,
                                      WPSConstants.Operations.DescribeProcess);

    private final RepositoryManager repositoryManager;

    @Inject
    public DescribeProcessHandler(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Override
    public DescribeProcessResponse handler(DescribeProcessRequest request)
            throws OwsExceptionReport {

        Set<ProcessOffering> offerings = request.getProcessIdentifier().stream()
                        .map(repositoryManager::getProcessDescription)
                        .filter(Optional::isPresent).map(Optional::get)
                        .map(ProcessOffering::new).collect(toSet());

        return new DescribeProcessResponse(
                request.getService(),
                request.getVersion(),
                new ProcessOfferings(offerings));
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.toString();
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
        Set<OwsDomain> constraints = null;
        Set<OwsMetadata> metadata = null;
        Set<OwsValue> identifiers = Stream.concat(
                Stream.of(new OwsValue(DescribeProcessRequest.ALL_KEYWORD)),
                repositoryManager.getAlgorithms().stream()
                .map(OwsCode::getValue).map(OwsValue::new))
                .collect(toSet());
        Set<OwsDomain> parameters = Collections
                .singleton(new OwsDomain(IDENTIFIER, new OwsAllowedValues(identifiers)));
        Set<OwsDCP> dcp = Collections.singleton(getDCP(service, version));
        return new OwsOperation(getOperationName(), parameters, constraints, metadata, dcp);
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
