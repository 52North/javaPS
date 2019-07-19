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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-28T09:55:34.783Z[GMT]")
public class ProcessSummary extends DescriptionType {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("jobControlOptions")
    @Valid
    private List<JobControlOptions> jobControlOptions = null;

    @JsonProperty("outputTransmission")
    @Valid
    private List<TransmissionMode> outputTransmission = null;

    @JsonProperty("links")
    @Valid
    private List<Link> links = null;

    public ProcessSummary id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
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

    public ProcessSummary version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     *
     * @return version
     **/
    @ApiModelProperty(required = true, value = "")
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
    @ApiModelProperty(value = "")
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
    @ApiModelProperty(value = "")
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
    @ApiModelProperty(value = "")
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
        StringBuilder sb = new StringBuilder();
        sb.append("class ProcessSummary {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    jobControlOptions: ").append(toIndentedString(jobControlOptions)).append("\n");
        sb.append("    outputTransmission: ").append(toIndentedString(outputTransmission)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
