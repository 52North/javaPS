package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.FormatDescription;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ComplexDataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:43:05.776Z[GMT]")

public class ComplexDataType   {
  @JsonProperty("formats")
  @Valid
  private List<FormatDescription> formats = new ArrayList<FormatDescription>();

  public ComplexDataType formats(List<FormatDescription> formats) {
    this.formats = formats;
    return this;
  }

  public ComplexDataType addFormatsItem(FormatDescription formatsItem) {
    this.formats.add(formatsItem);
    return this;
  }

  /**
   * Get formats
   * @return formats
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<FormatDescription> getFormats() {
    return formats;
  }

  public void setFormats(List<FormatDescription> formats) {
    this.formats = formats;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplexDataType complexDataType = (ComplexDataType) o;
    return Objects.equals(this.formats, complexDataType.formats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(formats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplexDataType {\n");
    
    sb.append("    formats: ").append(toIndentedString(formats)).append("\n");
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

