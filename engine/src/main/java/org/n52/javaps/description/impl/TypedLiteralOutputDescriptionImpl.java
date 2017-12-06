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
package org.n52.javaps.description.impl;

import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.impl.LiteralOutputDescriptionImpl;
import org.n52.javaps.description.TypedLiteralOutputDescription;
import org.n52.javaps.io.literal.LiteralType;

public class TypedLiteralOutputDescriptionImpl
        extends LiteralOutputDescriptionImpl
        implements TypedLiteralOutputDescription {
    private final LiteralType<?> type;

    public TypedLiteralOutputDescriptionImpl(OwsCode id,
                                             OwsLanguageString title,
                                             OwsLanguageString abstrakt,
                                             Set<OwsKeyword> keywords,
                                             Set<OwsMetadata> metadata,
                                             LiteralDataDomain defaultLiteralDataDomain,
                                             Set<LiteralDataDomain> supportedLiteralDataDomain,
                                             LiteralType<?> type) {
        super(id, title, abstrakt, keywords, metadata, defaultLiteralDataDomain, supportedLiteralDataDomain);
        this.type = Objects.requireNonNull(type, "type");
    }

    protected TypedLiteralOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getDefaultLiteralDataDomain(),
             builder.getSupportedLiteralDataDomains(),
             builder.getType());
    }

    @Override
    public LiteralType<?> getType() {
        return this.type;
    }

    public static abstract class AbstractBuilder<T extends TypedLiteralOutputDescription, B extends AbstractBuilder<T, B>>
            extends LiteralOutputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedLiteralOutputDescription.Builder<T, B> {
        private LiteralType<?> type;

        @Override
        @SuppressWarnings("unchecked")
        public B withType(LiteralType<?> type) {
            this.type = Objects.requireNonNull(type);
            return (B) this;
        }

        public LiteralType<?> getType() {
            return type;
        }

    }

    public static class Builder extends AbstractBuilder<TypedLiteralOutputDescription, Builder> {
        @Override
        public TypedLiteralOutputDescription build() {
            return new TypedLiteralOutputDescriptionImpl(this);
        }

    }

}
