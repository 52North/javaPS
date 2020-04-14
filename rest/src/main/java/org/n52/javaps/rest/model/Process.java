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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Process
 */
@Validated
public class Process extends ProcessSummary {
    @JsonProperty("inputs")
    @Valid
    private List<InputDescription> inputs;

    @JsonProperty("outputs")
    @Valid
    private List<OutputDescription> outputs;

    @JsonProperty("links")
    @Valid
    private List<Link> links;

    public Process inputs(List<InputDescription> inputs) {
        this.inputs = inputs;
        return this;
    }

    public Process outputs(List<OutputDescription> outputs) {
        this.outputs = outputs;
        return this;
    }

    public Process links(List<Link> links) {
        this.links = links;
        return this;
    }

    /**
     * Get inputs
     *
     * @return inputs
     **/
    @Valid
    public List<InputDescription> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputDescription> inputs) {
        this.inputs = inputs;
    }

    public Process addInputsItem(InputDescription inputsItem) {
        if (this.inputs == null) {
            this.inputs = new ArrayList<>();
        }
        this.inputs.add(inputsItem);
        return this;
    }

    /**
     * Get outputs
     *
     * @return outputs
     **/
    @Valid
    public List<OutputDescription> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputDescription> outputs) {
        this.outputs = outputs;
    }

    public Process addOutputsItem(OutputDescription outputsItem) {
        if (this.outputs == null) {
            this.outputs = new ArrayList<>();
        }
        this.outputs.add(outputsItem);
        return this;
    }

    /**
     * Get links
     *
     * @return links
     **/
    @Valid
    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Process addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<Link>();
        }
        this.links.add(linksItem);
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Process process = (Process) o;
        return Objects.equals(this.inputs, process.inputs) && Objects.equals(this.outputs, process.outputs)
                && Objects.equals(this.links, process.links) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, outputs, links, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Process{%s, inputs: %s, outputs: %s, links: %s}", super.toString(), inputs, outputs,
                links);
    }
}
