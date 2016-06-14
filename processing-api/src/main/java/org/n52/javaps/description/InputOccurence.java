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
package org.n52.javaps.description;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class InputOccurence {

    private final BigInteger min;
    private final BigInteger max;

    public InputOccurence(BigInteger min, BigInteger max) {
        this.min = min == null ? BigInteger.ONE : min;
        this.max = max == null ? BigInteger.ONE : max;
        checkArgument(this.min.compareTo(BigInteger.ZERO) >= 0, "minimum < 0");
        checkArgument(this.max.compareTo(BigInteger.ZERO) > 0, "maximum <= 0");
        checkArgument(this.min.compareTo(this.max) <= 0, "minimum > maximum");
    }

    public BigInteger getMin() {
        return this.min;
    }

    public BigInteger getMax() {
        return this.max;
    }

    public boolean isRequired() {
        return this.min.compareTo(BigInteger.ZERO) > 0;
    }

    public boolean isMultiple() {
        return this.max.compareTo(BigInteger.ONE) > 0;
    }

    public boolean isInBounds(BigInteger occurence) {
        return this.min.compareTo(occurence) >= 0 &&
               this.max.compareTo(occurence) <= 0;
    }

}
