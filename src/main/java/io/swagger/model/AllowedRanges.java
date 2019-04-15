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
import io.swagger.model.Range;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AllowedRanges
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-28T09:55:34.783Z[GMT]")
public class AllowedRanges   {
  @JsonProperty("allowedRanges")
  @Valid
  private List<Range> allowedRanges = new ArrayList<Range>();

  public AllowedRanges allowedRanges(List<Range> allowedRanges) {
    this.allowedRanges = allowedRanges;
    return this;
  }

  public AllowedRanges addAllowedRangesItem(Range allowedRangesItem) {
    this.allowedRanges.add(allowedRangesItem);
    return this;
  }

  /**
   * Get allowedRanges
   * @return allowedRanges
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public List<Range> getAllowedRanges() {
    return allowedRanges;
  }

  public void setAllowedRanges(List<Range> allowedRanges) {
    this.allowedRanges = allowedRanges;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllowedRanges allowedRanges = (AllowedRanges) o;
    return Objects.equals(this.allowedRanges, allowedRanges.allowedRanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowedRanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllowedRanges {\n");
    
    sb.append("    allowedRanges: ").append(toIndentedString(allowedRanges)).append("\n");
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
