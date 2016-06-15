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
