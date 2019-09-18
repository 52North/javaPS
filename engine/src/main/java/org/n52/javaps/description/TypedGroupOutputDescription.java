/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.description;

import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.GroupOutputData;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;

/**
 * @author Christian Autermann
 */
public interface TypedGroupOutputDescription
        extends GroupOutputDescription, TypedProcessOutputDescriptionContainer,
                TypedProcessOutputDescription<Class<? extends Data<ProcessOutputs>>> {

    @Override
    default Class<? extends Data<ProcessOutputs>> getType() {
        return GroupOutputData.class;
    }

    @Override
    default Class<?> getPayloadType() {
        return ProcessOutputs.class;
    }

    @Override
    default Class<? extends Data<?>> getBindingType() {
        return GroupOutputData.class;
    }

    @Override
    default TypedGroupOutputDescription asGroup() {
        return this;
    }

    interface Builder<T extends TypedGroupOutputDescription, B extends Builder<T, B>>
            extends GroupOutputDescription.Builder<T, B>,
                    TypedProcessOutputDescriptionContainer.Builder<T, B> {
    }

}
