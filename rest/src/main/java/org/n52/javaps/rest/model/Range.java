/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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
package org.n52.javaps.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Range
 */
@Validated
public class Range {
    @JsonProperty("minimumValue")
    private String minimumValue;

    @JsonProperty("maximumValue")
    private String maximumValue;

    @JsonProperty("spacing")
    private String spacing;

    /**
     * Gets or Sets rangeClosure
     */
    public enum RangeClosureEnum {
        CLOSED("closed"),

        OPEN("open"),

        OPEN_CLOSED("open-closed"),

        CLOSED_OPEN("closed-open");

        private String value;

        RangeClosureEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RangeClosureEnum fromValue(String text) {
            for (RangeClosureEnum b : RangeClosureEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("rangeClosure")
    private RangeClosureEnum rangeClosure;

    public Range minimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
        return this;
    }

    /**
     * Get minimumValue
     *
     * @return minimumValue
     **/
    public String getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(String minimumValue) {
        this.minimumValue = minimumValue;
    }

    public Range maximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
        return this;
    }

    /**
     * Get maximumValue
     *
     * @return maximumValue
     **/
    public String getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(String maximumValue) {
        this.maximumValue = maximumValue;
    }

    public Range spacing(String spacing) {
        this.spacing = spacing;
        return this;
    }

    /**
     * Get spacing
     *
     * @return spacing
     **/
    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public Range rangeClosure(RangeClosureEnum rangeClosure) {
        this.rangeClosure = rangeClosure;
        return this;
    }

    /**
     * Get rangeClosure
     *
     * @return rangeClosure
     **/
    public RangeClosureEnum getRangeClosure() {
        return rangeClosure;
    }

    public void setRangeClosure(RangeClosureEnum rangeClosure) {
        this.rangeClosure = rangeClosure;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Range range = (Range) o;
        return Objects.equals(this.minimumValue, range.minimumValue)
                && Objects.equals(this.maximumValue, range.maximumValue) && Objects.equals(this.spacing, range.spacing)
                && Objects.equals(this.rangeClosure, range.rangeClosure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumValue, maximumValue, spacing, rangeClosure);
    }

    @Override
    public String toString() {
        return String.format("Range{minimumValue: %s, maximumValue: %s, spacing: %s, rangeClosure: %s}", minimumValue,
                maximumValue, spacing, rangeClosure);
    }
}
