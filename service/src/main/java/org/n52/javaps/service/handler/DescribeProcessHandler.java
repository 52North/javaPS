/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerKey;
import org.n52.javaps.engine.Engine;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsDomain;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ProcessOfferings;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.wps.request.DescribeProcessRequest;
import org.n52.shetland.ogc.wps.response.DescribeProcessResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessHandler extends AbstractEngineHandler implements GenericOperationHandler<
        DescribeProcessRequest, DescribeProcessResponse> {
    private static final String IDENTIFIER = "Identifier";

    private static final OperationHandlerKey KEY = new OperationHandlerKey(WPSConstants.SERVICE,
            WPSConstants.Operations.DescribeProcess);

    @Inject
    public DescribeProcessHandler(Engine engine) {
        super(engine);
    }

    @Override
    public DescribeProcessResponse handle(DescribeProcessRequest request) throws OwsExceptionReport {

        Set<ProcessOffering> offerings;

        if (request.isAll()) {
            offerings = getEngine().getProcessDescriptions().stream().map(ProcessOffering::new).collect(toSet());
        } else {
            offerings = request.getProcessIdentifier().stream().map(getEngine()::getProcessDescription)
            .filter(Optional::isPresent).map(Optional::get).map(ProcessOffering::new).collect(toSet());
        }

        return new DescribeProcessResponse(request.getService(), request.getVersion(), new ProcessOfferings(offerings));
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
    protected Set<OwsDomain> getOperationParameters(String service,
            String version) {
        Stream<OwsValue> specialIdentifiers = Stream.of(new OwsValue(DescribeProcessRequest.ALL_KEYWORD));
        Stream<OwsValue> algorithmIdentifiers = getEngine().getProcessIdentifiers().stream().map(OwsCode::getValue).map(
                OwsValue::new);
        OwsDomain identifierDomain = new OwsDomain(IDENTIFIER, new OwsAllowedValues(Stream.concat(specialIdentifiers,
                algorithmIdentifiers).collect(toSet())));
        return Collections.singleton(identifierDomain);
    }

    @Override
    public boolean isSupported() {
        return true;
    }

}
