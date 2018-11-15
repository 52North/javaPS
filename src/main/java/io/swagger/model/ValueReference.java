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
 * ValueReference
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T14:41:11.502Z[GMT]")

public class ValueReference   {
  @JsonProperty("valueReference")
  private String valueReference = null;

  public ValueReference valueReference(String valueReference) {
    this.valueReference = valueReference;
    return this;
  }

  /**
   * Get valueReference
   * @return valueReference
  **/
  @ApiModelProperty(value = "")


  public String getValueReference() {
    return valueReference;
  }

  public void setValueReference(String valueReference) {
    this.valueReference = valueReference;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValueReference valueReference = (ValueReference) o;
    return Objects.equals(this.valueReference, valueReference.valueReference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valueReference);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValueReference {\n");
    
    sb.append("    valueReference: ").append(toIndentedString(valueReference)).append("\n");
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

