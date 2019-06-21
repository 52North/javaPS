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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BoundingBoxData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-20T11:06:03.350Z[GMT]")
public class BoundingBoxData   {
  @JsonProperty("crs")
  private String crs = null;

  @JsonProperty("bbox")
  @Valid
  private List<BigDecimal> bbox = new ArrayList<BigDecimal>();

  public BoundingBoxData crs(String crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Get crs
   * @return crs
  **/
  @ApiModelProperty(value = "")

  public String getCrs() {
    return crs;
  }

  public void setCrs(String crs) {
    this.crs = crs;
  }

  public BoundingBoxData bbox(List<BigDecimal> bbox) {
    this.bbox = bbox;
    return this;
  }

  public BoundingBoxData addBboxItem(BigDecimal bboxItem) {
    this.bbox.add(bboxItem);
    return this;
  }

  /**
   * Get bbox
   * @return bbox
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
@Size(min=4,max=6)   public List<BigDecimal> getBbox() {
    return bbox;
  }

  public void setBbox(List<BigDecimal> bbox) {
    this.bbox = bbox;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundingBoxData boundingBoxData = (BoundingBoxData) o;
    return Objects.equals(this.crs, boundingBoxData.crs) &&
        Objects.equals(this.bbox, boundingBoxData.bbox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crs, bbox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoundingBoxData {\n");
    
    sb.append("    crs: ").append(toIndentedString(crs)).append("\n");
    sb.append("    bbox: ").append(toIndentedString(bbox)).append("\n");
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
