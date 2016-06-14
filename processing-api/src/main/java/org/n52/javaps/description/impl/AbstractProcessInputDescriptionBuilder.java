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
        extends AbstractDescriptionBuilder<T, B> implements ProcessInputDescriptionBuilder<T, B> {

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
