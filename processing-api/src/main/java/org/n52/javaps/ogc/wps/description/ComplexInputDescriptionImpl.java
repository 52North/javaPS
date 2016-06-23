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

import java.math.BigInteger;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.javaps.io.data.IData;
import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;
import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.wps.InputOccurence;
import org.n52.javaps.ogc.wps.description.ComplexInputDescription;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ComplexInputDescriptionImpl
        extends AbstractProcessInputDescription
        implements ComplexInputDescription {

    private final Format defaultFormat;
    private final Set<Format> supportedFormats;
    private final Optional<BigInteger> maximumMegabytes;

    protected ComplexInputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getBindingClass(),
             new InputOccurence(builder.getMinimalOccurence(),
                                builder.getMaximalOccurence()),
             builder.getDefaultFormat(),
             builder.getSupportedFormats(),
             builder.getMaximumMegabytes());
    }

    public ComplexInputDescriptionImpl(OwsCode id,
                                       OwsLanguageString title,
                                       OwsLanguageString abstrakt,
                                       Set<OwsKeyword> keywords,
                                       Set<OwsMetadata> metadata,
                                       Class<? extends IData> bindingClass,
                                       InputOccurence occurence,
                                       Format defaultFormat,
                                       Set<Format> supportedFormats,
                                       BigInteger maximumMegabytes) {
        super(id, title, abstrakt, keywords, metadata, bindingClass, occurence);
        this.defaultFormat = Objects
                .requireNonNull(defaultFormat, "defaultFormat");
        this.supportedFormats = supportedFormats == null ? Collections
                .emptySet() : supportedFormats;
        this.maximumMegabytes = Optional.ofNullable(maximumMegabytes);
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(supportedFormats);
    }

    @Override
    public Format getDefaultFormat() {
        return defaultFormat;
    }

    @Override
    public ComplexInputDescriptionImpl asComplex() {
        return this;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public Optional<BigInteger> getMaximumMegabytes() {
        return this.maximumMegabytes;
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

    public static ComplexInputDescription.Builder<?, ?> builder() {
        return new BuilderImpl();
    }

    protected static abstract class AbstractBuilder<T extends ComplexInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements ComplexInputDescription.Builder<T, B> {

        private final ImmutableSet.Builder<Format> supportedFormats
                = ImmutableSet
                .builder();
        private Format defaultFormat;
        private BigInteger maximumMegabytes;

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withMaximumMegabytes(BigInteger maximumMegabytes) {
            Preconditions.checkArgument(maximumMegabytes == null ||
                                        maximumMegabytes
                                        .compareTo(BigInteger.ZERO) >
                                        0);
            this.maximumMegabytes = maximumMegabytes;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withDefaultFormat(Format format) {
            this.defaultFormat = Objects.requireNonNull(format);
            this.supportedFormats.add(format);
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withSupportedFormat(Format format) {
            if (format != null) {
                this.supportedFormats.add(format);
            }
            return (B) this;
        }

        Set<Format> getSupportedFormats() {
            return supportedFormats.build();
        }

        Format getDefaultFormat() {
            return defaultFormat;
        }

        BigInteger getMaximumMegabytes() {
            return maximumMegabytes;
        }

    }

    private static class BuilderImpl extends AbstractBuilder<ComplexInputDescriptionImpl, BuilderImpl> {
        @Override
        public ComplexInputDescriptionImpl build() {
            return new ComplexInputDescriptionImpl(this);
        }
    }

}
