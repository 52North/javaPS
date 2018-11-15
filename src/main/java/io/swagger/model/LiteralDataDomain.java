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
 * LiteralDataDomain
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-15T08:00:15.505Z[GMT]")

public class LiteralDataDomain   {
  @JsonProperty("dataType")
  private String dataType = null;

  @JsonProperty("defaultValue")
  private String defaultValue = null;

  @JsonProperty("valueDefinition")
  private Object valueDefinition = null;

  @JsonProperty("uom")
  private String uom = null;

  public LiteralDataDomain dataType(String dataType) {
    this.dataType = dataType;
    return this;
  }

  /**
   * Get dataType
   * @return dataType
  **/
  @ApiModelProperty(value = "")


  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public LiteralDataDomain defaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * Get defaultValue
   * @return defaultValue
  **/
  @ApiModelProperty(value = "")


  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public LiteralDataDomain valueDefinition(Object valueDefinition) {
    this.valueDefinition = valueDefinition;
    return this;
  }

  /**
   * Get valueDefinition
   * @return valueDefinition
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Object getValueDefinition() {
    return valueDefinition;
  }

  public void setValueDefinition(Object valueDefinition) {
    this.valueDefinition = valueDefinition;
  }

  public LiteralDataDomain uom(String uom) {
    this.uom = uom;
    return this;
  }

  /**
   * Get uom
   * @return uom
  **/
  @ApiModelProperty(value = "")


  public String getUom() {
    return uom;
  }

  public void setUom(String uom) {
    this.uom = uom;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiteralDataDomain literalDataDomain = (LiteralDataDomain) o;
    return Objects.equals(this.dataType, literalDataDomain.dataType) &&
        Objects.equals(this.defaultValue, literalDataDomain.defaultValue) &&
        Objects.equals(this.valueDefinition, literalDataDomain.valueDefinition) &&
        Objects.equals(this.uom, literalDataDomain.uom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataType, defaultValue, valueDefinition, uom);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LiteralDataDomain {\n");
    
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    defaultValue: ").append(toIndentedString(defaultValue)).append("\n");
    sb.append("    valueDefinition: ").append(toIndentedString(valueDefinition)).append("\n");
    sb.append("    uom: ").append(toIndentedString(uom)).append("\n");
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

