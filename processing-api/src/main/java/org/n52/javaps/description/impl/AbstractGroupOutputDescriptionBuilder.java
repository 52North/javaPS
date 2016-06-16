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
package org.n52.javaps.description.impl;

import java.util.Map;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.GroupOutputDescriptionBuilder;
import org.n52.javaps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractGroupOutputDescriptionBuilder<T extends GroupOutputDescription, B extends AbstractGroupOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B>
        implements GroupOutputDescriptionBuilder<T, B> {

    private final ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> inputs
            = ImmutableMap.builder();

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withOutput(ProcessOutputDescription input) {
        if (input != null) {
            this.inputs.put(input.getId(), input);
        }
        return (B) this;
    }

    Map<OwsCodeType, ProcessOutputDescription> getOutputs() {
        return this.inputs.build();
    }

}
