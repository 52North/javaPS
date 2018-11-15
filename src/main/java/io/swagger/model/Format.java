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
 * Format
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class Format   {
  @JsonProperty("mimeType")
  private String mimeType = null;

  @JsonProperty("schema")
  private String schema = null;

  @JsonProperty("encoding")
  private String encoding = null;

  public Format mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Get mimeType
   * @return mimeType
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public Format schema(String schema) {
    this.schema = schema;
    return this;
  }

  /**
   * Get schema
   * @return schema
  **/
  @ApiModelProperty(value = "")


  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public Format encoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /**
   * Get encoding
   * @return encoding
  **/
  @ApiModelProperty(value = "")


  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Format format = (Format) o;
    return Objects.equals(this.mimeType, format.mimeType) &&
        Objects.equals(this.schema, format.schema) &&
        Objects.equals(this.encoding, format.encoding);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mimeType, schema, encoding);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Format {\n");
    
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    encoding: ").append(toIndentedString(encoding)).append("\n");
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

