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
package org.n52.javaps.ogc.wps.description;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.io.data.IData;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsKeyword;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.iceland.ogc.ows.OwsMetadata;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements LiteralOutputDescription {

    private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralOutputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getBindingClass(),
             builder.getDefaultLiteralDataDomain(),
             builder.getSupportedLiteralDataDomains());
    }

    public LiteralOutputDescriptionImpl(OwsCode id, OwsLanguageString title,
                                        OwsLanguageString abstrakt,
                                        Set<OwsKeyword> keywords,
                                        Set<OwsMetadata> metadata,
                                        Class<? extends IData> bindingClass,
                                        LiteralDataDomain defaultLiteralDataDomain,
                                        Set<LiteralDataDomain> supportedLiteralDataDomains) {
        super(id, title, abstrakt, keywords, metadata, bindingClass);
        this.defaultLiteralDataDomain = Objects
                .requireNonNull(defaultLiteralDataDomain, "defaultLiteralDataDomain");
        this.supportedLiteralDataDomains = supportedLiteralDataDomains == null
                                                   ? Collections.emptySet()
                                                   : supportedLiteralDataDomains;
    }

    @Override
    public LiteralOutputDescriptionImpl asLiteral() {
        return this;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public <T> T visit(ReturningVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(
            ThrowingReturningVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(this.supportedLiteralDataDomains);
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    public static LiteralOutputDescription.Builder<?, ?> builder() {
        return new BuilderImpl();
    }

    protected static abstract class AbstractBuilder<T extends LiteralOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessOutputDescription.AbstractBuilder<T, B>
            implements LiteralOutputDescription.Builder<T, B> {

        private LiteralDataDomain defaultLiteralDataDomain;
        private final ImmutableSet.Builder<LiteralDataDomain> supportedLiteralDataDomains
                = ImmutableSet.builder();

        LiteralDataDomain getDefaultLiteralDataDomain() {
            return this.defaultLiteralDataDomain;
        }

        Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
            return this.supportedLiteralDataDomains.build();
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withDefaultLiteralDataDomain(
                LiteralDataDomain literalDataDomain) {
            this.defaultLiteralDataDomain = Objects
                    .requireNonNull(literalDataDomain);
            return (B) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withSupportedLiteralDataDomain(LiteralDataDomain domain) {
            if (domain != null) {
                this.supportedLiteralDataDomains.add(domain);
            }
            return (B) this;
        }

    }

    private static class BuilderImpl extends AbstractBuilder<LiteralOutputDescription, BuilderImpl> {
        @Override
        public LiteralOutputDescriptionImpl build() {
            return new LiteralOutputDescriptionImpl(this);
        }
    }

}
