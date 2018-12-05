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
import io.swagger.model.Link;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Root
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-16T13:43:05.776Z[GMT]")

public class Root   {
  @JsonProperty("links")
  @Valid
  private List<Link> links = new ArrayList<Link>();

  public Root links(List<Link> links) {
    this.links = links;
    return this;
  }

  public Root addLinksItem(Link linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
  **/
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/\",\"rel\":\"self\",\"type\":\"application/json\",\"title\":\"this document\"},{\"href\":\"http://data.example.org/api\",\"rel\":\"service\",\"type\":\"application/openapi+json;version=3.0\",\"title\":\"the API definition\"},{\"href\":\"http://data.example.org/conformance\",\"rel\":\"conformance\",\"type\":\"application/json\",\"title\":\"WPS 2.0 REST/JSON Binding conformance classes implemented by this server\"},{\"href\":\"http://data.example.org/processes\",\"rel\":\"data\",\"type\":\"application/json\",\"title\":\"Metadata about the processes\"}]", required = true, value = "")
  @NotNull

  @Valid

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Root root = (Root) o;
    return Objects.equals(this.links, root.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Root {\n");
    
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

