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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsAllowedValues implements Iterable<OwsValueRestriction> {
    private static final OwsAllowedValues ANY = new OwsAllowedValues();
    private final Set<OwsValueRestriction> restrictions = new HashSet<>();

    public OwsAllowedValues(Iterable<OwsValueRestriction> restrictions) {
        if (restrictions!= null) {
            for (OwsValueRestriction restriction : restrictions) {
                this.restrictions.add(Objects.requireNonNull(restriction));
            }
        }
    }

    public OwsAllowedValues(Stream<OwsValueRestriction> restrictions) {
        if (restrictions != null) {
            restrictions.forEach(x -> this.restrictions.add(Objects.requireNonNull(x)));
        }
    }

    public OwsAllowedValues() {
    }

    public void add(OwsValueRestriction restriction) {
        this.restrictions.add(Objects.requireNonNull(restriction));
    }

    @Override
    public Iterator<OwsValueRestriction> iterator() {
        return this.restrictions.iterator();
    }

    public Stream<OwsValueRestriction> stream() {
        return this.restrictions.stream();
    }

    public boolean isAny() {
        return this.restrictions.isEmpty();
    }

    public static OwsAllowedValues any() {
        return ANY;
    }
}
