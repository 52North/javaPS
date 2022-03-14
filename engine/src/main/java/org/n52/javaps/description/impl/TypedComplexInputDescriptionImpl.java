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

import org.n52.javaps.description.TypedComplexInputDescription;
import org.n52.javaps.io.complex.ComplexData;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.ComplexInputDescriptionImpl;

import java.util.Objects;

public class TypedComplexInputDescriptionImpl extends ComplexInputDescriptionImpl
        implements TypedComplexInputDescription {

    private static final String TYPE_STRING = "type";
    private final Class<? extends ComplexData<?>> type;

    protected TypedComplexInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.type = Objects.requireNonNull(builder.getType(), TYPE_STRING);
    }

    @Override
    public Class<? extends ComplexData<?>> getType() {
        return this.type;
    }

    protected abstract static class AbstractBuilder<T extends TypedComplexInputDescription,
                                                           B extends AbstractBuilder<T, B>>
            extends ComplexInputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedComplexInputDescription.Builder<T, B> {

        private Class<? extends ComplexData<?>> type;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  ComplexInputDescription entity) {
            super(factory, entity);
            if (entity instanceof TypedComplexInputDescription) {
                this.type = ((TypedComplexInputDescription) entity).getType();
            }
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withType(Class<? extends ComplexData<?>> type) {
            this.type = Objects.requireNonNull(type, TYPE_STRING);
            return self();
        }

        public Class<? extends ComplexData<?>> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedComplexInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          ComplexInputDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public TypedComplexInputDescription build() {
            return new TypedComplexInputDescriptionImpl(this);
        }
    }
}
