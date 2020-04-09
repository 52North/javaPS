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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ProcessSummary
 */
@Validated
public class ProcessSummary extends DescriptionType {
    @JsonProperty("id")
    private String id;

    @JsonProperty("version")
    private String version;

    @JsonProperty("jobControlOptions")
    @Valid
    private List<JobControlOptions> jobControlOptions;

    @JsonProperty("outputTransmission")
    @Valid
    private List<TransmissionMode> outputTransmission;

    @JsonProperty("links")
    @Valid
    private List<Link> links;

    public ProcessSummary id(String id) {
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

    public ProcessSummary version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     *
     * @return version
     **/
    @NotNull
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ProcessSummary jobControlOptions(List<JobControlOptions> jobControlOptions) {
        this.jobControlOptions = jobControlOptions;
        return this;
    }

    public ProcessSummary addJobControlOptionsItem(JobControlOptions jobControlOptionsItem) {
        if (this.jobControlOptions == null) {
            this.jobControlOptions = new ArrayList<JobControlOptions>();
        }
        this.jobControlOptions.add(jobControlOptionsItem);
        return this;
    }

    /**
     * Get jobControlOptions
     *
     * @return jobControlOptions
     **/
    @Valid
    public List<JobControlOptions> getJobControlOptions() {
        return jobControlOptions;
    }

    public void setJobControlOptions(List<JobControlOptions> jobControlOptions) {
        this.jobControlOptions = jobControlOptions;
    }

    public ProcessSummary outputTransmission(List<TransmissionMode> outputTransmission) {
        this.outputTransmission = outputTransmission;
        return this;
    }

    public ProcessSummary addOutputTransmissionItem(TransmissionMode outputTransmissionItem) {
        if (this.outputTransmission == null) {
            this.outputTransmission = new ArrayList<TransmissionMode>();
        }
        this.outputTransmission.add(outputTransmissionItem);
        return this;
    }

    /**
     * Get outputTransmission
     *
     * @return outputTransmission
     **/
    @Valid
    public List<TransmissionMode> getOutputTransmission() {
        return outputTransmission;
    }

    public void setOutputTransmission(List<TransmissionMode> outputTransmission) {
        this.outputTransmission = outputTransmission;
    }

    public ProcessSummary links(List<Link> links) {
        this.links = links;
        return this;
    }

    public ProcessSummary addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<Link>();
        }
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     *
     * @return links
     **/
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
        ProcessSummary processSummary = (ProcessSummary) o;
        return Objects.equals(this.id, processSummary.id) &&
               Objects.equals(this.version, processSummary.version) &&
               Objects.equals(this.jobControlOptions, processSummary.jobControlOptions) &&
               Objects.equals(this.outputTransmission, processSummary.outputTransmission) &&
               Objects.equals(this.links, processSummary.links) &&
               super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, jobControlOptions, outputTransmission, links, super.hashCode());
    }

    @Override
    public String toString() {
        return String.format("ProcessSummary{%s, id: %s, version: %s, "
                             + "jobControlOptions: %s, outputTransmission: %s, links: %s}",
                             super.toString(), id, version, jobControlOptions, outputTransmission, links);
    }
}
