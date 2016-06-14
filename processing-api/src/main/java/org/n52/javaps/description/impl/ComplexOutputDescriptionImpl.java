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


import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.ComplexOutputDescriptionBuilder;
import org.n52.javaps.description.Format;

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

    protected ComplexOutputDescriptionImpl(AbstractComplexOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.defaultFormat = Objects.requireNonNull(builder.getDefaultFormat());
        this.supportedFormats = builder.getSupportedFormats();
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
    public ComplexOutputDescriptionImpl asComplex() {
        return this;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public <T> T visit(ReturningVisitor<T> visitor) {
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
