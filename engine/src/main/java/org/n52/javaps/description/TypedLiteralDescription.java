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

import org.n52.shetland.ogc.wps.description.LiteralDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.literal.LiteralData;
import org.n52.javaps.io.literal.LiteralType;

/**
 *
 * @author Christian Autermann
 */
public interface TypedLiteralDescription extends LiteralDescription, TypedDataDescription<LiteralType<?>> {

    @Override
    default Class<?> getPayloadType() {
        return getType().getPayloadType();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default Class<? extends Data<?>> getBindingType() {
        Class<? extends Data> c = LiteralData.class;
        return (Class<? extends Data<?>>) c;

    }

    interface Builder<T extends TypedLiteralDescription, B extends Builder<T, B>>
            extends LiteralDescription.Builder<T, B>,
                    TypedDataDescription.Builder<LiteralType<?>, T, B> {
    }

}
