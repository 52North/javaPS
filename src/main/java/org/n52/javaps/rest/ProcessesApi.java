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
package org.n52.javaps.rest;

import org.n52.javaps.algorithm.ExecutionException;
import org.n52.javaps.rest.model.Execute;
import org.n52.javaps.rest.model.ProcessCollection;
import org.n52.javaps.rest.model.ProcessOffering;
import org.n52.javaps.rest.model.StatusInfo;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

public interface ProcessesApi {
    String BASE_URL = RootApi.BASE_URL + "/processes";

    @PostMapping(value = BASE_URL + "/{processId}/jobs", produces = MediaTypes.APPLICATION_JSON,
                 consumes = MediaTypes.APPLICATION_JSON)
    ResponseEntity<?> execute(@Valid @RequestBody Execute body, @PathVariable("processId") String processId)
            throws EngineException, ExecutionException;

    @GetMapping(value = BASE_URL + "/{processId}/jobs", produces = MediaTypes.APPLICATION_JSON)
    ResponseEntity<?> getJobList(@PathVariable("processId") String processId);

    @GetMapping(value = BASE_URL + "/{processId}/jobs", produces = MediaTypes.TEXT_HTML)
    String getExecuteForm(@PathVariable("processId") String processId, Model model);

    @GetMapping(value = BASE_URL + "/{processId:.+}", produces = MediaTypes.APPLICATION_JSON)
    @ResponseBody
    ProcessOffering getProcessDescription(@PathVariable("processId") String id) throws ProcessNotFoundException;

    @GetMapping(value = BASE_URL, produces = MediaTypes.APPLICATION_JSON)
    @ResponseBody
    ProcessCollection getProcesses();

    @GetMapping(value = BASE_URL + "/{processId}/jobs/{jobId}/result",
                produces = MediaTypes.APPLICATION_JSON)
    ResponseEntity<?> getResult(@PathVariable("processId") String id, @PathVariable("jobId") String jobId)
            throws EngineException, ExecutionException;

    @GetMapping(value = BASE_URL + "/{processId}/jobs/{jobId}",
                produces = MediaTypes.APPLICATION_JSON)
    @ResponseBody
    StatusInfo getStatus(@PathVariable("processId") String processId, @PathVariable("jobId") String jobId)
            throws EngineException;

}
