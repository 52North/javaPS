/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.rest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DescriptionType
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class DescriptionType {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("keywords")
    @Valid
    private List<String> keywords;

    @JsonProperty("metadata")
    @Valid
    private List<Metadata> metadata;

    @JsonProperty("additionalParameters")
    private List<AdditionalParameter> additionalParameters;

    public DescriptionType id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
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
     *
     * @return title
     **/
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DescriptionType description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     **/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     *
     * @return keywords
     **/
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
     *
     * @return metadata
     **/
    @Valid
    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public DescriptionType additionalParameters(List<AdditionalParameter> additionalParameters) {
        this.additionalParameters = additionalParameters;
        return this;
    }

    /**
     * Get additionalParameters
     *
     * @return additionalParameters
     **/
    public List<AdditionalParameter> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(List<AdditionalParameter> additionalParameters) {
        this.additionalParameters = additionalParameters;
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
        return Objects.equals(this.id, descriptionType.id) && Objects.equals(this.title, descriptionType.title)
                && Objects.equals(this.description, descriptionType.description)
                && Objects.equals(this.keywords, descriptionType.keywords)
                && Objects.equals(this.metadata, descriptionType.metadata)
                && Objects.equals(this.additionalParameters, descriptionType.additionalParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, keywords, metadata, additionalParameters);
    }

    @Override
    public String toString() {
        return String.format(
           "DescriptionType{id: %s, title: %s, description: %s, keywords: %s, metadata: %s, additionalParameters: %s}",
           id, title, description, keywords, metadata, additionalParameters);
    }
}
