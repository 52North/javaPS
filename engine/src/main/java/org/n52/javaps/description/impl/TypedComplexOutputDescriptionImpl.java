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

import org.n52.javaps.description.TypedComplexOutputDescription;
import org.n52.javaps.io.complex.ComplexData;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.ComplexOutputDescriptionImpl;

import java.util.Objects;

public class TypedComplexOutputDescriptionImpl extends ComplexOutputDescriptionImpl
        implements TypedComplexOutputDescription {
    private final Class<? extends ComplexData<?>> type;

    public TypedComplexOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.type = Objects.requireNonNull(builder.getType(), "type");
    }

    @Override
    public Class<? extends ComplexData<?>> getType() {
        return this.type;
    }

    protected abstract static class AbstractBuilder<T extends TypedComplexOutputDescription,
                                                           B extends AbstractBuilder<T, B>>
            extends ComplexOutputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedComplexOutputDescription.Builder<T, B> {
        private Class<? extends ComplexData<?>> type;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  ComplexOutputDescription entity) {
            super(factory, entity);
            if (entity instanceof TypedComplexOutputDescription) {
                this.type = ((TypedComplexOutputDescription) entity).getType();
            }
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withType(Class<? extends ComplexData<?>> type) {
            this.type = Objects.requireNonNull(type);
            return self();
        }

        public Class<? extends ComplexData<?>> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedComplexOutputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          ComplexOutputDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public TypedComplexOutputDescription build() {
            return new TypedComplexOutputDescriptionImpl(this);
        }

    }

}
