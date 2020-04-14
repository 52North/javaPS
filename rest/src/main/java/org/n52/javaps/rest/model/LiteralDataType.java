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
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * LiteralDataType
 */
@Validated
public class LiteralDataType {
    @JsonProperty("literalDataDomains")
    @Valid
    private List<LiteralDataDomain> literalDataDomains = new ArrayList<LiteralDataDomain>();

    public LiteralDataType literalDataDomains(List<LiteralDataDomain> literalDataDomains) {
        this.literalDataDomains = literalDataDomains;
        return this;
    }

    public LiteralDataType addLiteralDataDomainsItem(LiteralDataDomain literalDataDomainsItem) {
        this.literalDataDomains.add(literalDataDomainsItem);
        return this;
    }

    /**
     * Get literalDataDomains
     *
     * @return literalDataDomains
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull
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
        return String.format("LiteralDataType{literalDataDomains: %s}", literalDataDomains);
    }
}
