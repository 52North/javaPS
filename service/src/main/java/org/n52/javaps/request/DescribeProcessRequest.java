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
package org.n52.javaps.request;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.algorithm.ProcessDescription;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.DescribeProcessResponse;

public class DescribeProcessRequest extends
        AbstractServiceRequest<DescribeProcessResponse> {

    private List<String> identifiers = new LinkedList<>();

    private boolean all = false;

    @Override
    public DescribeProcessResponse getResponse()
            throws OwsExceptionReport {

        DescribeProcessResponse describeProcessResponse = (DescribeProcessResponse) new DescribeProcessResponse().set(this);

        for (String identifier : identifiers) {

            ProcessDescription processDescription = new ProcessDescription();

            describeProcessResponse.addProcessDescription(processDescription);
        }

        return describeProcessResponse;
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.name();
    }

    public List<String> getProcessIdentifier() {
        return Collections.unmodifiableList(identifiers);
    }

    public void addProcessIdentifier(String identifier) {
        this.identifiers.add(identifier);
    }

    public boolean isAll() {
        return getProcessIdentifier().stream().anyMatch(id -> id
                .equalsIgnoreCase("ALL"));
    }

}
