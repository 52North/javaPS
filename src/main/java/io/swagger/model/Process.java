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
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.InputDescription;
import io.swagger.model.JobControlOptions;
import io.swagger.model.Link;
import io.swagger.model.OutputDescription;
import io.swagger.model.ProcessSummary;
import io.swagger.model.TransmissionMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Process
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-04-15T12:59:11.004Z[GMT]")
public class Process extends ProcessSummary  {
  @JsonProperty("inputs")
  @Valid
  private List<InputDescription> inputs = null;

  @JsonProperty("outputs")
  @Valid
  private List<OutputDescription> outputs = null;

  public Process inputs(List<InputDescription> inputs) {
    this.inputs = inputs;
    return this;
  }

  public Process addInputsItem(InputDescription inputsItem) {
    if (this.inputs == null) {
      this.inputs = new ArrayList<InputDescription>();
    }
    this.inputs.add(inputsItem);
    return this;
  }

  /**
   * Get inputs
   * @return inputs
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<InputDescription> getInputs() {
    return inputs;
  }

  public void setInputs(List<InputDescription> inputs) {
    this.inputs = inputs;
  }

  public Process outputs(List<OutputDescription> outputs) {
    this.outputs = outputs;
    return this;
  }

  public Process addOutputsItem(OutputDescription outputsItem) {
    if (this.outputs == null) {
      this.outputs = new ArrayList<OutputDescription>();
    }
    this.outputs.add(outputsItem);
    return this;
  }

  /**
   * Get outputs
   * @return outputs
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<OutputDescription> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<OutputDescription> outputs) {
    this.outputs = outputs;
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
    return Objects.equals(this.inputs, process.inputs) &&
        Objects.equals(this.outputs, process.outputs) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputs, outputs, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Process {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    inputs: ").append(toIndentedString(inputs)).append("\n");
    sb.append("    outputs: ").append(toIndentedString(outputs)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
