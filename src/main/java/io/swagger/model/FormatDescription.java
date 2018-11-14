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
 * FormatDescription
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-14T13:20:59.832Z[GMT]")

public class FormatDescription extends Format  {
  @JsonProperty("maximumMegabytes")
  private Integer maximumMegabytes = null;

  @JsonProperty("default")
  private Boolean _default = false;

  public FormatDescription maximumMegabytes(Integer maximumMegabytes) {
    this.maximumMegabytes = maximumMegabytes;
    return this;
  }

  /**
   * Get maximumMegabytes
   * @return maximumMegabytes
  **/
  @ApiModelProperty(value = "")


  public Integer getMaximumMegabytes() {
    return maximumMegabytes;
  }

  public void setMaximumMegabytes(Integer maximumMegabytes) {
    this.maximumMegabytes = maximumMegabytes;
  }

  public FormatDescription _default(Boolean _default) {
    this._default = _default;
    return this;
  }

  /**
   * Get _default
   * @return _default
  **/
  @ApiModelProperty(value = "")


  public Boolean isDefault() {
    return _default;
  }

  public void setDefault(Boolean _default) {
    this._default = _default;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormatDescription formatDescription = (FormatDescription) o;
    return Objects.equals(this.maximumMegabytes, formatDescription.maximumMegabytes) &&
        Objects.equals(this._default, formatDescription._default) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maximumMegabytes, _default, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormatDescription {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    maximumMegabytes: ").append(toIndentedString(maximumMegabytes)).append("\n");
    sb.append("    _default: ").append(toIndentedString(_default)).append("\n");
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

