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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LiteralDataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-28T09:55:34.783Z[GMT]")
public class LiteralDataType {
    @JsonProperty("literalDataDomains")
    @Valid
    private List<LiteralDataDomain> literalDataDomains = null;

    public LiteralDataType literalDataDomains(List<LiteralDataDomain> literalDataDomains) {
        this.literalDataDomains = literalDataDomains;
        return this;
    }

    public LiteralDataType addLiteralDataDomainsItem(LiteralDataDomain literalDataDomainsItem) {
        if (this.literalDataDomains == null) {
            this.literalDataDomains = new ArrayList<LiteralDataDomain>();
        }
        this.literalDataDomains.add(literalDataDomainsItem);
        return this;
    }

    /**
     * Get literalDataDomains
     *
     * @return literalDataDomains
     **/
    @ApiModelProperty(value = "")
    @Valid
    public List<LiteralDataDomain> getLiteralDataDomains() {
        return literalDataDomains;
    }

    public void setLiteralDataDomains(List<LiteralDataDomain> literalDataDomains) {
        this.literalDataDomains = literalDataDomains;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralDataType literalDataType = (LiteralDataType) o;
        return Objects.equals(this.literalDataDomains, literalDataType.literalDataDomains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literalDataDomains);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LiteralDataType {\n");

        sb.append("    literalDataDomains: ").append(toIndentedString(literalDataDomains)).append("\n");
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
