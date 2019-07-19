/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
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
package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * LiteralDataDomainValueDefinition
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T12:20:58.856+01:00[Europe/Berlin]")

public class LiteralDataDomainValueDefinition {
    @JsonProperty("defaultValue")
    private String defaultValue = null;

    @JsonProperty("allowedValues")
    private AllowedValues allowedValues = null;

    public LiteralDataDomainValueDefinition defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Get defaultValue
     *
     * @return defaultValue
     **/
    @ApiModelProperty(value = "")

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
    @ApiModelProperty(value = "")

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
        return Objects.equals(this.defaultValue, literalDataDomainValueDefinition.defaultValue) &&
                Objects.equals(this.allowedValues, literalDataDomainValueDefinition.allowedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, allowedValues);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LiteralDataDomainValueDefinition {\n");

        sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
        sb.append("    allowedValues: ").append(toIndentedString(allowedValues)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

