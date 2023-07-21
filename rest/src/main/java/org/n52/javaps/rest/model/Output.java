/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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
import java.util.Objects;

/**
 * Output
 */
@Validated
public class Output extends DataType {
    @JsonProperty("id")
    private String id;

    @JsonProperty("transmissionMode")
    private TransmissionMode transmissionMode;

    public Output id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Output transmissionMode(TransmissionMode transmissionMode) {
        this.transmissionMode = transmissionMode;
        return this;
    }

    /**
     * Get transmissionMode
     *
     * @return transmissionMode
     **/

    @Valid
    public TransmissionMode getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(TransmissionMode transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Output output = (Output) o;
        return Objects.equals(this.id, output.id) && Objects.equals(this.transmissionMode, output.transmissionMode)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transmissionMode, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Output {%s, id: %s, transmissionMode: %s}", super.toString(), id, transmissionMode);
    }
}
