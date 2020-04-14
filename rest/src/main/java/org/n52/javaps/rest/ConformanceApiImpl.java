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
package org.n52.javaps.rest;

import org.n52.javaps.rest.model.ReqClasses;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class ConformanceApiImpl implements ConformanceApi {

    @Override
    public ReqClasses getConformanceClasses() {
        List<String> conformsTo = Arrays.asList("http://www.opengis.net/spec/wps-rest/req/core",
                "http://www.opengis.net/spec/wps-rest/req/oas30", "http://www.opengis.net/spec/wps-rest/req/html");
        return new ReqClasses().conformsTo(conformsTo);
    }

}
