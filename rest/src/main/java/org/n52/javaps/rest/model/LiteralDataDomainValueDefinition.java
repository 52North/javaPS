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

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralDataDomainValueDefinition
 */
@Validated
public class LiteralDataDomainValueDefinition {
    @JsonProperty("defaultValue")
    private String defaultValue;

    @JsonProperty("allowedValues")
    private AllowedValues allowedValues;

    public LiteralDataDomainValueDefinition defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Get defaultValue
     *
     * @return defaultValue
     **/
    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public LiteralDataDomainValueDefinition allowedValues(AllowedValues allowedValues) {
        this.allowedValues = allowedValues;
        return this;
    }

    /**
     * Get allowedValues
     *
     * @return allowedValues
     **/
    @Valid
    public AllowedValues getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(AllowedValues allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralDataDomainValueDefinition literalDataDomainValueDefinition = (LiteralDataDomainValueDefinition) o;
        return Objects.equals(this.defaultValue, literalDataDomainValueDefinition.defaultValue)
                && Objects.equals(this.allowedValues, literalDataDomainValueDefinition.allowedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, allowedValues);
    }

    @Override
    public String toString() {
        return String.format("LiteralDataDomainValueDefinition{defaultValue: %s, allowedValues: %s}", defaultValue,
                allowedValues);
    }

}
