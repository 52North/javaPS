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
package org.n52.javaps.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * LiteralData
 */
@Validated
public class LiteralData {
    @JsonProperty("value")
    private String value;

    @JsonProperty("dataType")
    private String dataType;

    @JsonProperty("uom")
    private String uom;

    public LiteralData value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     *
     * @return value
     **/
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LiteralData dataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    /**
     * Get dataType
     *
     * @return dataType
     **/
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public LiteralData uom(String uom) {
        this.uom = uom;
        return this;
    }

    /**
     * Get uom
     *
     * @return uom
     **/
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
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
        LiteralData literalData = (LiteralData) o;
        return Objects.equals(this.value, literalData.value) &&
               Objects.equals(this.dataType, literalData.dataType) &&
               Objects.equals(this.uom, literalData.uom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dataType, uom);
    }

    @Override
    public String toString() {
        return String.format("LiteralData{value: %s, dataType: %s, uom: %s}", value, dataType, uom);
    }
}
