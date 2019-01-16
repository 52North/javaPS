/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.description.impl.ComplexInputDescriptionImpl;
import org.n52.javaps.description.TypedComplexInputDescription;
import org.n52.javaps.io.complex.ComplexData;

public class TypedComplexInputDescriptionImpl extends ComplexInputDescriptionImpl implements
        TypedComplexInputDescription {

    private static final String TYPE_STRING = "type";
    private final Class<? extends ComplexData<?>> type;

    public TypedComplexInputDescriptionImpl(OwsCode id, OwsLanguageString title, OwsLanguageString abstrakt, Set<
            OwsKeyword> keywords, Set<OwsMetadata> metadata, InputOccurence occurence, Format defaultFormat, Set<
                    Format> supportedFormat, BigInteger maximumMegabytes, Class<? extends ComplexData<?>> type) {
        super(id, title, abstrakt, keywords, metadata, occurence, defaultFormat, supportedFormat, maximumMegabytes);
        this.type = Objects.requireNonNull(type, TYPE_STRING);
    }

    protected TypedComplexInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        this(builder.getId(), builder.getTitle(), builder.getAbstract(), builder.getKeywords(), builder.getMetadata(),
                new InputOccurence(builder.getMinimalOccurence(), builder.getMaximalOccurence()), builder
                        .getDefaultFormat(), builder.getSupportedFormats(), builder.getMaximumMegabytes(), builder
                                .getType());
    }

    @Override
    public Class<? extends ComplexData<?>> getType() {
        return this.type;
    }

    public abstract static class AbstractBuilder<T extends TypedComplexInputDescription, B extends AbstractBuilder<T,
            B>> extends ComplexInputDescriptionImpl.AbstractBuilder<T, B> implements
            TypedComplexInputDescription.Builder<T, B> {

        private Class<? extends ComplexData<?>> type;

        @Override
        @SuppressWarnings("unchecked")
        public B withType(Class<? extends ComplexData<?>> type) {
            this.type = Objects.requireNonNull(type, TYPE_STRING);
            return (B) this;
        }

        public Class<? extends ComplexData<?>> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedComplexInputDescription, Builder> {
        @Override
        public TypedComplexInputDescription build() {
            return new TypedComplexInputDescriptionImpl(this);
        }
    }
}
