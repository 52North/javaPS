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
import org.n52.iceland.ogc.ows.OwsCRS;
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
public class BoundingBoxOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements BoundingBoxOutputDescription {

    private final OwsCRS defaultCRS;
    private final Set<OwsCRS> supportedCRS;

    protected BoundingBoxOutputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getBindingClass(),
             builder.getDefaultCRS(),
             builder.getSupportedCRS());
    }

    public BoundingBoxOutputDescriptionImpl(OwsCode id,
                                            OwsLanguageString title,
                                            OwsLanguageString abstrakt,
                                            Set<OwsKeyword> keywords,
                                            Set<OwsMetadata> metadata,
                                            Class<? extends IData> bindingClass,
                                            OwsCRS defaultCRS,
                                            Set<OwsCRS> supportedCRS) {
        super(id, title, abstrakt, keywords, metadata, bindingClass);
        this.defaultCRS = Objects.requireNonNull(defaultCRS, "defaultCRS");
        this.supportedCRS = supportedCRS == null ? Collections.emptySet()
                                    : supportedCRS;
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public OwsCRS getDefaultCRS() {
        return this.defaultCRS;
    }

    @Override
    public BoundingBoxOutputDescriptionImpl asBoundingBox() {
        return this;
    }

    @Override
    public boolean isBoundingBox() {
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

    public static BoundingBoxOutputDescription.Builder<?, ?> builder() {
        return new BuilderImpl();
    }

    protected static abstract class AbstractBuilder<T extends BoundingBoxOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessOutputDescription.AbstractBuilder<T, B>
            implements BoundingBoxOutputDescription.Builder<T, B> {

        private OwsCRS defaultCRS;
        private final ImmutableSet.Builder<OwsCRS> supportedCRS = ImmutableSet
                .builder();

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withDefaultCRS(OwsCRS defaultCRS) {
            this.defaultCRS = defaultCRS;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withSupportedCRS(OwsCRS uom) {
            if (uom != null) {
                this.supportedCRS.add(uom);
            }
            return (B) this;
        }

        OwsCRS getDefaultCRS() {
            return defaultCRS;
        }

        Set<OwsCRS> getSupportedCRS() {
            return supportedCRS.build();
        }

    }

    private static class BuilderImpl extends AbstractBuilder<BoundingBoxOutputDescriptionImpl, BuilderImpl> {

        @Override
        public BoundingBoxOutputDescriptionImpl build() {
            return new BoundingBoxOutputDescriptionImpl(this);
        }
    }

}
