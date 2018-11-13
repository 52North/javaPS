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
import io.swagger.model.DescriptionType;
import io.swagger.model.JobControlOptions;
import io.swagger.model.Metadata;
import io.swagger.model.OutputDescription;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T12:20:58.856+01:00[Europe/Berlin]")

public class Process extends DescriptionType  {
  @JsonProperty("inputs")
  @Valid
  private List<Object> inputs = null;

  @JsonProperty("outputs")
  @Valid
  private List<OutputDescription> outputs = null;

  @JsonProperty("version")
  private String version = null;

  @JsonProperty("jobControlOptions")
  @Valid
  private List<JobControlOptions> jobControlOptions = null;

  @JsonProperty("outputTransmission")
  @Valid
  private List<TransmissionMode> outputTransmission = null;

  @JsonProperty("executeEndpoint")
  private String executeEndpoint = null;

  public Process inputs(List<Object> inputs) {
    this.inputs = inputs;
    return this;
  }

  public Process addInputsItem(Object inputsItem) {
    if (this.inputs == null) {
      this.inputs = new ArrayList<Object>();
    }
    this.inputs.add(inputsItem);
    return this;
  }

  /**
   * Get inputs
   * @return inputs
  **/
  @ApiModelProperty(value = "")


  public List<Object> getInputs() {
    return inputs;
  }

  public void setInputs(List<Object> inputs) {
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

  public Process version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(value = "")


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Process jobControlOptions(List<JobControlOptions> jobControlOptions) {
    this.jobControlOptions = jobControlOptions;
    return this;
  }

  public Process addJobControlOptionsItem(JobControlOptions jobControlOptionsItem) {
    if (this.jobControlOptions == null) {
      this.jobControlOptions = new ArrayList<JobControlOptions>();
    }
    this.jobControlOptions.add(jobControlOptionsItem);
    return this;
  }

  /**
   * Get jobControlOptions
   * @return jobControlOptions
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<JobControlOptions> getJobControlOptions() {
    return jobControlOptions;
  }

  public void setJobControlOptions(List<JobControlOptions> jobControlOptions) {
    this.jobControlOptions = jobControlOptions;
  }

  public Process outputTransmission(List<TransmissionMode> outputTransmission) {
    this.outputTransmission = outputTransmission;
    return this;
  }

  public Process addOutputTransmissionItem(TransmissionMode outputTransmissionItem) {
    if (this.outputTransmission == null) {
      this.outputTransmission = new ArrayList<TransmissionMode>();
    }
    this.outputTransmission.add(outputTransmissionItem);
    return this;
  }

  /**
   * Get outputTransmission
   * @return outputTransmission
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<TransmissionMode> getOutputTransmission() {
    return outputTransmission;
  }

  public void setOutputTransmission(List<TransmissionMode> outputTransmission) {
    this.outputTransmission = outputTransmission;
  }

  public Process executeEndpoint(String executeEndpoint) {
    this.executeEndpoint = executeEndpoint;
    return this;
  }

  /**
   * Get executeEndpoint
   * @return executeEndpoint
  **/
  @ApiModelProperty(value = "")


  public String getExecuteEndpoint() {
    return executeEndpoint;
  }

  public void setExecuteEndpoint(String executeEndpoint) {
    this.executeEndpoint = executeEndpoint;
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
        Objects.equals(this.version, process.version) &&
        Objects.equals(this.jobControlOptions, process.jobControlOptions) &&
        Objects.equals(this.outputTransmission, process.outputTransmission) &&
        Objects.equals(this.executeEndpoint, process.executeEndpoint) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputs, outputs, version, jobControlOptions, outputTransmission, executeEndpoint, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Process {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    inputs: ").append(toIndentedString(inputs)).append("\n");
    sb.append("    outputs: ").append(toIndentedString(outputs)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    jobControlOptions: ").append(toIndentedString(jobControlOptions)).append("\n");
    sb.append("    outputTransmission: ").append(toIndentedString(outputTransmission)).append("\n");
    sb.append("    executeEndpoint: ").append(toIndentedString(executeEndpoint)).append("\n");
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

