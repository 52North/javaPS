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
package org.n52.javaps.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ValueType
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class ValueType {
    @JsonProperty("inlineValue")
    private Object inlineValue;

    @JsonProperty("href")
    private String href;

    public ValueType inlineValue(Object inlineValue) {
        this.inlineValue = inlineValue;
        return this;
    }

    /**
     * Get inlineValue
     *
     * @return inlineValue
     **/
    public Object getInlineValue() {
        return inlineValue;
    }

    public void setInlineValue(Object inlineValue) {
        this.inlineValue = inlineValue;
    }

    public ValueType href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Get href
     *
     * @return href
     **/
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueType valueType = (ValueType) o;
        return Objects.equals(this.inlineValue, valueType.inlineValue) &&
               Objects.equals(this.href, valueType.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlineValue, href);
    }

    @Override
    public String toString() {
        return String.format("ValueType{inlineValue: %s,href: %s}", inlineValue, href);
    }
}
