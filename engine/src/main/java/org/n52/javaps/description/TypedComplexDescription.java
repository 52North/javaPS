/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.wps.description.ComplexDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.complex.ComplexData;

/**
 *
 * @author Christian Autermann
 */
public interface TypedComplexDescription extends ComplexDescription, TypedDataDescription<Class<? extends ComplexData<?>>> {

    @Override
    default Class<?> getPayloadType() {
        try {
            return getType().getMethod("getPayload").getReturnType();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new Error(ex);
        }
    }

    @Override
    public default Class<? extends Data<?>> getBindingType() {
        return getType();
    }

    interface Builder<T extends TypedComplexDescription, B extends Builder<T, B>>
            extends ComplexDescription.Builder<T, B>,
                    TypedDataDescription.Builder<Class<? extends ComplexData<?>>, T, B> {
    }

}
