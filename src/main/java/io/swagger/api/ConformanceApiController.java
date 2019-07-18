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
package io.swagger.api;

import io.swagger.model.ReqClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")
@Controller
public class ConformanceApiController implements ConformanceApi {

    private static final Logger log = LoggerFactory.getLogger(ConformanceApiController.class);

    public ResponseEntity<ReqClasses> getRequirementsClasses() {
        List<String> conformsTo = Arrays.asList(
                "http://www.opengis.net/spec/wps-rest/req/core",
                "http://www.opengis.net/spec/wps-rest/req/oas30",
                "http://www.opengis.net/spec/wps-rest/req/html");
        return ResponseEntity.ok(new ReqClasses().conformsTo(conformsTo));
    }

}
