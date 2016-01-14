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
package org.n52.wps.request;

import com.google.common.base.MoreObjects;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.wps.SkeletonConstants;
import org.n52.wps.response.DemoResponse;

/**
 * A POJO for the request, including a helper constructor to pass input parameters to the response.
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class DemoRequest extends AbstractServiceRequest<DemoResponse> {

    private String one;

    private Integer two;

    public DemoRequest(String service) {
        setService(service);
    }

    @Override
    public DemoResponse getResponse() throws OwsExceptionReport {
        return (DemoResponse) new DemoResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return SkeletonConstants.OPERATION_DEMO;
    }

    public String getOne() {
        return one;
    }

    public DemoRequest setOne(String one) {
        this.one = one;
        return this;
    }

    public Integer getTwo() {
        return two;
    }

    public DemoRequest setTwo(Integer two) {
        this.two = two;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("one", one)
                .add("two", two)
                .toString();
    }

}
