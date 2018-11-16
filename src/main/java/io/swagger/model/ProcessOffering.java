package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Process;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ProcessOffering
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:43:05.776Z[GMT]")

public class ProcessOffering   {
  @JsonProperty("process")
  private Process process = null;

  public ProcessOffering process(Process process) {
    this.process = process;
    return this;
  }

  /**
   * Get process
   * @return process
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Process getProcess() {
    return process;
  }

  public void setProcess(Process process) {
    this.process = process;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessOffering processOffering = (ProcessOffering) o;
    return Objects.equals(this.process, processOffering.process);
  }

  @Override
  public int hashCode() {
    return Objects.hash(process);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessOffering {\n");
    
    sb.append("    process: ").append(toIndentedString(process)).append("\n");
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

