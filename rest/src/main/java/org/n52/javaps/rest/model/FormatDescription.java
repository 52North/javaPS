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

import java.math.BigInteger;
import java.util.Objects;

/**
 * FormatDescription
 */
@Validated
public class FormatDescription extends Format {
    @JsonProperty("maximumMegabytes")
    private BigInteger maximumMegabytes;

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
        return Objects.equals(this.maximumMegabytes, formatDescription.maximumMegabytes)
                && Objects.equals(this._default, formatDescription._default) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumMegabytes, _default, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("FormatDescription {%s, maximumMegabytes: %s, default: %s}", super.toString(),
                maximumMegabytes, _default);
    }

}