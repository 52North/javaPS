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
package org.n52.javaps.description.impl;


import java.math.BigInteger;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.ComplexOutputDescriptionBuilder;
import org.n52.javaps.description.Format;
import org.n52.javaps.description.ReturningProcessOutputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessOutputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ComplexOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements ComplexOutputDescription {

    private final Set<Format> supportedFormats;
    private final Format defaultFormat;
    private final Optional<BigInteger> maximumMegabytes;

    protected ComplexOutputDescriptionImpl(AbstractComplexOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.defaultFormat = Objects.requireNonNull(builder.getDefaultFormat());
        this.supportedFormats = builder.getSupportedFormats();
        this.maximumMegabytes = Optional.ofNullable(builder.getMaximumMegabytes());
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(this.supportedFormats);
    }

    @Override
    public Format getDefaultFormat() {
        return this.defaultFormat;
    }

    @Override
    public Optional<BigInteger> getMaximumMegabytes() {
        return this.maximumMegabytes;
    }

    @Override
    public ComplexOutputDescriptionImpl asComplex() {
        return this;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public <T> T visit(ReturningProcessOutputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningProcessOutputVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }


    public static ComplexOutputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractComplexOutputDescriptionBuilder<ComplexOutputDescriptionImpl, BuilderImpl> {

        @Override
        public ComplexOutputDescriptionImpl build() {
            return new ComplexOutputDescriptionImpl(this);
        }

    }

}
