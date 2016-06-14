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
package org.n52.iceland.ogc.ows;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsCRS {
    private final String value;

    public OwsCRS(String value) {
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    public String getValue() {
        return value;
    }

    public static OwsCRS of(String value) {
        return new OwsCRS(value);
    }

    public static Set<OwsCRS> of(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptySet();
        } else {
            final Set<OwsCRS> supportedCRS = new HashSet<>(values.size());
            for (String value : values) {
                supportedCRS.add(of(value));
            }
            return supportedCRS;
        }
    }

}
