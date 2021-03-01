/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
/*
 * Copyright 2016-2021 52?North Initiative for Geospatial Open Source
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

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * InlineOrRefData
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class InlineOrRefData {
    @JsonProperty("dataType")
    private NameReferenceType dataType;

    @JsonProperty("uom")
    private NameReferenceType uom;

    @JsonProperty("format")
    private Format format;

    @JsonProperty("href")
    private String href;

    @JsonProperty("value")
    private Object value;

    public InlineOrRefData dataType(NameReferenceType dataType) {
        this.dataType = dataType;
        return this;
    }

    /**
     * Get dataType
     *
     * @return dataType
     **/

    @Valid
    public NameReferenceType getDataType() {
        return dataType;
    }

    public void setDataType(NameReferenceType dataType) {
        this.dataType = dataType;
    }

    public InlineOrRefData uom(NameReferenceType uom) {
        this.uom = uom;
        return this;
    }

    /**
     * Get uom
     *
     * @return uom
     **/

    @Valid
    public NameReferenceType getUom() {
        return uom;
    }

    public void setUom(NameReferenceType uom) {
        this.uom = uom;
    }

    public InlineOrRefData format(Format format) {
        this.format = format;
        return this;
    }

    /**
     * Get format
     *
     * @return format
     **/

    @Valid
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public InlineOrRefData href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Get href
     *
     * @return href
     **/

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public InlineOrRefData value(Object value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     *
     * @return value
     **/

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InlineOrRefData inlineOrRefData = (InlineOrRefData) o;
        return Objects.equals(this.dataType, inlineOrRefData.dataType) && Objects.equals(this.uom, inlineOrRefData.uom)
                && Objects.equals(this.format, inlineOrRefData.format)
                && Objects.equals(this.href, inlineOrRefData.href) && Objects.equals(this.value, inlineOrRefData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, uom, format, href, value);
    }

    // TODO adjust to other classes
    @Override
    public String toString() {
        return String.format("InlineOrRefData{dataType: %s, uom: %s, format: %s, href: %s, value: %s}", dataType, uom,
                format, href, value);
    }
}
