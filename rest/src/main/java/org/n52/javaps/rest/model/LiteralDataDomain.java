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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralDataDomain
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class LiteralDataDomain {
    @JsonProperty("valueDefinition")
    private Object valueDefinition;

    @JsonProperty("defaultValue")
    private String defaultValue;

    @JsonProperty("dataType")
    private LiteralDataDomainDataType dataType;

    @JsonProperty("uom")
    private LiteralDataDomainDataType uom;

    public LiteralDataDomain valueDefinition(Object valueDefinition) {
        this.valueDefinition = valueDefinition;
        return this;
    }

    /**
     * Get valueDefinition
     *
     * @return valueDefinition
     **/
    public Object getValueDefinition() {
        return valueDefinition;
    }

    public void setValueDefinition(Object valueDefinition) {
        this.valueDefinition = valueDefinition;
    }

    public LiteralDataDomain defaultValue(String defaultValue) {
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

    public LiteralDataDomain dataType(LiteralDataDomainDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    /**
     * Get dataType
     *
     * @return dataType
     **/
    @Valid
    public LiteralDataDomainDataType getDataType() {
        return dataType;
    }

    public void setDataType(LiteralDataDomainDataType dataType) {
        this.dataType = dataType;
    }

    public LiteralDataDomain uom(LiteralDataDomainDataType uom) {
        this.uom = uom;
        return this;
    }

    /**
     * Get uom
     *
     * @return uom
     **/
    @Valid
    public LiteralDataDomainDataType getUom() {
        return uom;
    }

    public void setUom(LiteralDataDomainDataType uom) {
        this.uom = uom;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralDataDomain literalDataDomain = (LiteralDataDomain) o;
        return Objects.equals(this.valueDefinition, literalDataDomain.valueDefinition)
                && Objects.equals(this.defaultValue, literalDataDomain.defaultValue)
                && Objects.equals(this.dataType, literalDataDomain.dataType)
                && Objects.equals(this.uom, literalDataDomain.uom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueDefinition, defaultValue, dataType, uom);
    }

    @Override
    public String toString() {
        return String.format("LiteralDataDomain{valueDefinition: %s, defaultValue: %s, dataType: %s, uom: %s}",
                valueDefinition, defaultValue, dataType, uom);
    }

}
