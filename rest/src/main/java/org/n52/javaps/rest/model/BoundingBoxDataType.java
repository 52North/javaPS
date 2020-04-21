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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BoundingBoxDataType
 */
@Validated
public class BoundingBoxDataType {
    @JsonProperty("supportedCRS")
    @Valid
    private List<SupportedCRS> supportedCRS = new ArrayList<>();

    public BoundingBoxDataType supportedCRS(List<SupportedCRS> supportedCRS) {
        this.supportedCRS = supportedCRS;
        return this;
    }

    public BoundingBoxDataType addSupportedCRSItem(SupportedCRS supportedCRSItem) {
        this.supportedCRS.add(supportedCRSItem);
        return this;
    }

    /**
     * Get supportedCRS
     *
     * @return supportedCRS
     **/
    @NotNull
    @Valid
    public List<SupportedCRS> getSupportedCRS() {
        return supportedCRS;
    }

    public void setSupportedCRS(List<SupportedCRS> supportedCRS) {
        this.supportedCRS = supportedCRS;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BoundingBoxDataType boundingBoxDataType = (BoundingBoxDataType) o;
        return Objects.equals(this.supportedCRS, boundingBoxDataType.supportedCRS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supportedCRS);
    }

    @Override
    public String toString() {
        return String.format("BoundingBoxDataType{supportedCRS: %s}", supportedCRS);
    }
}
