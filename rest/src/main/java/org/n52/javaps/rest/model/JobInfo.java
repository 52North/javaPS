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

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * JobInfo
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
        date = "2020-01-28T10:33:35.029Z[GMT]")
public class JobInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("infos")
    private StatusInfo infos;

    public JobInfo id(String id) {
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

    public JobInfo infos(StatusInfo infos) {
        this.infos = infos;
        return this;
    }

    /**
     * Get infos
     *
     * @return infos
     **/
    @ApiModelProperty(value = "")

    @Valid
    public StatusInfo getInfos() {
        return infos;
    }

    public void setInfos(StatusInfo infos) {
        this.infos = infos;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobInfo jobInfo = (JobInfo) o;
        return Objects.equals(this.id, jobInfo.id) && Objects.equals(this.infos, jobInfo.infos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, infos);
    }

    @Override
    public String toString() {
        return String.format("JobInfo{id: %s, infos: %s}", id, infos);
    }

}
