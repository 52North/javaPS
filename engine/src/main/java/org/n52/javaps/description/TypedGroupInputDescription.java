/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.GroupInputData;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;

/**
 * @author Christian Autermann
 */
public interface TypedGroupInputDescription extends GroupInputDescription, TypedProcessInputDescriptionContainer,
                                                    TypedProcessInputDescription<Class<? extends Data<ProcessInputs>>> {

    @Override
    default Class<? extends Data<ProcessInputs>> getType() {
        return GroupInputData.class;
    }

    @Override
    default Class<?> getPayloadType() {
        return ProcessInputs.class;
    }

    @Override
    default Class<? extends Data<?>> getBindingType() {
        return GroupInputData.class;
    }

    @Override
    default TypedGroupInputDescription asGroup() {
        return this;
    }

    interface Builder<T extends TypedGroupInputDescription, B extends Builder<T, B>>
            extends GroupInputDescription.Builder<T, B>,
                    TypedProcessInputDescriptionContainer.Builder<T, B> {
    }
}
