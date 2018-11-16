package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.BoundingBoxData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ResultBoundingBox
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:20:35.957Z[GMT]")

public class ResultBoundingBox   {
  @JsonProperty("boundingBox")
  private BoundingBoxData boundingBox = null;

  public ResultBoundingBox boundingBox(BoundingBoxData boundingBox) {
    this.boundingBox = boundingBox;
    return this;
  }

  /**
   * Get boundingBox
   * @return boundingBox
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public BoundingBoxData getBoundingBox() {
    return boundingBox;
  }

  public void setBoundingBox(BoundingBoxData boundingBox) {
    this.boundingBox = boundingBox;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResultBoundingBox resultBoundingBox = (ResultBoundingBox) o;
    return Objects.equals(this.boundingBox, resultBoundingBox.boundingBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boundingBox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResultBoundingBox {\n");
    
    sb.append("    boundingBox: ").append(toIndentedString(boundingBox)).append("\n");
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

