/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.DataDescriptionType;
import io.swagger.model.FormatDescription;
import io.swagger.model.LiteralDataDomain;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * LiteralInputType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T12:20:58.856+01:00[Europe/Berlin]")

public class LiteralInputType extends DataDescriptionType  {
  @JsonProperty("minOccurs")
  private Integer minOccurs = null;

  @JsonProperty("maxOccurs")
  private Integer maxOccurs = null;

  @JsonProperty("literalDataDomain")
  private LiteralDataDomain literalDataDomain = null;

  public LiteralInputType minOccurs(Integer minOccurs) {
    this.minOccurs = minOccurs;
    return this;
  }

  /**
   * Get minOccurs
   * @return minOccurs
  **/
  @ApiModelProperty(value = "")


  public Integer getMinOccurs() {
    return minOccurs;
  }

  public void setMinOccurs(Integer minOccurs) {
    this.minOccurs = minOccurs;
  }

  public LiteralInputType maxOccurs(Integer maxOccurs) {
    this.maxOccurs = maxOccurs;
    return this;
  }

  /**
   * Get maxOccurs
   * @return maxOccurs
  **/
  @ApiModelProperty(value = "")


  public Integer getMaxOccurs() {
    return maxOccurs;
  }

  public void setMaxOccurs(Integer maxOccurs) {
    this.maxOccurs = maxOccurs;
  }

  public LiteralInputType literalDataDomain(LiteralDataDomain literalDataDomain) {
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
    LiteralInputType literalInputType = (LiteralInputType) o;
    return Objects.equals(this.minOccurs, literalInputType.minOccurs) &&
        Objects.equals(this.maxOccurs, literalInputType.maxOccurs) &&
        Objects.equals(this.literalDataDomain, literalInputType.literalDataDomain) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minOccurs, maxOccurs, literalDataDomain, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LiteralInputType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    minOccurs: ").append(toIndentedString(minOccurs)).append("\n");
    sb.append("    maxOccurs: ").append(toIndentedString(maxOccurs)).append("\n");
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

