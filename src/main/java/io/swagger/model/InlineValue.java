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
 * InlineValue
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class InlineValue   {
  @JsonProperty("inlineValue")
  private String inlineValue = null;

  public InlineValue inlineValue(String inlineValue) {
    this.inlineValue = inlineValue;
    return this;
  }

  /**
   * Get inlineValue
   * @return inlineValue
  **/
  @ApiModelProperty(value = "")


  public String getInlineValue() {
    return inlineValue;
  }

  public void setInlineValue(String inlineValue) {
    this.inlineValue = inlineValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineValue inlineValue = (InlineValue) o;
    return Objects.equals(this.inlineValue, inlineValue.inlineValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inlineValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineValue {\n");
    
    sb.append("    inlineValue: ").append(toIndentedString(inlineValue)).append("\n");
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

