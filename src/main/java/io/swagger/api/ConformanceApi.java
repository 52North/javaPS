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
/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.2).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.Exception;
import io.swagger.model.ReqClasses;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")

@Api(value = "conformance", description = "the conformance API")
public interface ConformanceApi {

    @ApiOperation(value = "information about standards that this API conforms to", nickname = "getRequirementsClasses", notes = "list all requirements classes specified in a standard (e.g., WPS REST/JSON Binding Part 1: Core) that the server conforms to", response = ReqClasses.class, tags={ "Capabilities", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "the URIs of all requirements classes supported by the server", response = ReqClasses.class),
        @ApiResponse(code = 200, message = "An error occured.", response = Exception.class) })
    @RequestMapping(value = "rest/conformance",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<ReqClasses> getRequirementsClasses();

}
