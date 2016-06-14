/*
 * Copyright (C) 2013-2015 Christian Autermann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.n52.javaps.description.impl;


import java.math.BigInteger;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.ComplexInputDescriptionBuilder;
import org.n52.javaps.description.Format;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ComplexInputDescriptionImpl
        extends AbstractProcessInputDescription
        implements ComplexInputDescription {

    private final Set<Format> supportedFormats;
    private final Format defaultFormat;
    private final Optional<BigInteger> maximumMegabytes;

    protected ComplexInputDescriptionImpl(
            AbstractComplexInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.defaultFormat = Objects.requireNonNull(builder.getDefaultFormat());
        this.supportedFormats = builder.getSupportedFormats();
        this.maximumMegabytes = Optional.ofNullable(builder
                .getMaximumMegabytes());
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

    public static ComplexInputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractComplexInputDescriptionBuilder<ComplexInputDescriptionImpl, BuilderImpl> {
        @Override
        public ComplexInputDescriptionImpl build() {
            return new ComplexInputDescriptionImpl(this);
        }
    }

}
