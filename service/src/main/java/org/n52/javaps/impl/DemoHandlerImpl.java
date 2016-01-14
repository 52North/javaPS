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
package org.n52.javaps.impl;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsParameterValuePossibleValues;
import org.n52.iceland.ogc.ows.OwsParameterValueRange;
import org.n52.javaps.handler.DemoHandler;
import org.n52.javaps.request.DemoRequest;
import org.n52.javaps.response.DemoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.WPSConstants;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class DemoHandlerImpl implements DemoHandler {

    private static final Set<OperationHandlerKey> OPERATION_HANDLER_KEY
            = Collections.singleton(new OperationHandlerKey(WPSConstants.SERVICE,
                            WPSConstants.OPERATION_DEMO));

    private static final Logger log = LoggerFactory.getLogger(DemoHandlerImpl.class);

    private BindingRepository bindingRepository;

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = bindingRepository;
    }

    @Override
    public DemoResponse demo(DemoRequest request) throws OwsExceptionReport {
        // the actual business logic in this skeleton happens here - could also be partly in the operators!
        log.debug("Handling request {}", request);

        DemoResponse response = request.getResponse();

        ArrayList<String> three = Lists.newArrayList();

        three.add(new StringBuilder(request.getOne()).reverse().toString());
        three.add(new StringBuilder(request.getTwo().toString()).reverse().toString());

        response.setThree(three);

        log.debug("Done handling request; response is {}", response);
        return response;
    }

    @Override
    public String getOperationName() {
        return WPSConstants.OPERATION_DEMO;
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version) throws OwsExceptionReport {
        OwsOperation op = new OwsOperation();
        op.setOperationName(getOperationName());

        op.addParameterValue("anotherParam",
                new OwsParameterValuePossibleValues(Lists.newArrayList("val1", "value1")));
        op.addAnyParameterValue("one");
        op.addParameterValue("two", new OwsParameterValueRange("1", "52"));
        op.addConstraintValue("aConstraint", new OwsParameterValuePossibleValues("constraintToThis"));

        TreeSet<String> outputMediaTypes = this.bindingRepository.getBindingsByMediaType()
                .keySet().stream()
                .map(String::valueOf)
                .collect(TreeSet::new, Set::add, Set::addAll);
        op.addParameterValue(WPSConstants.DemoParam.OUTPUT_FORMAT,
                new OwsParameterValuePossibleValues(outputMediaTypes));

        return op;
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return OPERATION_HANDLER_KEY;
    }

}
