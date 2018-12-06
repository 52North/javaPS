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

import io.swagger.model.Exception;
import io.swagger.model.ReqClasses;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")

@Controller
public class ConformanceApiController implements ConformanceApi {

    private static final Logger log = LoggerFactory.getLogger(ConformanceApiController.class);

//    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ConformanceApiController(HttpServletRequest request) {
//        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ReqClasses> getRequirementsClasses() {
        String accept = request.getHeader("Accept");
        
        ReqClasses reqClasses = new ReqClasses();
        
        List<String> conformsTo = new ArrayList<>();
        
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/core"); 
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/oas30");
        conformsTo.add("http://www.opengis.net/spec/wps-rest/req/html");
        
        reqClasses.setConformsTo(conformsTo);
        
        return ResponseEntity.ok(reqClasses);
    }

}
