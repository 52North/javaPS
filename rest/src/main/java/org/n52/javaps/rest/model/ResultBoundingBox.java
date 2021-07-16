/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
import java.util.Objects;

/**
 * ResultBoundingBox
 */
@Validated
public class ResultBoundingBox {
    @JsonProperty("boundingBox")
    private BoundingBoxData boundingBox;

    public ResultBoundingBox boundingBox(BoundingBoxData boundingBox) {
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
        ResultBoundingBox resultBoundingBox = (ResultBoundingBox) o;
        return Objects.equals(this.boundingBox, resultBoundingBox.boundingBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boundingBox);
    }

    @Override
    public String toString() {
        return String.format("ResultBoundingBox{boundingBox: %s}", boundingBox);
    }

}
