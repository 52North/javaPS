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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiParam;
import io.swagger.model.Execute;
import io.swagger.model.JobCollection;
import io.swagger.model.ProcessCollection;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.wps.*;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.wps.javaps.rest.deserializer.ExecuteDeserializer;
import org.n52.wps.javaps.rest.serializer.ExceptionSerializer;
import org.n52.wps.javaps.rest.serializer.ProcessSerializer;
import org.n52.wps.javaps.rest.serializer.ResultSerializer;
import org.n52.wps.javaps.rest.serializer.StatusInfoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toSet;

@Controller
@Configurable
public class ProcessesApiController implements ProcessesApi {

    private static final Logger log = LoggerFactory.getLogger(ProcessesApiController.class);
    private Engine engine;

//    private final ObjectMapper objectMapper;

    @Autowired
    ServletContext context;

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", baseURL + "/processes/");
    }

    private final HttpServletRequest request;

    @Autowired
    private ProcessSerializer processSerializer;
    @Autowired
    private StatusInfoSerializer statusInfoSerializer;
    @Autowired
    private ExceptionSerializer exceptionSerializer;
    @Autowired
    private ResultSerializer resultSerializer;
    @Autowired
    private ExecuteDeserializer executeDeserializer;

    @Autowired
    public ProcessesApiController(HttpServletRequest request) {
        this.request = request;
    }

    public ResponseEntity<?> execute(Execute body, String id) throws CodedException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        boolean syncExecute = false;

        for (String parameterName : parameterMap.keySet()) {
            if (parameterName.equalsIgnoreCase("sync-execute")) {
                String[] syncExecuteValues = parameterMap.get(parameterName);

                if (syncExecuteValues.length > 0) {
                    try {
                        syncExecute = Boolean.parseBoolean(syncExecuteValues[0]);
                    } catch (Exception e) {
                        //ignore
                        //TODO log 
                    }
                    break;
                } else {
                    break;
                }
            }
        }

        OwsCode owsCode = new OwsCode(id);

        List<ProcessData> inputs;
        try {
            inputs = executeDeserializer.readInputs(body.getInputs());
        } catch (URISyntaxException | JsonProcessingException e) {
            log.error("Could not resolve URI: ", e);
            throw new InvalidParameterValueException("identifier", owsCode.getValue());//TODO
        }

        List<OutputDefinition> outputs = executeDeserializer.readOutputs(body.getOutputs());

        JobId jobId;

        try {
            jobId = engine.execute(owsCode, inputs, outputs, ResponseMode.DOCUMENT);
        } catch (ProcessNotFoundException ex) {
            return exception(HttpStatus.NOT_FOUND, String.format("The process with id '%s' does not exist.", owsCode.getValue()), "Invalidparameter");
        } catch (InputDecodingException ex) {
            return exception(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "NoApplicableCode");
        }

        if (!syncExecute) {
            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.add("location", serviceURL + id + "/jobs/" + jobId.getValue());

            return new ResponseEntity<Void>(httpHeaders, HttpStatus.CREATED);
        } else {

            try {
                Future<Result> futureResult = engine.getResult(jobId);

                Result result = futureResult.get();

                return ResponseEntity.ok(resultSerializer.serializeResult(result));

            } catch (EngineException | InterruptedException | ExecutionException | IOException e) {
                return exception(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "NoApplicableCode");
            }

        }
    }

    public ResponseEntity<?> getJobList(@ApiParam(value = "The id of a process.", required = true) @PathVariable("id") String id) {
        OwsCode owsCode = new OwsCode(id);
        JobCollection jobCollection = new JobCollection();
        engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).forEach(jobCollection::addJobsItem);
        return ResponseEntity.ok(jobCollection);
    }

    public String getExecuteForm(@ApiParam(value = "The id of a process.", required = true) @PathVariable("id") String id, Model model) {
        OwsCode owsCode = new OwsCode(id);
        context.setAttribute("processId", id);
        context.setAttribute("jobSet", engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).collect(toSet()));
        return "../../../jsp/test_client";
    }

    public ResponseEntity<?> getProcessDescription(@ApiParam(value = "The id of a process", required = true) @PathVariable("id") String id) {
        OwsCode owsCode = new OwsCode(id);
        Optional<ProcessOffering> offering = engine.getProcessDescription(owsCode).map(ProcessOffering::new);
        if (!offering.isPresent()) {
            return exception(HttpStatus.NOT_FOUND, String.format("The process with id '%s' does not exist.", id), "Invalidparameter");
        }

        return ResponseEntity.ok(processSerializer.serializeProcessOffering(offering.get()));
    }

    public ResponseEntity<ProcessCollection> getProcesses() {

        Set<ProcessOffering> offerings = engine.getProcessDescriptions().stream().map(ProcessOffering::new).collect(toSet());

        return ResponseEntity.ok(processSerializer.createProcessCollection(offerings));
    }

    public ResponseEntity<?> getResult(@ApiParam(value = "The id of a process", required = true) @PathVariable("id") String id, @ApiParam(value = "The id of a job", required = true) @PathVariable("jobID") String jobID) {

        JobId jobId = new JobId(jobID);

        if (!engine.hasJob(jobId)) {
            return ResponseEntity.badRequest().body(exceptionSerializer.serializeException("NoSuchJob", String.format("Job with id %s not found.", jobId)));
        }

        try {

            Future<org.n52.shetland.ogc.wps.Result> futureResult = engine.getResult(jobId);

            if (!futureResult.isDone()) {
                return ResponseEntity.badRequest().body(exceptionSerializer.serializeException("ResultNotReady", String.format("Job with id %s not ready.", jobId)));
            }

            return ResponseEntity.ok(resultSerializer.serializeResult(futureResult.get()));
        } catch (EngineException | InterruptedException | ExecutionException | IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(exceptionSerializer.serializeException("NoApplicableCode", "Internal server error."));
        }
    }

    public ResponseEntity<?> getStatus(@ApiParam(value = "The id of a process", required = true) @PathVariable("id") String id, @ApiParam(value = "The id of a job", required = true) @PathVariable("jobID") String jobID) {

        JobId jobId = new JobId(jobID);

        if (!engine.hasJob(jobId)) {

            return ResponseEntity.badRequest().body(exceptionSerializer.serializeException("NoSuchJob", String.format("Job with id %s not found.", jobId)));
        }

        try {

            org.n52.shetland.ogc.wps.StatusInfo status = engine.getStatus(jobId);

            return ResponseEntity.ok(statusInfoSerializer.serialize(status, id, jobID));

        } catch (EngineException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(exceptionSerializer.serializeException("NoApplicableCode", "Internal server error."));
        }
    }

    private ResponseEntity<io.swagger.model.Exception> exception(HttpStatus httpStatusCode, String description, String exceptionCode) {
        return ResponseEntity.status(httpStatusCode).body(exceptionSerializer.serializeException(exceptionCode, description));
    }

}
