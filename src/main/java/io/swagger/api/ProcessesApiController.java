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

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
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
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiParam;
import io.swagger.model.Execute;
import io.swagger.model.JobCollection;
import io.swagger.model.ProcessCollection;
import io.swagger.model.StatusInfo;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")

@Controller
@Configurable
public class ProcessesApiController implements ProcessesApi {

    private static final Logger log = LoggerFactory.getLogger(ProcessesApiController.class);
    private Engine engine;

//    private final ObjectMapper objectMapper;

    @Autowired
    ServletContext context;
    
    @Inject
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
    
    @Inject
    private ProcessSerializer processSerializer;
    
    @Inject
    private StatusInfoSerializer statusInfoSerializer;
    
//    @Inject
//    private ExceptionSerializer exceptionSerializer;

    @org.springframework.beans.factory.annotation.Autowired
    public ProcessesApiController(HttpServletRequest request) {
        this.request = request;
    }

    public ResponseEntity<?> execute(@ApiParam(value = "Mandatory execute request JSON" ,required=true )  @Valid @RequestBody Execute body,@ApiParam(value = "The id of a process.",required=true) @PathVariable("id") String id) throws CodedException {
        String accept = request.getHeader("Accept");        
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        boolean syncExecute = false;
        
        for (String parameterName : parameterMap.keySet()) {
            if(parameterName.equalsIgnoreCase("sync-execute")) {
                String[] syncExecuteValues = parameterMap.get(parameterName);
                
                if(syncExecuteValues.length > 0) {
                    try {
                        syncExecute = Boolean.parseBoolean(syncExecuteValues[0]);
                    } catch (Exception e) {
                        //ignore
                        //TODO log 
                    }                    
                    break;
                }else {
                    break;
                }
            }
        }
        
        OwsCode owsCode = new OwsCode(id);
        
        List<ProcessData> inputs;
        try {
            inputs = ExecuteDeserializer.readInputs(body.getInputs());
        } catch (URISyntaxException | JsonProcessingException e) {
            log.error("Could not resolve URI: ", e);
            throw new InvalidParameterValueException("identifier", owsCode.getValue());//TODO
        }
        
        List<OutputDefinition> outputs = ExecuteDeserializer.readOutputs(body.getOutputs());
        
        JobId jobId = null;
        
        try {
            jobId = engine.execute(owsCode, inputs, outputs, ResponseMode.DOCUMENT);
        } catch (ProcessNotFoundException ex) {            
            return exception(404, String.format("The process with id '%s' does not exist.", owsCode.getValue()), "Invalidparameter");
//            throw new InvalidParameterValueException("identifier", owsCode.getValue());
        } catch (InputDecodingException ex) {
            return exception(500, ex.getMessage(), "NoApplicableCode");
        }
        
        if (!syncExecute) {
            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.add("location", serviceURL + id + "/jobs/" + jobId.getValue());

            ResponseEntity<Void> responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);

            return responseEntity;
        }else {
            
            try {
                Future<Result> futureResult = engine.getResult(jobId);
                
                Result result = futureResult.get();
                
                return ResponseEntity.ok(ResultSerializer.serializeResult(result));
                
            } catch (EngineException | InterruptedException | ExecutionException | IOException e) {
                return exception(500, e.getMessage(), "NoApplicableCode");
            }
            
        }
    }

    public ResponseEntity<?> getJobList(@ApiParam(value = "The id of a process.",required=true) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        
        OwsCode owsCode = new OwsCode(id);
        
        Set<String> values = engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).collect(toSet());
        
        JobCollection jobCollection = new JobCollection();        
        
        for (String jobID : values) {
            jobCollection.addJobsItem(jobID);
        }
        
        return ResponseEntity.ok(jobCollection);
    }
    
    public String getExecuteForm(@ApiParam(value = "The id of a process.",required=true) @PathVariable("id") String id, Model model) {
        String accept = request.getHeader("Accept");    
        
        OwsCode owsCode = new OwsCode(id);    

        context.setAttribute("processId", id);
        
        context.setAttribute("jobSet", engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).collect(toSet()));
        
        return "../../../jsp/test_client";
    }

    public ResponseEntity<?> getProcessDescription(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        
        OwsCode owsCode = new OwsCode(id);
        
        Optional<ProcessOffering> offering = engine.getProcessDescription(owsCode).map(ProcessOffering::new);
        
        if(!offering.isPresent()) {
            
            return exception(404, String.format("The process with id '%s' does not exist.", id), "Invalidparameter");
            
//            io.swagger.model.Exception  = new io.swagger.model.Exception();
//            exception.setCode("Invalidparameter");
//            exception.setDescription();
//            
//            ResponseEntity<io.swagger.model.Exception> response = new ResponseEntity<io.swagger.model.Exception>(exception, HttpStatus.valueOf(404));
//            return response;
            
        }
        
        return ResponseEntity.ok(processSerializer.serializeProcessOffering(offering.get()));
    }

    public ResponseEntity<ProcessCollection> getProcesses() {
        String accept = request.getHeader("Accept");
        
        Set<ProcessOffering> offerings = engine.getProcessDescriptions().stream().map(ProcessOffering::new).collect(toSet());
        
        return ResponseEntity.ok(processSerializer.serializeProcessOfferings(offerings));
    }

    public ResponseEntity<?> getResult(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id,@ApiParam(value = "The id of a job",required=true) @PathVariable("jobID") String jobID) {
        
        JobId jobId = new JobId(jobID);
        
        if(!engine.hasJob(jobId)) {
            
            return ResponseEntity.badRequest().body(ExceptionSerializer.serializeException("NoSuchJob", String.format("Job with id %s not found.", jobId)));
        }
            
        try {
            
            Future<org.n52.shetland.ogc.wps.Result> futureResult = engine.getResult(jobId);
            
            if(!futureResult.isDone()) {
                return ResponseEntity.badRequest().body(ExceptionSerializer.serializeException("ResultNotReady", String.format("Job with id %s not ready.", jobId)));
            }
            
            return ResponseEntity.ok(ResultSerializer.serializeResult(futureResult.get()));
        } catch (EngineException | InterruptedException | ExecutionException | IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ExceptionSerializer.serializeException("NoApplicableCode", "Internal server error."));
        }
    }

    public ResponseEntity<?> getStatus(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id,@ApiParam(value = "The id of a job",required=true) @PathVariable("jobID") String jobID) {
        String accept = request.getHeader("Accept");
        
        JobId jobId = new JobId(jobID);
        
        if(!engine.hasJob(jobId)) {
            
            return ResponseEntity.badRequest().body(ExceptionSerializer.serializeException("NoSuchJob", String.format("Job with id %s not found.", jobId)));
        }
        
        try {
            
            org.n52.shetland.ogc.wps.StatusInfo status = engine.getStatus(jobId);
            
            return ResponseEntity.ok(statusInfoSerializer.serialize(status, id, jobID));
            
        } catch (EngineException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(ExceptionSerializer.serializeException("NoApplicableCode", "Internal server error."));
        }
    }
    
    private ResponseEntity<io.swagger.model.Exception> exception(int httpStatusCode, String description, String exceptionCode){
        io.swagger.model.Exception exception = new io.swagger.model.Exception();
        exception.setCode(exceptionCode);
        exception.setDescription(description);
        return new ResponseEntity<io.swagger.model.Exception>(exception, HttpStatus.valueOf(httpStatusCode));
    }

}
