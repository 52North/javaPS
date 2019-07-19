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

import java.math.BigInteger;
import java.util.Objects;

/**
 * FormatDescription
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-28T09:55:34.783Z[GMT]")
public class FormatDescription extends Format {
    @JsonProperty("maximumMegabytes")
    private BigInteger maximumMegabytes = null;

    @JsonProperty("default")
    private Boolean _default = false;

    public FormatDescription maximumMegabytes(BigInteger maximumMegabytes) {
        this.maximumMegabytes = maximumMegabytes;
        return this;
    }

    /**
     * Get maximumMegabytes
     *
     * @return maximumMegabytes
     **/
    @ApiModelProperty(value = "")

    public BigInteger getMaximumMegabytes() {
        return maximumMegabytes;
    }

    public void setMaximumMegabytes(BigInteger maximumMegabytes) {
        this.maximumMegabytes = maximumMegabytes;
    }

    public FormatDescription _default(Boolean _default) {
        this._default = _default;
        return this;
    }

    /**
     * Get _default
     *
     * @return _default
     **/
    @ApiModelProperty(value = "")

    public Boolean isDefault() {
        return _default;
    }

    public void setDefault(Boolean _default) {
        this._default = _default;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormatDescription formatDescription = (FormatDescription) o;
        return Objects.equals(this.maximumMegabytes, formatDescription.maximumMegabytes) &&
                Objects.equals(this._default, formatDescription._default) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumMegabytes, _default, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FormatDescription {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    maximumMegabytes: ").append(toIndentedString(maximumMegabytes)).append("\n");
        sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
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
