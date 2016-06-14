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
package org.n52.javaps.description;

import java.util.Arrays;

import org.n52.iceland.ogc.ows.OwsCRS;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface BoundingBoxDescriptionBuilder<T extends BoundingBoxDescription, B extends BoundingBoxDescriptionBuilder<T, B>> {

    B withDefaultCRS(OwsCRS defaultCRS);

    default B withDefaultCRS(String defaultCRS) {
        return withDefaultCRS(defaultCRS == null ? null : new OwsCRS(defaultCRS));
    }

    @SuppressWarnings("unchecked")
    default B withSupportedCRS(Iterable<OwsCRS> crss) {
        for (OwsCRS crs : crss) {
            withSupportedCRS(crs);
        }
        return (B) this;
    }

    default B withSupportedCRS(OwsCRS... crss) {
        return withSupportedCRS(Arrays.asList(crss));
    }

    default B withSupportedCRS(String uom) {
        return withSupportedCRS(uom == null ? null : new OwsCRS(uom));
    }

    B withSupportedCRS(OwsCRS uom);

}
