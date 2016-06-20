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
package org.n52.javaps.response;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.ogc.wps.WPSConstants;

public class WpsDescribeProcessResponse extends AbstractServiceResponse {

    private final List<ProcessOffering> offerings = new LinkedList<>();

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.name();
    }

    public List<ProcessOffering> getProcessOffering() {
        return Collections.unmodifiableList(this.offerings);
    }

    public void addProcessOffering(ProcessOffering description) {
        this.offerings.add(Objects.requireNonNull(description));
    }

}
