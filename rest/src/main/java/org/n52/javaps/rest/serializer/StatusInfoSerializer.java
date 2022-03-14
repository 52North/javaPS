/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.rest.serializer;

import org.n52.javaps.rest.model.Link;
import org.n52.javaps.rest.model.StatusInfo;
import org.n52.javaps.rest.model.StatusEnum;
import org.n52.faroe.annotation.Configurable;
import org.n52.javaps.rest.MediaTypes;
import org.n52.shetland.ogc.wps.JobStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Configurable
public class StatusInfoSerializer extends AbstractSerializer {

    public StatusInfo serialize(org.n52.shetland.ogc.wps.StatusInfo statusInfo,
            String processId,
            String jobId,
            boolean isList) {

        StatusInfo serializedStatusInfo = new StatusInfo();

        serializedStatusInfo.setJobID(jobId);

        serializedStatusInfo.setStatus(createStatusEnum(statusInfo.getStatus()));

        statusInfo.getPercentCompleted().map(Integer::valueOf).ifPresent(serializedStatusInfo::setProgress);

        List<Link> links = new ArrayList<>(2);
        // if this a job list is requested, use rel "status" instead of "self"
        if (isList) {
            links.add(createStatusLink(processId, jobId));
        } else {
            links.add(createSelfLink(processId, jobId));
        }
        if (statusInfo.getStatus().equals(JobStatus.succeeded())) {
            links.add(createResultLink(processId, jobId));
        } else if (statusInfo.getStatus().equals(JobStatus.failed())) {
            links.add(createExceptionLink(processId, jobId));
        }

        serializedStatusInfo.setLinks(links);

        return serializedStatusInfo;

    }

    private Link createSelfLink(String processId,
            String jobId) {
        Link selfLink = new Link();
        selfLink.setHref(createJobHref(processId, jobId));
        selfLink.setRel("self");
        selfLink.setType(MediaTypes.APPLICATION_JSON);
        selfLink.setTitle("This document");
        return selfLink;
    }

    private Link createStatusLink(String processId,
            String jobId) {
        Link selfLink = new Link();
        selfLink.setHref(createJobHref(processId, jobId));
        selfLink.setRel("status");
        selfLink.setType(MediaTypes.APPLICATION_JSON);
        selfLink.setTitle("Status document");
        return selfLink;
    }

    private Link createResultLink(String processId,
            String jobId) {
        Link resultLink = new Link();
        resultLink.setHref(createResultHref(processId, jobId));
        resultLink.setRel("results");
        resultLink.setType(MediaTypes.APPLICATION_JSON);
        resultLink.setTitle("Job results");
        return resultLink;
    }

    private Link createExceptionLink(String processId,
            String jobId) {
        Link exceptionLink = new Link();
        exceptionLink.setHref(createResultHref(processId, jobId));
        exceptionLink.setRel("exceptions");
        exceptionLink.setType(MediaTypes.APPLICATION_JSON);
        exceptionLink.setTitle("Job exceptions");
        return exceptionLink;
    }

    private StatusEnum createStatusEnum(JobStatus status) {
        if (status.equals(JobStatus.succeeded())) {
            return StatusEnum.SUCCESSFUL;
        } else if (status.equals(JobStatus.failed())) {
            return StatusEnum.FAILED;
        } else if (status.equals(JobStatus.accepted())) {
            return StatusEnum.ACCEPTED;
        } else if (status.equals(JobStatus.running())) {
            return StatusEnum.RUNNING;
        }
        throw new IllegalArgumentException("Status not valid: " + status);
    }

}
