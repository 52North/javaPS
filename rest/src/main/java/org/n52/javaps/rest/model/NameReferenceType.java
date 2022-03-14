/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * NameReferenceType
 */
@Validated
@javax.annotation.Generated(
        value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
        date = "2020-04-01T13:54:06.047Z[GMT]")
public class NameReferenceType {
    @JsonProperty("name")
    private String name;

    @JsonProperty("reference")
    private String reference;

    public NameReferenceType name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NameReferenceType reference(String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * Get reference
     *
     * @return reference
     **/

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NameReferenceType nameReferenceType = (NameReferenceType) o;
        return Objects.equals(this.name, nameReferenceType.name)
                && Objects.equals(this.reference, nameReferenceType.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, reference);
    }

    @Override
    public String toString() {
        return String.format("NameReferenceType{name: %s, reference: %s}", name, reference);
    }
}
