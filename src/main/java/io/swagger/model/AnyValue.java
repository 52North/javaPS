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
 * AnyValue
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class AnyValue   {
  @JsonProperty("anyValue")
  private Boolean anyValue = true;

  public AnyValue anyValue(Boolean anyValue) {
    this.anyValue = anyValue;
    return this;
  }

  /**
   * Get anyValue
   * @return anyValue
  **/
  @ApiModelProperty(value = "")


  public Boolean isAnyValue() {
    return anyValue;
  }

  public void setAnyValue(Boolean anyValue) {
    this.anyValue = anyValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnyValue anyValue = (AnyValue) o;
    return Objects.equals(this.anyValue, anyValue.anyValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anyValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnyValue {\n");
    
    sb.append("    anyValue: ").append(toIndentedString(anyValue)).append("\n");
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

