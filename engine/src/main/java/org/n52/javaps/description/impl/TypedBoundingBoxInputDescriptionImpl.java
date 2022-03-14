/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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

import org.n52.javaps.description.TypedBoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.BoundingBoxInputDescriptionImpl;

public class TypedBoundingBoxInputDescriptionImpl extends BoundingBoxInputDescriptionImpl
        implements TypedBoundingBoxInputDescription {

    protected TypedBoundingBoxInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    protected abstract static class AbstractBuilder<T extends TypedBoundingBoxInputDescription,
                                                        B extends AbstractBuilder<T, B>>
            extends BoundingBoxInputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedBoundingBoxInputDescription.Builder<T, B> {
        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  BoundingBoxInputDescription entity) {
            super(factory, entity);
        }

        protected AbstractBuilder(
                ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }
    }

    public static class Builder extends AbstractBuilder<TypedBoundingBoxInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          BoundingBoxInputDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public TypedBoundingBoxInputDescription build() {
            return new TypedBoundingBoxInputDescriptionImpl(this);
        }
    }
}
