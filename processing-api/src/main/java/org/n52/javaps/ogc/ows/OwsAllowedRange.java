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
package org.n52.javaps.ogc.ows;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsAllowedRange implements OwsValueRestriction {
    public static final String CLOSED = "closed";
    public static final String CLOSED_OPEN = "closed-open";
    public static final String OPEN_CLOSED = "open-closed";
    public static final String OPEN = "open";
    private final Bound lowerBound;
    private final Bound upperBound;
    private final Optional<String> spacing;

    public OwsAllowedRange(String lowerBound, BoundType lowerBoundType,
                           String upperBound, BoundType upperBoundType) {
        this(lowerBound, lowerBoundType, upperBound, upperBoundType, null);
    }

    public OwsAllowedRange(String lowerBound, BoundType lowerBoundType,
                           String upperBound, BoundType upperBoundType, String spacing) {
        this.lowerBound = new Bound(lowerBoundType, lowerBound);
        this.upperBound = new Bound(upperBoundType, upperBound);
        this.spacing = Optional.ofNullable(Strings.emptyToNull(spacing));
    }


    public Optional<String> getLowerBound() {
        return this.lowerBound.getValue();
    }

    public Optional<String> getUpperBound() {
        return this.upperBound.getValue();
    }

    public BoundType getLowerBoundType() {
        return this.lowerBound.getType();
    }

    public BoundType getUpperBoundType() {
        return this.upperBound.getType();
    }

    public Optional<String> getSpacing() {
        return spacing;
    }

    public String getType() {
        if (getLowerBoundType() == BoundType.OPEN) {
            if (getUpperBoundType() == BoundType.OPEN) {
                return OPEN;
            } else {
                return OPEN_CLOSED;
            }
        } else {
            if (getUpperBoundType() == BoundType.OPEN) {
                return CLOSED_OPEN;
            } else {
                return CLOSED;
            }
        }
    }

    @Override
    public OwsAllowedRange asRange() {
        return this;
    }

    @Override
    public boolean isRange() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lowerBound, this.upperBound, this.spacing);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            OwsAllowedRange that = (OwsAllowedRange) obj;
            return Objects.equals(this.lowerBound, that.lowerBound) &&
                   Objects.equals(this.upperBound, that.upperBound) &&
                   Objects.equals(this.spacing, that.spacing);
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(this.lowerBound.asLower() + ", " +
                          this.upperBound.asUpper()).toString();
    }

    private static class Bound {
        private final BoundType type;
        private final Optional<String> value;

        Bound(BoundType type, String value) {
            this.type = Objects.requireNonNull(type);
            this.value = Optional.ofNullable(value);
        }

        BoundType getType() {
            return type;
        }

        Optional<String> getValue() {
            return value;
        }

        String asUpper() {
            return this.getType().asUpper() + getValue().orElse("\u221e");
        }

        String asLower() {
            return this.getType().asLower() + getValue().orElse("-\u221e");
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.type, this.value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj.getClass() == getClass()) {
                Bound that = (Bound) obj;
                return Objects.equals(this.type, that.type) &&
                       Objects.equals(this.value, that.value);
            }
            return false;
        }
    }

    public static enum BoundType {
        OPEN {
                    @Override
                    char asUpper() {
                        return ')';
                    }

                    @Override
                    char asLower() {
                        return '(';
                    }
                },
        CLOSED {
                    @Override
                    char asUpper() {
                        return ']';
                    }

                    @Override
                    char asLower() {
                        return '[';
                    }
                };

        abstract char asUpper();

        abstract char asLower();
    }

}
