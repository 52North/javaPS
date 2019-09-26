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
package org.n52.javaps.transactional.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.rest.MediaTypes;
import org.n52.javaps.rest.ProcessesApi;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.UndeletableProcessException;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Extension of {@link ProcessesApi} for transactional operations.
 *
 * @author Christian Autermann
 */
public interface TransactionalApi {

    /**
     * Deletes the process with the specified id.
     *
     * @param id The identifier of the process.
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UndeletableProcessException If the process is not deletable.
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ProcessesApi.BASE_URL + "/{id:.+}")
    void undeployProcess(@PathVariable("id") String id)
            throws ProcessNotFoundException, UndeletableProcessException;

    /**
     * Adds a new process to this service.
     *
     * @param request The description of the new process, a {@link ApplicationPackage}.
     * @return The {@linkplain ResponseEntity} containing the {@code Location} of the new process.
     * @throws DuplicateProcessException   If a process with the same identifier already exists.
     * @throws UnsupportedProcessException If the process is not supported.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = ProcessesApi.BASE_URL, consumes = MediaTypes.APPLICATION_JSON)
    ResponseEntity<?> deployProcess(@RequestBody JsonNode request)
            throws DuplicateProcessException, UnsupportedProcessException;

    /**
     * Updates the process with the specified id.
     *
     * @param id      The identifier of the process.
     * @param request The description of the new process, a {@link ApplicationPackage}
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UnsupportedProcessException If the process is not supported.
     * @throws UndeletableProcessException If the process cannot be updated.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(path = ProcessesApi.BASE_URL + "/{id:.+}", consumes = MediaTypes.APPLICATION_JSON)
    void updateProcess(@PathVariable("id") String id, @RequestBody JsonNode request)
            throws ProcessNotFoundException, UnsupportedProcessException, UndeletableProcessException;

}
