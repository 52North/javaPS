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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public interface ComplexDescriptionBuilder<T extends ComplexDescription, B extends ComplexDescriptionBuilder<T, B>> {

    B withDefaultFormat(Format format);

    B withSupportedFormat(Format format);

    @SuppressWarnings("unchecked")
    default B withSupportedFormat(Iterable<Format> formats){
        if (formats != null) {
            for (Format format : formats) {
                withSupportedFormat(format);
            }
        }
        return (B) this;
    }

    default B withSupportedFormat(Format... formats){
        return withSupportedFormat(Arrays.asList(formats));
    }

}
