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
package org.n52.javaps.description.impl;

import org.n52.javaps.description.TypedLiteralInputDescription;
import org.n52.javaps.io.literal.LiteralType;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.impl.LiteralInputDescriptionImpl;

import java.util.Objects;

public class TypedLiteralInputDescriptionImpl extends LiteralInputDescriptionImpl
        implements TypedLiteralInputDescription {

    private static final String TYPE_STRING = "type";
    private final LiteralType<?> type;

    protected TypedLiteralInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.type = Objects.requireNonNull(builder.getType(), TYPE_STRING);
    }

    @Override
    public LiteralType<?> getType() {
        return this.type;
    }

    protected abstract static class AbstractBuilder<T extends TypedLiteralInputDescription,
                                                           B extends AbstractBuilder<T, B>>
            extends LiteralInputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedLiteralInputDescription.Builder<T, B> {

        private LiteralType<?> type;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  LiteralInputDescription entity) {
            super(factory, entity);
            if (entity instanceof TypedLiteralInputDescription) {
                this.type = ((TypedLiteralInputDescription) entity).getType();
            }
        }

        @Override
        public B withType(LiteralType<?> type) {
            this.type = Objects.requireNonNull(type, TYPE_STRING);
            return self();
        }

        public LiteralType<?> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedLiteralInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          LiteralInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public TypedLiteralInputDescription build() {
            return new TypedLiteralInputDescriptionImpl(this);
        }
    }
}
