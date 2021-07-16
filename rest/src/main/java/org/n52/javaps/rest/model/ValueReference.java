/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
 * ValueReference
 */
@Validated
public class ValueReference {
    @JsonProperty("valueReference")
    private String valueReference;

    public ValueReference valueReference(String valueReference) {
        this.valueReference = valueReference;
        return this;
    }

    /**
     * Get valueReference
     *
     * @return valueReference
     **/
    public String getValueReference() {
        return valueReference;
    }

    public void setValueReference(String reference) {
        this.valueReference = reference;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueReference other = (ValueReference) o;
        return Objects.equals(this.valueReference, other.valueReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueReference);
    }

    @Override
    public String toString() {
        return String.format("ValueReference{valueReference: %s}", valueReference);
    }
}
