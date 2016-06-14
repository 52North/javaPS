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
package org.n52.javaps.description.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.math.BigInteger;

import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessInputDescriptionBuilder;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public abstract class AbstractProcessInputDescriptionBuilder<T extends ProcessInputDescription, B extends ProcessInputDescriptionBuilder<T, B>>
        extends AbstractDataDescriptionBuilder<T, B> implements ProcessInputDescriptionBuilder<T, B> {

    private BigInteger minimalOccurence = BigInteger.ONE;
    private BigInteger maximalOccurence = BigInteger.ONE;

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withMinimalOccurence(BigInteger min) {
        if (min != null) {
            checkArgument(min.compareTo(BigInteger.ZERO) > 0, "minimalOccurence");
            this.minimalOccurence = min;
        } else {
            this.minimalOccurence = BigInteger.ONE;
        }
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withMaximalOccurence(BigInteger max) {
        if (max != null) {
            checkArgument(max.compareTo(BigInteger.ZERO) > 0, "maximalOccurence");
            this.maximalOccurence = max;
        } else {
            this.maximalOccurence = BigInteger.ONE;
        }
        return (B) this;
    }

    BigInteger getMinimalOccurence() {
        return minimalOccurence;
    }

    BigInteger getMaximalOccurence() {
        return maximalOccurence;
    }

}
