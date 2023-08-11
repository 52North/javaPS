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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Link
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class Link {
    @JsonProperty("href")
    private String href;

    @JsonProperty("rel")
    private String rel;

    @JsonProperty("type")
    private String type;

    @JsonProperty("hreflang")
    private String hreflang;

    @JsonProperty("title")
    private String title;

    public Link href(String href) {
        this.href = href;
        return this;
    }

    /**
     * Get href
     *
     * @return href
     **/
    @NotNull
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Link rel(String rel) {
        this.rel = rel;
        return this;
    }

    /**
     * Get rel
     *
     * @return rel
     **/
    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public Link type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Link hreflang(String hreflang) {
        this.hreflang = hreflang;
        return this;
    }

    /**
     * Get hreflang
     *
     * @return hreflang
     **/
    public String getHreflang() {
        return hreflang;
    }

    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    public Link title(String title) {
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href) && Objects.equals(this.rel, link.rel)
                && Objects.equals(this.type, link.type) && Objects.equals(this.hreflang, link.hreflang)
                && Objects.equals(this.title, link.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, type, hreflang, title);
    }

    @Override
    public String toString() {

        return String.format("Link{href: %s, rel: %s, type: %s, hreflang: %s, title: %s}", href, rel, type, hreflang,
                title);
    }
}
