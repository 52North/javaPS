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
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "")

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
        return Objects.equals(this.id, output.id) &&
               Objects.equals(this.transmissionMode, output.transmissionMode) &&
               super.equals(o);
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
