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
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCRS;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescriptionBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements BoundingBoxOutputDescription {

    private final Set<OwsCRS> supportedCRS;
    private final Optional<OwsCRS> defaultCRS;

    protected BoundingBoxOutputDescriptionImpl(
            AbstractBoundingBoxOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.supportedCRS = builder.getSupportedCRS();
        this.defaultCRS = Optional.ofNullable(builder.getDefaultCRS());
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public Optional<OwsCRS> getDefaultCRS() {
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

    public static BoundingBoxOutputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractBoundingBoxOutputDescriptionBuilder<BoundingBoxOutputDescriptionImpl, BuilderImpl> {

        @Override
        public BoundingBoxOutputDescriptionImpl build() {
            return new BoundingBoxOutputDescriptionImpl(this);
        }
    }

}
