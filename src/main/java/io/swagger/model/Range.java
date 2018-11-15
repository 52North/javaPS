package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Range
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class Range   {
  @JsonProperty("minimumValue")
  private String minimumValue = null;

  @JsonProperty("maximumValue")
  private String maximumValue = null;

  @JsonProperty("spacing")
  private String spacing = null;

  /**
   * Gets or Sets rangeClosure
   */
  public enum RangeClosureEnum {
    CLOSED("closed"),
    
    OPEN("open"),
    
    OPEN_CLOSED("open-closed"),
    
    CLOSED_OPEN("closed-open");

    private String value;

    RangeClosureEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RangeClosureEnum fromValue(String text) {
      for (RangeClosureEnum b : RangeClosureEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("rangeClosure")
  private RangeClosureEnum rangeClosure = null;

  public Range minimumValue(String minimumValue) {
    this.minimumValue = minimumValue;
    return this;
  }

  /**
   * Get minimumValue
   * @return minimumValue
  **/
  @ApiModelProperty(value = "")


  public String getMinimumValue() {
    return minimumValue;
  }

  public void setMinimumValue(String minimumValue) {
    this.minimumValue = minimumValue;
  }

  public Range maximumValue(String maximumValue) {
    this.maximumValue = maximumValue;
    return this;
  }

  /**
   * Get maximumValue
   * @return maximumValue
  **/
  @ApiModelProperty(value = "")


  public String getMaximumValue() {
    return maximumValue;
  }

  public void setMaximumValue(String maximumValue) {
    this.maximumValue = maximumValue;
  }

  public Range spacing(String spacing) {
    this.spacing = spacing;
    return this;
  }

  /**
   * Get spacing
   * @return spacing
  **/
  @ApiModelProperty(value = "")


  public String getSpacing() {
    return spacing;
  }

  public void setSpacing(String spacing) {
    this.spacing = spacing;
  }

  public Range rangeClosure(RangeClosureEnum rangeClosure) {
    this.rangeClosure = rangeClosure;
    return this;
  }

  /**
   * Get rangeClosure
   * @return rangeClosure
  **/
  @ApiModelProperty(value = "")


  public RangeClosureEnum getRangeClosure() {
    return rangeClosure;
  }

  public void setRangeClosure(RangeClosureEnum rangeClosure) {
    this.rangeClosure = rangeClosure;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Range range = (Range) o;
    return Objects.equals(this.minimumValue, range.minimumValue) &&
        Objects.equals(this.maximumValue, range.maximumValue) &&
        Objects.equals(this.spacing, range.spacing) &&
        Objects.equals(this.rangeClosure, range.rangeClosure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minimumValue, maximumValue, spacing, rangeClosure);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Range {\n");
    
    sb.append("    minimumValue: ").append(toIndentedString(minimumValue)).append("\n");
    sb.append("    maximumValue: ").append(toIndentedString(maximumValue)).append("\n");
    sb.append("    spacing: ").append(toIndentedString(spacing)).append("\n");
    sb.append("    rangeClosure: ").append(toIndentedString(rangeClosure)).append("\n");
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

