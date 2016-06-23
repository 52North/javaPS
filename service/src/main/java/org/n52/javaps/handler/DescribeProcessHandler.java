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

import org.n52.iceland.ds.GenericOperationHandler;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.javaps.Engine;
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
public class DescribeProcessHandler extends AbstractEngineHandler
        implements GenericOperationHandler<DescribeProcessRequest, DescribeProcessResponse> {
    private static final String IDENTIFIER = "Identifier";
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE,
                                      WPSConstants.Operations.DescribeProcess);

    public DescribeProcessHandler(Engine engine) {
        super(engine);
    }


    @Override
    public DescribeProcessResponse handler(DescribeProcessRequest request)
            throws OwsExceptionReport {

        Set<ProcessOffering> offerings = request.getProcessIdentifier().stream()
                        .map(getEngine()::getProcessDescription)
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
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected Set<OwsDomain> getOperationParameters() {
        Stream<OwsValue> specialIdentifiers = Stream.of(new OwsValue(DescribeProcessRequest.ALL_KEYWORD));
        Stream<OwsValue> algorithmIdentifiers = getEngine().getProcessIdentifiers().stream().map(OwsCode::getValue).map(OwsValue::new);
        OwsDomain identifierDomain = new OwsDomain(IDENTIFIER, new OwsAllowedValues(Stream.concat(specialIdentifiers, algorithmIdentifiers).collect(toSet())));
        return Collections .singleton(identifierDomain);
    }

}
