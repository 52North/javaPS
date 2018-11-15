package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BoundingBoxData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class BoundingBoxData   {
  @JsonProperty("crs")
  private String crs = null;

  @JsonProperty("lowerCorner")
  private String lowerCorner = null;

  @JsonProperty("upperCorner")
  private String upperCorner = null;

  public BoundingBoxData crs(String crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Get crs
   * @return crs
  **/
  @ApiModelProperty(value = "")


  public String getCrs() {
    return crs;
  }

  public void setCrs(String crs) {
    this.crs = crs;
  }

  public BoundingBoxData lowerCorner(String lowerCorner) {
    this.lowerCorner = lowerCorner;
    return this;
  }

  /**
   * Get lowerCorner
   * @return lowerCorner
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getLowerCorner() {
    return lowerCorner;
  }

  public void setLowerCorner(String lowerCorner) {
    this.lowerCorner = lowerCorner;
  }

  public BoundingBoxData upperCorner(String upperCorner) {
    this.upperCorner = upperCorner;
    return this;
  }

  /**
   * Get upperCorner
   * @return upperCorner
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getUpperCorner() {
    return upperCorner;
  }

  public void setUpperCorner(String upperCorner) {
    this.upperCorner = upperCorner;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundingBoxData boundingBoxData = (BoundingBoxData) o;
    return Objects.equals(this.crs, boundingBoxData.crs) &&
        Objects.equals(this.lowerCorner, boundingBoxData.lowerCorner) &&
        Objects.equals(this.upperCorner, boundingBoxData.upperCorner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crs, lowerCorner, upperCorner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundingBoxData {\n");
    
    sb.append("    crs: ").append(toIndentedString(crs)).append("\n");
    sb.append("    lowerCorner: ").append(toIndentedString(lowerCorner)).append("\n");
    sb.append("    upperCorner: ").append(toIndentedString(upperCorner)).append("\n");
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

