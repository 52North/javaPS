/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LandingPage
 */
@Validated
public class LandingPage {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("links")
    @Valid
    private List<Link> links = new ArrayList<Link>();

    public LandingPage title(String title) {
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

    public LandingPage description(String description) {
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

    public LandingPage links(List<Link> links) {
        this.links = links;
        return this;
    }

    public LandingPage addLinksItem(Link linksItem) {
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     *
     * @return links
     **/
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
        LandingPage landingPage = (LandingPage) o;
        return Objects.equals(this.title, landingPage.title)
                && Objects.equals(this.description, landingPage.description)
                && Objects.equals(this.links, landingPage.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, links);
    }

    @Override
    public String toString() {
        return String.format("LandingPage{title: %s, description: %s, links: %s}", title, description, links);
    }
}
