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
import java.util.Objects;

/**
 * ResultValueType
 */
@Validated
public class ResultValueType {
    @JsonProperty("inlineValue")
    private Object inlineValue;

    @JsonProperty("href")
    private String href;

    @JsonProperty("boundingBox")
    private BoundingBoxData boundingBox;

    public ResultValueType inlineValue(Object inlineValue) {
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

    public ResultValueType href(String href) {
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

    public ResultValueType boundingBox(BoundingBoxData boundingBox) {
        this.boundingBox = boundingBox;
        return this;
    }

    /**
     * Get boundingBox
     *
     * @return boundingBox
     **/
    @NotNull
    @Valid
    public BoundingBoxData getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBoxData boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultValueType resultValueType = (ResultValueType) o;
        return Objects.equals(this.inlineValue, resultValueType.inlineValue) &&
               Objects.equals(this.href, resultValueType.href) &&
               Objects.equals(this.boundingBox, resultValueType.boundingBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlineValue, href, boundingBox);
    }

    @Override
    public String toString() {

        return String.format("ResultValueType{inlineValue: %s, href: %s, boundingBox: %s}",
                             inlineValue, href, boundingBox);
    }
}

