package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.LiteralDataDomain;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * LiteralDataType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class LiteralDataType   {
  @JsonProperty("literalDataDomain")
  private LiteralDataDomain literalDataDomain = null;

  public LiteralDataType literalDataDomain(LiteralDataDomain literalDataDomain) {
    this.literalDataDomain = literalDataDomain;
    return this;
  }

  /**
   * Get literalDataDomain
   * @return literalDataDomain
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public LiteralDataDomain getLiteralDataDomain() {
    return literalDataDomain;
  }

  public void setLiteralDataDomain(LiteralDataDomain literalDataDomain) {
    this.literalDataDomain = literalDataDomain;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiteralDataType literalDataType = (LiteralDataType) o;
    return Objects.equals(this.literalDataDomain, literalDataType.literalDataDomain);
  }

  @Override
  public int hashCode() {
    return Objects.hash(literalDataDomain);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LiteralDataType {\n");
    
    sb.append("    literalDataDomain: ").append(toIndentedString(literalDataDomain)).append("\n");
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

