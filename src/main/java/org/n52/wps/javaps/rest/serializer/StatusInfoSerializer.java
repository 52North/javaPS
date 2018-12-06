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

import java.util.Optional;

import org.n52.shetland.ogc.wps.JobStatus;

import io.swagger.model.StatusInfo;
import io.swagger.model.StatusInfo.StatusEnum;

public class StatusInfoSerializer {

    public static StatusInfo serialize(org.n52.shetland.ogc.wps.StatusInfo statusInfo) {
        
        StatusInfo serializedStatusInfo = new StatusInfo();
        
        serializedStatusInfo.setStatus(mapStatus(statusInfo.getStatus()));
        
        Optional<Short> percentCompleted = statusInfo.getPercentCompleted();
        
        if(percentCompleted.isPresent()) {
            serializedStatusInfo.setProgress(Integer.valueOf(percentCompleted.get()));
        }
        
        return serializedStatusInfo;
        
    }

    private static StatusEnum mapStatus(JobStatus status) {
        
        if(status.equals(JobStatus.succeeded())) {
            return StatusEnum.SUCCESSFUL;
        } else if(status.equals(JobStatus.failed())) {
            return StatusEnum.FAILED;
        } else if(status.equals(JobStatus.accepted()) || status.equals(JobStatus.running())) {
            return StatusEnum.RUNNING;
        }
        
        throw new IllegalArgumentException("Status not valid: " + status);
    }
    
}
