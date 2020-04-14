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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Format
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class Format {
    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("schema")
    private String schema;

    @JsonProperty("encoding")
    private String encoding;

    public Format mimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    /**
     * Get mimeType
     *
     * @return mimeType
     **/
    @NotNull
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Format schema(String schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Get schema
     *
     * @return schema
     **/
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Format encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Get encoding
     *
     * @return encoding
     **/
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Format format = (Format) o;
        return Objects.equals(this.mimeType, format.mimeType) && Objects.equals(this.schema, format.schema)
                && Objects.equals(this.encoding, format.encoding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mimeType, schema, encoding);
    }

    @Override
    public String toString() {
        return String.format("Format{mimeType: %s, schema: %s, encoding: %s}", mimeType, schema, encoding);
    }
}
