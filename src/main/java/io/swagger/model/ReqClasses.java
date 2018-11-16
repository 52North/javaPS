package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReqClasses
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:43:05.776Z[GMT]")

public class ReqClasses   {
  @JsonProperty("conformsTo")
  @Valid
  private List<String> conformsTo = new ArrayList<String>();

  public ReqClasses conformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
    return this;
  }

  public ReqClasses addConformsToItem(String conformsToItem) {
    this.conformsTo.add(conformsToItem);
    return this;
  }

  /**
   * Get conformsTo
   * @return conformsTo
  **/
  @ApiModelProperty(example = "[\"http://www.opengis.net/spec/wps-rest/req/core\",\"http://www.opengis.net/spec/wps-rest/req/oas30\",\"http://www.opengis.net/spec/wps-rest/req/html\"]", required = true, value = "")
  @NotNull


  public List<String> getConformsTo() {
    return conformsTo;
  }

  public void setConformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReqClasses reqClasses = (ReqClasses) o;
    return Objects.equals(this.conformsTo, reqClasses.conformsTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conformsTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReqClasses {\n");
    
    sb.append("    conformsTo: ").append(toIndentedString(conformsTo)).append("\n");
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

