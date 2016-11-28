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
package org.n52.javaps.description;

import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.iceland.ogc.wps.description.BoundingBoxDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.bbox.BoundingBoxData;

/**
 *
 * @author Christian Autermann
 */
public interface TypedBoundingBoxDescription extends BoundingBoxDescription,
                                                     TypedDataDescription<Class<? extends BoundingBoxData>> {

    @Override
    default Class<?> getPayloadType() {
        return OwsBoundingBox.class;
    }

    @Override
    default Class<? extends Data<?>> getBindingType() {
        return BoundingBoxData.class;
    }

    @Override
    public default Class<? extends BoundingBoxData> getType() {
        return BoundingBoxData.class;
    }

    interface Builder<T extends TypedBoundingBoxDescription, B extends Builder<T, B>>
            extends BoundingBoxDescription.Builder<T, B> {

    }

}
