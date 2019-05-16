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
package org.n52.wps.javaps.rest.serializer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.JobStatus;

import io.swagger.model.Link;
import io.swagger.model.StatusInfo;
import io.swagger.model.StatusInfo.StatusEnum;

@Configurable
public class StatusInfoSerializer {

    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", "/rest/processes/");
    }

    public StatusInfoSerializer() {}
    
    public StatusInfo serialize(org.n52.shetland.ogc.wps.StatusInfo statusInfo, String processId, String jobId) {
        
        StatusInfo serializedStatusInfo = new StatusInfo();
        
        serializedStatusInfo.setStatus(mapStatus(statusInfo.getStatus()));
        
        Optional<Short> percentCompleted = statusInfo.getPercentCompleted();
        
        if(percentCompleted.isPresent()) {
            serializedStatusInfo.setProgress(Integer.valueOf(percentCompleted.get()));
        }
        
        List<Link> links = new ArrayList<>();
        
        Link link = new Link();
        
        String selfHref = createSelfHref(processId, jobId);
        
        link.setHref(selfHref);
        
        link.setRel("self");
        
        link.setType("application/json");
        
        link.setTitle("this document");
        
        links.add(link);        
        
        if(serializedStatusInfo.getStatus().equals(StatusEnum.SUCCESSFUL)) {
            
            link = new Link();
            
            link.setHref(createResultURL(selfHref));
            
            link.setRel("result");
            
            link.setType("application/json");
            
            link.setTitle("Job result");
            
            links.add(link);
        }else if(serializedStatusInfo.getStatus().equals(StatusEnum.FAILED)) {
            
            link = new Link();
            
            link.setHref(createResultURL(selfHref));
            
            link.setRel("exception");
            
            link.setType("application/json");
            
            link.setTitle("Job exception");
            
            links.add(link);
        }
        
        serializedStatusInfo.setLinks(links);
        
        return serializedStatusInfo;
        
    }

    private StatusEnum mapStatus(JobStatus status) {
        
        if(status.equals(JobStatus.succeeded())) {
            return StatusEnum.SUCCESSFUL;
        } else if(status.equals(JobStatus.failed())) {
            return StatusEnum.FAILED;
        } else if(status.equals(JobStatus.accepted()) || status.equals(JobStatus.running())) {
            return StatusEnum.RUNNING;
        }
        
        throw new IllegalArgumentException("Status not valid: " + status);
    }
    
    private String createSelfHref(String processId, String jobId) {
        return serviceURL + processId + "/jobs/" + jobId;
    }
    
    private String createResultURL(String selfHref) {
        return selfHref + "/result";
    }
    
}
