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

import java.util.Objects;

/**
 * OutputDescription
 */
@Validated
public class OutputDescription extends DataDescriptionType {
    @JsonProperty("output")
    private Object output;

    public OutputDescription output(Object output) {
        this.output = output;
        return this;
    }

    /**
     * Get output
     *
     * @return output
     **/
    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutputDescription outputDescription = (OutputDescription) o;
        return Objects.equals(this.output, outputDescription.output) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("OutputDescription{%s, output: %s}", super.toString(), output);
    }

}
