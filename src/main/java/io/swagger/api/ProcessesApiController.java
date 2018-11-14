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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.print.attribute.standard.JobImpressionsCompleted;
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
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.wps.javaps.rest.deserializer.ExecuteDeserializer;
import org.n52.wps.javaps.rest.serializer.ProcessSerializer;
import org.n52.wps.javaps.rest.serializer.StatusInfoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiParam;
import io.swagger.model.Execute;
import io.swagger.model.Input;
import io.swagger.model.JobCollection;
import io.swagger.model.Output;
import io.swagger.model.Process;
import io.swagger.model.ProcessCollection;
import io.swagger.model.ProcessSummary;
import io.swagger.model.Result;
import io.swagger.model.StatusInfo;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-11-08T09:36:12.450Z[GMT]")

@Controller
@Configurable
public class ProcessesApiController implements ProcessesApi {

    private static final Logger log = LoggerFactory.getLogger(ProcessesApiController.class);
    private Engine engine;

//    private final ObjectMapper objectMapper;

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
        this.serviceURL = url.replace("/service", "/processes/");
    }
    
    private final HttpServletRequest request;
    
    @Inject
    private ProcessSerializer processSerializer;

    @org.springframework.beans.factory.annotation.Autowired
    public ProcessesApiController(HttpServletRequest request) {
//    	public ProcessesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
//        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> execute(@ApiParam(value = "Mandatory execute request JSON" ,required=true )  @Valid @RequestBody Execute body,@ApiParam(value = "The id of a process.",required=true) @PathVariable("id") String id) throws CodedException {
        String accept = request.getHeader("Accept");
        
        OwsCode owsCode = new OwsCode(id);
        
        //read execute JSON
//        body.getInputs().g
        
        List<ProcessData> inputs;
        try {
            inputs = ExecuteDeserializer.readInputs(body.getInputs());
        } catch (URISyntaxException e) {
            log.error("Could not resolve URI: ", e);
            throw new InvalidParameterValueException("identifier", owsCode.getValue());//TODO
        }
        
        List<OutputDefinition> outputs = ExecuteDeserializer.readOutputs(body.getOutputs());
        
        JobId jobId = null;
        
        try {
            jobId = engine.execute(owsCode, inputs, outputs, ResponseMode.DOCUMENT);
        } catch (ProcessNotFoundException ex) {
            throw new InvalidParameterValueException("identifier", owsCode.getValue());
        } catch (InputDecodingException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }
        
        HttpHeaders httpHeaders = new HttpHeaders();
        
        httpHeaders.add("location", serviceURL + id + "/jobs/" + jobId.getValue());
        
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        
        return responseEntity;
    }

    public ResponseEntity<JobCollection> getJobList(@ApiParam(value = "The id of a process.",required=true) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        
        OwsCode owsCode = new OwsCode(id);
        
        Set<String> values = engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).collect(toSet());
        
        JobCollection jobCollection = new JobCollection();        
        
        for (String jobID : values) {
            jobCollection.addJobsItem(jobID);
        }
        
        return ResponseEntity.ok(jobCollection);
    }

    public ResponseEntity<io.swagger.model.ProcessOffering> getProcessDescription(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id) {
        String accept = request.getHeader("Accept");
        
        OwsCode owsCode = new OwsCode(id);
        
        ProcessOffering offering = engine.getProcessDescription(owsCode).map(ProcessOffering::new).get();
        
//        io.swagger.model.ProcessOffering processOffering = new io.swagger.model.ProcessOffering();
//        
//        Process process = new Process();
//        
//        process.setId(id);
//        
//        process.setExecuteEndpoint(serviceURL + id + "/jobs");
//        
//        processOffering.process(process);
        
        return ResponseEntity.ok(processSerializer.serializeProcessOffering(offering));
    }

    public ResponseEntity<ProcessCollection> getProcesses() {
        String accept = request.getHeader("Accept");
        
        Set<ProcessOffering> offerings = engine.getProcessDescriptions().stream().map(ProcessOffering::new).collect(toSet());
        
        ProcessSummary process;
        
//        process.setJobControlOptions(jobControlOptions);
        
        ProcessCollection collection = new ProcessCollection();
        
        List<ProcessSummary> processes = new ArrayList<>();
        
        for (ProcessOffering processOffering : offerings) {
            process = new ProcessSummary();
            
            String id = processOffering.getProcessDescription().getId().getValue();
            
            process.setId(id);
            
            process.setProcessDescriptionURL(serviceURL + id);
            
            processes.add(process);
            
        }
        
        collection.setProcesses(processes);
        
        return ResponseEntity.ok(collection);
    }

    public ResponseEntity<Result> getResult(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id,@ApiParam(value = "The id of a job",required=true) @PathVariable("jobID") String jobID) throws InvalidParameterValueException {
        
        JobId jobId = new JobId(jobID);
        
        try {
            
            Future<org.n52.shetland.ogc.wps.Result> futureResult = engine.getResult(jobId);
            
            if(!futureResult.isDone()) {
                throw new InvalidParameterValueException("JobId", jobId.getValue());
            }
                
        } catch (EngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<Result>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<StatusInfo> getStatus(@ApiParam(value = "The id of a process",required=true) @PathVariable("id") String id,@ApiParam(value = "The id of a job",required=true) @PathVariable("jobID") String jobID) {
        String accept = request.getHeader("Accept");
        
        JobId jobId = new JobId(jobID);
        
        try {
            
            org.n52.shetland.ogc.wps.StatusInfo status = engine.getStatus(jobId);
            
            return ResponseEntity.ok(StatusInfoSerializer.serialize(status));
            
        } catch (EngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return new ResponseEntity<StatusInfo>(HttpStatus.NOT_IMPLEMENTED);
    }

}
