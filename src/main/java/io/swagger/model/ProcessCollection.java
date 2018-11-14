package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.ProcessSummary;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ProcessCollection
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class ProcessCollection   {
  @JsonProperty("processes")
  @Valid
  private List<ProcessSummary> processes = new ArrayList<ProcessSummary>();

  public ProcessCollection processes(List<ProcessSummary> processes) {
    this.processes = processes;
    return this;
  }

  public ProcessCollection addProcessesItem(ProcessSummary processesItem) {
    this.processes.add(processesItem);
    return this;
  }

  /**
   * Get processes
   * @return processes
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<ProcessSummary> getProcesses() {
    return processes;
  }

  public void setProcesses(List<ProcessSummary> processes) {
    this.processes = processes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcessCollection processCollection = (ProcessCollection) o;
    return Objects.equals(this.processes, processCollection.processes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(processes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessCollection {\n");
    
    sb.append("    processes: ").append(toIndentedString(processes)).append("\n");
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

