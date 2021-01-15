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

import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.javaps.io.bbox.BoundingBoxData;

/**
 *
 * @author Christian Autermann
 */
public interface TypedBoundingBoxInputDescription extends BoundingBoxInputDescription, TypedBoundingBoxDescription,
        TypedProcessInputDescription<Class<? extends BoundingBoxData>> {
    @Override
    default TypedBoundingBoxInputDescription asBoundingBox() {
        return this;
    }

    interface Builder<T extends TypedBoundingBoxInputDescription, B extends Builder<T, B>> extends
            BoundingBoxInputDescription.Builder<T, B>, TypedBoundingBoxDescription.Builder<T, B> {
    }
}
