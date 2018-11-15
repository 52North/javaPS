package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Format;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class DataType   {
  @JsonProperty("format")
  private Format format = null;

  public DataType format(Format format) {
    this.format = format;
    return this;
  }

  /**
   * Get format
   * @return format
  **/
  @ApiModelProperty(value = "")

  @Valid

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataType dataType = (DataType) o;
    return Objects.equals(this.format, dataType.format);
  }

  @Override
  public int hashCode() {
    return Objects.hash(format);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataType {\n");
    
    sb.append("    format: ").append(toIndentedString(format)).append("\n");
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

