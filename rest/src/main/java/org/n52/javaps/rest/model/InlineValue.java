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
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import java.util.Objects;

/**
 * InlineValue
 */
@Validated
public class InlineValue implements ValueType {
    @JsonProperty("inlineValue")
    private Object inlineValue;

    public InlineValue inlineValue(Object value) {
        this.inlineValue = value;
        return this;
    }

    /**
     * Get inlineValue
     *
     * @return inlineValue
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Object getInlineValue() {
        return inlineValue;
    }

    public void setInlineValue(Object inlineValue) {
        this.inlineValue = inlineValue;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InlineValue other = (InlineValue) o;
        return Objects.equals(this.inlineValue, other.inlineValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlineValue);
    }

    @Override
    public String toString() {
        return String.format("InlineValue{inlineValue: %s}", inlineValue);
    }
}
