/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.wp.javaps.transactional.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface TransactionalApi {

    String baseURL = "/rest";

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = baseURL + "/processes/{id}")
    void undeployProcess(@PathVariable("id") String id)
            throws ProcessNotFoundException, NotUndeployableProcessException;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = baseURL + "/processes")
    ResponseEntity<?> deployProcess(@RequestBody JsonNode request)
            throws DuplicateProcessException, UnsupportedProcessException;

}
