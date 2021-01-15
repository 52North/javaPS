/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.javaps.rest.model.ReqClasses;
import org.n52.javaps.rest.settings.RestSettingsConstants;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@Configurable
public class ConformanceApiImpl implements ConformanceApi {

    private boolean isJobListEnabled;

    @Setting(RestSettingsConstants.ENABLE_JOB_LIST_EXTENSION)
    public void setIsJobListEnabled(Boolean isJobListEnabled) {
        Validation.notNull("isJobListEnabled", isJobListEnabled);
        this.isJobListEnabled = isJobListEnabled;
    }

    @Override
    public ReqClasses getConformanceClasses() {
        List<String> conformsTo = Arrays.asList("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/core",
                "http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/oas30",
                "http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/json",
                "http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/html");

        if (isJobListEnabled) {
            conformsTo = new ArrayList<String>(conformsTo);
            conformsTo.add(
                "http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/job-list");
        }

        return new ReqClasses().conformsTo(conformsTo);
    }

}
