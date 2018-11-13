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
import io.swagger.model.Metadata;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * DescriptionType
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T12:20:58.856+01:00[Europe/Berlin]")

public class DescriptionType   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("abstract")
  private String _abstract = null;

  @JsonProperty("keywords")
  @Valid
  private List<String> keywords = null;

  @JsonProperty("metadata")
  @Valid
  private List<Metadata> metadata = null;

  public DescriptionType id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DescriptionType title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
  **/
  @ApiModelProperty(value = "")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public DescriptionType _abstract(String _abstract) {
    this._abstract = _abstract;
    return this;
  }

  /**
   * Get _abstract
   * @return _abstract
  **/
  @ApiModelProperty(value = "")


  public String getAbstract() {
    return _abstract;
  }

  public void setAbstract(String _abstract) {
    this._abstract = _abstract;
  }

  public DescriptionType keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public DescriptionType addKeywordsItem(String keywordsItem) {
    if (this.keywords == null) {
      this.keywords = new ArrayList<String>();
    }
    this.keywords.add(keywordsItem);
    return this;
  }

  /**
   * Get keywords
   * @return keywords
  **/
  @ApiModelProperty(value = "")


  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public DescriptionType metadata(List<Metadata> metadata) {
    this.metadata = metadata;
    return this;
  }

  public DescriptionType addMetadataItem(Metadata metadataItem) {
    if (this.metadata == null) {
      this.metadata = new ArrayList<Metadata>();
    }
    this.metadata.add(metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<Metadata> getMetadata() {
    return metadata;
  }

  public void setMetadata(List<Metadata> metadata) {
    this.metadata = metadata;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DescriptionType descriptionType = (DescriptionType) o;
    return Objects.equals(this.id, descriptionType.id) &&
        Objects.equals(this.title, descriptionType.title) &&
        Objects.equals(this._abstract, descriptionType._abstract) &&
        Objects.equals(this.keywords, descriptionType.keywords) &&
        Objects.equals(this.metadata, descriptionType.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, _abstract, keywords, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DescriptionType {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    _abstract: ").append(toIndentedString(_abstract)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

