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

import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCRS;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescriptionBuilder;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractBoundingBoxOutputDescriptionBuilder<T extends BoundingBoxOutputDescription, B extends AbstractBoundingBoxOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B> implements BoundingBoxOutputDescriptionBuilder<T, B> {

    private OwsCRS defaultCRS;
    private final ImmutableSet.Builder<OwsCRS> supportedCRS = ImmutableSet.builder();

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
