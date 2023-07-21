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
package org.n52.javaps.rest.serializer;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;

import java.net.URI;

@Configurable
public abstract class AbstractSerializer {
    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", "/rest/processes");
    }

    protected String createJobHref(String processId, String jobId) {
        return String.format("%s/%s/jobs/%s", serviceURL, processId, jobId);
    }

    protected String createResultHref(String processId, String jobId) {
        return String.format("%s/%s/jobs/%s/results", serviceURL, processId, jobId);
    }

    protected String getJobsHref(String processId) {
        return String.format("%s/%s/jobs", serviceURL, processId);
    }

    protected String getProcessHref(String processId) {
        return String.format("%s/%s", serviceURL, processId);
    }

}
