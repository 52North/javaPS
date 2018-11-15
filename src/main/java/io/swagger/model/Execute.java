package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Input;
import io.swagger.model.Output;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Execute
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class Execute   {
  @JsonProperty("inputs")
  @Valid
  private List<Input> inputs = null;

  @JsonProperty("outputs")
  @Valid
  private List<Output> outputs = new ArrayList<Output>();

  public Execute inputs(List<Input> inputs) {
    this.inputs = inputs;
    return this;
  }

  public Execute addInputsItem(Input inputsItem) {
    if (this.inputs == null) {
      this.inputs = new ArrayList<Input>();
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

  public List<Input> getInputs() {
    return inputs;
  }

  public void setInputs(List<Input> inputs) {
    this.inputs = inputs;
  }

  public Execute outputs(List<Output> outputs) {
    this.outputs = outputs;
    return this;
  }

  public Execute addOutputsItem(Output outputsItem) {
    this.outputs.add(outputsItem);
    return this;
  }

  /**
   * Get outputs
   * @return outputs
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<Output> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<Output> outputs) {
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
    Execute execute = (Execute) o;
    return Objects.equals(this.inputs, execute.inputs) &&
        Objects.equals(this.outputs, execute.outputs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inputs, outputs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Execute {\n");
    
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

