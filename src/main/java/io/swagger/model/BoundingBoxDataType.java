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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BoundingBoxDataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-28T09:55:34.783Z[GMT]")
public class BoundingBoxDataType {
    @JsonProperty("supportedCRS")
    @Valid
    private List<SupportedCRS> supportedCRS = new ArrayList<SupportedCRS>();

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
    @ApiModelProperty(required = true, value = "")
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
        StringBuilder sb = new StringBuilder();
        sb.append("class BoundingBoxDataType {\n");

        sb.append("    supportedCRS: ").append(toIndentedString(supportedCRS)).append("\n");
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
