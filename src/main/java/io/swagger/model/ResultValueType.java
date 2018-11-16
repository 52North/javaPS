package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.BoundingBoxData;
import io.swagger.model.InlineValue;
import io.swagger.model.ReferenceValue;
import io.swagger.model.ResultBoundingBox;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ResultValueType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:20:35.957Z[GMT]")

public class ResultValueType   {
  @JsonProperty("inlineValue")
  private Object inlineValue = null;

  @JsonProperty("href")
  private String href = null;

  @JsonProperty("boundingBox")
  private BoundingBoxData boundingBox = null;

  public ResultValueType inlineValue(Object inlineValue) {
    this.inlineValue = inlineValue;
    return this;
  }

  /**
   * Get inlineValue
   * @return inlineValue
  **/
  @ApiModelProperty(value = "")


  public Object getInlineValue() {
    return inlineValue;
  }

  public void setInlineValue(Object inlineValue) {
    this.inlineValue = inlineValue;
  }

  public ResultValueType href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href
   * @return href
  **/
  @ApiModelProperty(value = "")


  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public ResultValueType boundingBox(BoundingBoxData boundingBox) {
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
    ResultValueType resultValueType = (ResultValueType) o;
    return Objects.equals(this.inlineValue, resultValueType.inlineValue) &&
        Objects.equals(this.href, resultValueType.href) &&
        Objects.equals(this.boundingBox, resultValueType.boundingBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inlineValue, href, boundingBox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResultValueType {\n");
    
    sb.append("    inlineValue: ").append(toIndentedString(inlineValue)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

