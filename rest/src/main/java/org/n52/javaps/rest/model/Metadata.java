/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Metadata
 */
@Validated
public class Metadata {
    @JsonProperty("title")
    private String title;

    @JsonProperty("role")
    private String role;

    @JsonProperty("href")
    private String href;

    public Metadata title(String title) {
        this.title = title;
        return this;
    }

    public Metadata role(String role) {
        this.role = role;
        return this;
    }

    public Metadata href(String href) {
        this.href = href;
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

    /**
     * Get role
     *
     * @return role
     **/
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Get href
     *
     * @return href
     **/
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Metadata metadata = (Metadata) o;
        return Objects.equals(this.title, metadata.title) && Objects.equals(this.role, metadata.role)
                && Objects.equals(this.href, metadata.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, role, href);
    }

    @Override
    public String toString() {
        return String.format("Metadata{title: %s, role: %s, href: %s}", title, role, href);
    }
}
