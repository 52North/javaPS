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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * StatusInfo
 */
@Validated
@JsonInclude(Include.NON_NULL)
public class StatusInfo {

    @JsonProperty("status")
    private StatusEnum status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("progress")
    private Integer progress;

    @JsonProperty("jobID")
    private String jobID;

    @JsonProperty("links")
    @Valid
    private List<Link> links = new ArrayList<Link>();

    public StatusInfo status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     *
     * @return status
     **/
    @NotNull
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusInfo message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Get message
     *
     * @return message
     **/
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusInfo progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    /**
     * Get progress minimum: 0 maximum: 100
     *
     * @return progress
     **/
    @Min(0)
    @Max(100)
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public StatusInfo links(List<Link> links) {
        this.links = links;
        return this;
    }

    public StatusInfo addLinksItem(Link linksItem) {
        this.links.add(linksItem);
        return this;
    }

    public StatusInfo jobID(String jobID) {
        this.jobID = jobID;
        return this;
    }

    /**
     * Get jobID
     *
     * @return jobID
     **/
    @NotNull

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
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
        StatusInfo statusInfo = (StatusInfo) o;
        return Objects.equals(this.status, statusInfo.status) && Objects.equals(this.message, statusInfo.message)
                && Objects.equals(this.progress, statusInfo.progress) && Objects.equals(this.jobID, statusInfo.jobID)
                && Objects.equals(this.links, statusInfo.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, progress, jobID, links);
    }

    @Override
    public String toString() {
        return String.format("StatusInfo{status: %s, message: %s, progress: %s, jobID: %s, links: %s}", status, message,
                progress, jobID, links);
    }
}
