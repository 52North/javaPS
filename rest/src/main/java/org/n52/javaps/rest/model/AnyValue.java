/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * AnyValue
 */
@Validated
public class AnyValue {
    @JsonProperty("anyValue")
    private Boolean anyValue = true;

    public AnyValue anyValue(Boolean anyValue) {
        this.anyValue = anyValue;
        return this;
    }

    /**
     * Get anyValue
     *
     * @return anyValue
     **/

    public Boolean isAnyValue() {
        return anyValue;
    }

    public void setAnyValue(Boolean isAnyValue) {
        this.anyValue = isAnyValue;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnyValue other = (AnyValue) o;
        return Objects.equals(this.anyValue, other.anyValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anyValue);
    }

    @Override
    public String toString() {
        return "AnyValue {}";
    }

}