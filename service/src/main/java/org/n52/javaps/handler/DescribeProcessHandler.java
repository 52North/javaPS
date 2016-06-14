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
import java.util.List;
import java.util.Set;

import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.util.http.HTTPStatus;
import org.n52.javaps.algorithm.ProcessDescription;
import org.n52.javaps.algorithm.RepositoryManager;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.DescribeProcessRequest;
import org.n52.javaps.response.DescribeProcessResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessHandler implements
        GenericHandler<DescribeProcessRequest, DescribeProcessResponse> {

    @Autowired
    private RepositoryManager repositoryManager;

    @Override
    public DescribeProcessResponse handler(DescribeProcessRequest request)
            throws OwsExceptionReport {

        DescribeProcessResponse describeProcessResponse = (DescribeProcessResponse) new DescribeProcessResponse().set(request);

        List<String> identifiers = request.getProcessIdentifier();

        for (String identifier : identifiers) {

            try {
                ProcessDescription processDescription = repositoryManager.getAlgorithm(identifier).getDescription();

                describeProcessResponse.addProcessDescription(processDescription);

            } catch (NullPointerException e) {
                throw new InvalidParameterValueException("identifer", identifier).setStatus(HTTPStatus.BAD_REQUEST);
            }
        }

        return describeProcessResponse;
    }

     @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.toString();
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
        return new OwsOperation();
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.DescribeProcess.toString()));
    }

}
