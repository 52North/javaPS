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
package org.n52.iceland.ogc.wps.description.typed.impl;

import java.util.Objects;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCRS;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsKeyword;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.iceland.ogc.ows.OwsMetadata;
import org.n52.iceland.ogc.wps.description.impl.BoundingBoxOutputDescriptionImpl;
import org.n52.iceland.ogc.wps.description.typed.TypedBoundingBoxOutputDescription;
import org.n52.javaps.io.bbox.BoundingBoxData;

public class TypedBoundingBoxOutputDescriptionImpl
        extends BoundingBoxOutputDescriptionImpl
        implements TypedBoundingBoxOutputDescription {
    private final Class<? extends BoundingBoxData<?>> type;

    public TypedBoundingBoxOutputDescriptionImpl(OwsCode id,
                                                 OwsLanguageString title,
                                                 OwsLanguageString abstrakt,
                                                 Set<OwsKeyword> keywords,
                                                 Set<OwsMetadata> metadata,
                                                 OwsCRS defaultCRS,
                                                 Set<OwsCRS> supportedCRS,
                                                 Class<? extends BoundingBoxData<?>> type) {
        super(id, title, abstrakt, keywords, metadata, defaultCRS, supportedCRS);
        this.type = Objects.requireNonNull(type, "type");
    }

    protected TypedBoundingBoxOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getDefaultCRS(),
             builder.getSupportedCRS(),
             builder.getType());
    }

    @Override
    public Class<? extends BoundingBoxData<?>> getType() {
        return this.type;
    }

    public static abstract class AbstractBuilder<T extends TypedBoundingBoxOutputDescription, B extends AbstractBuilder<T, B>>
            extends BoundingBoxOutputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedBoundingBoxOutputDescription.Builder<T, B> {
        private Class<? extends BoundingBoxData<?>> type;

        @Override
        @SuppressWarnings("unchecked")
        public B withType(Class<? extends BoundingBoxData<?>> type) {
            this.type = Objects.requireNonNull(type);
            return (B) this;
        }

        public Class<? extends BoundingBoxData<?>> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedBoundingBoxOutputDescription, Builder> {
        @Override
        public TypedBoundingBoxOutputDescription build() {
            return new TypedBoundingBoxOutputDescriptionImpl(this);
        }

    }

}
