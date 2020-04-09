/*
 * Copyright (C) 2020 by 52 North Initiative for Geospatial Open Source Software GmbH
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ReqClasses
 */
@Validated
public class ReqClasses {
    @JsonProperty("conformsTo")
    @Valid
    private List<String> conformsTo = new ArrayList<>();

    public ReqClasses conformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
        return this;
    }

    public ReqClasses addConformsToItem(String conformsToItem) {
        this.conformsTo.add(conformsToItem);
        return this;
    }

    /**
     * Get conformsTo
     *
     * @return conformsTo
     **/
    @NotNull
    public List<String> getConformsTo() {
        return conformsTo;
    }

    public void setConformsTo(List<String> conformsTo) {
        this.conformsTo = conformsTo;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReqClasses reqClasses = (ReqClasses) o;
        return Objects.equals(this.conformsTo, reqClasses.conformsTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conformsTo);
    }

    @Override
    public String toString() {
        return String.format("ReqClasses{conformsTo: %s}", conformsTo);
    }
}
