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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ComplexDataType
 */
@Validated
public class ComplexDataType {
    @JsonProperty("formats")
    @Valid
    private List<FormatDescription> formats = new ArrayList<>();

    public ComplexDataType formats(List<FormatDescription> formats) {
        this.formats = formats;
        return this;
    }

    public ComplexDataType addFormatsItem(FormatDescription formatsItem) {
        this.formats.add(formatsItem);
        return this;
    }

    /**
     * Get formats
     *
     * @return formats
     **/
    @NotNull
    @Valid
    public List<FormatDescription> getFormats() {
        return formats;
    }

    public void setFormats(List<FormatDescription> formats) {
        this.formats = formats;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComplexDataType complexDataType = (ComplexDataType) o;
        return Objects.equals(this.formats, complexDataType.formats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formats);
    }

    @Override
    public String toString() {
        return String.format("ComplexDataType {formats: %s}", formats);
    }
}
