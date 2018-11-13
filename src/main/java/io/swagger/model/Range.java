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
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Range
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T12:20:58.856+01:00[Europe/Berlin]")

public class Range   {
  @JsonProperty("mimimumValue")
  private String mimimumValue = null;

  @JsonProperty("maximumValue")
  private String maximumValue = null;

  @JsonProperty("spacing")
  private String spacing = null;

  public Range mimimumValue(String mimimumValue) {
    this.mimimumValue = mimimumValue;
    return this;
  }

  /**
   * Get mimimumValue
   * @return mimimumValue
  **/
  @ApiModelProperty(value = "")


  public String getMimimumValue() {
    return mimimumValue;
  }

  public void setMimimumValue(String mimimumValue) {
    this.mimimumValue = mimimumValue;
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Range range = (Range) o;
    return Objects.equals(this.mimimumValue, range.mimimumValue) &&
        Objects.equals(this.maximumValue, range.maximumValue) &&
        Objects.equals(this.spacing, range.spacing);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mimimumValue, maximumValue, spacing);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Range {\n");
    
    sb.append("    mimimumValue: ").append(toIndentedString(mimimumValue)).append("\n");
    sb.append("    maximumValue: ").append(toIndentedString(maximumValue)).append("\n");
    sb.append("    spacing: ").append(toIndentedString(spacing)).append("\n");
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

