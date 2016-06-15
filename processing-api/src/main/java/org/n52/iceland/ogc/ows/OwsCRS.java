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
