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
import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.DescribeProcessResponse;

public class DescribeProcessRequest extends
        AbstractServiceRequest<DescribeProcessResponse> {
    public static final String ALL_KEYWORD = "ALL";

    private final List<OwsCode> identifiers = new LinkedList<>();

    @Override
    public DescribeProcessResponse getResponse() {
        return (DescribeProcessResponse) new DescribeProcessResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.name();
    }

    public List<OwsCode> getProcessIdentifier() {
        return Collections.unmodifiableList(identifiers);
    }

    public void addProcessIdentifier(String identifier) {
        addProcessIdentifier(new OwsCode(identifier));
    }

    public void addProcessIdentifier(OwsCode identifier) {
        this.identifiers.add(Objects.requireNonNull(identifier));
    }

    public boolean isAll() {
        return getProcessIdentifier().stream()
                .filter(id -> !id.getCodeSpace().isPresent())
                .anyMatch(id -> id.getValue().equalsIgnoreCase(ALL_KEYWORD));
    }

}
