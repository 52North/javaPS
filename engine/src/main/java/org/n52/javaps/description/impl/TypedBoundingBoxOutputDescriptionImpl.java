/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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

import org.n52.javaps.description.TypedBoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.BoundingBoxOutputDescriptionImpl;

public class TypedBoundingBoxOutputDescriptionImpl extends BoundingBoxOutputDescriptionImpl
        implements TypedBoundingBoxOutputDescription {

    protected TypedBoundingBoxOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    protected abstract static class AbstractBuilder<T extends TypedBoundingBoxOutputDescription,
                                                        B extends AbstractBuilder<T, B>>
            extends BoundingBoxOutputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedBoundingBoxOutputDescription.Builder<T, B> {
        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  BoundingBoxOutputDescription entity) {
            super(factory, entity);
        }

        protected AbstractBuilder(
                ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }
    }

    public static class Builder extends AbstractBuilder<TypedBoundingBoxOutputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          BoundingBoxOutputDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public TypedBoundingBoxOutputDescription build() {
            return new TypedBoundingBoxOutputDescriptionImpl(this);
        }
    }

}
