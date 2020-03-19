/*
 * Copyright (C) 2020 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import java.math.BigInteger;
import java.util.Objects;

/**
 * InputDescription
 */
@Validated
public class InputDescription extends DataDescriptionType {
    @JsonProperty("input")
    private Object input;

    @JsonProperty("minOccurs")
    private BigInteger minOccurs;

    @JsonProperty("maxOccurs")
    private BigInteger maxOccurs;

    public InputDescription input(Object input) {
        this.input = input;
        return this;
    }

    /**
     * Get input
     *
     * @return input
     **/
    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public InputDescription minOccurs(BigInteger minOccurs) {
        this.minOccurs = minOccurs;
        return this;
    }

    /**
     * Get minOccurs
     *
     * @return minOccurs
     **/
    public BigInteger getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(BigInteger minOccurs) {
        this.minOccurs = minOccurs;
    }

    public InputDescription maxOccurs(BigInteger maxOccurs) {
        this.maxOccurs = maxOccurs;
        return this;
    }

    /**
     * Get maxOccurs
     *
     * @return maxOccurs
     **/
    public BigInteger getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(BigInteger maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputDescription inputDescription = (InputDescription) o;
        return Objects.equals(this.input, inputDescription.input) &&
               Objects.equals(this.minOccurs, inputDescription.minOccurs) &&
               Objects.equals(this.maxOccurs, inputDescription.maxOccurs) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, minOccurs, maxOccurs, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("InputDescription{%s, input: %s, minOccurs: %s, maxOccurs: %s}",
                             super.toString(), input, minOccurs, maxOccurs);
    }
}
