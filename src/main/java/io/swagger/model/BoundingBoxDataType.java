package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.SupportedCRS;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BoundingBoxDataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class BoundingBoxDataType   {
  @JsonProperty("supportedCRS")
  @Valid
  private List<SupportedCRS> supportedCRS = new ArrayList<SupportedCRS>();

  public BoundingBoxDataType supportedCRS(List<SupportedCRS> supportedCRS) {
    this.supportedCRS = supportedCRS;
    return this;
  }

  public BoundingBoxDataType addSupportedCRSItem(SupportedCRS supportedCRSItem) {
    this.supportedCRS.add(supportedCRSItem);
    return this;
  }

  /**
   * Get supportedCRS
   * @return supportedCRS
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<SupportedCRS> getSupportedCRS() {
    return supportedCRS;
  }

  public void setSupportedCRS(List<SupportedCRS> supportedCRS) {
    this.supportedCRS = supportedCRS;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundingBoxDataType boundingBoxDataType = (BoundingBoxDataType) o;
    return Objects.equals(this.supportedCRS, boundingBoxDataType.supportedCRS);
  }

  @Override
  public int hashCode() {
    return Objects.hash(supportedCRS);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundingBoxDataType {\n");
    
    sb.append("    supportedCRS: ").append(toIndentedString(supportedCRS)).append("\n");
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

