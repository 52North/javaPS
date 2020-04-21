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

import java.util.Objects;

/**
 * SupportedCRS
 */
@Validated
public class SupportedCRS {
    @JsonProperty("crs")
    private String crs;

    @JsonProperty("default")
    private Boolean _default = false;

    public SupportedCRS crs(String crs) {
        this.crs = crs;
        return this;
    }

    /**
     * Get crs
     *
     * @return crs
     **/
    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public SupportedCRS _default(Boolean _default) {
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
        SupportedCRS supportedCRS = (SupportedCRS) o;
        return Objects.equals(this.crs, supportedCRS.crs) && Objects.equals(this._default, supportedCRS._default);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crs, _default);
    }

    @Override
    public String toString() {
        return String.format("SupportedCRS{crs: %s, default: %s}", crs, _default);
    }
}
