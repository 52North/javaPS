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

import com.google.common.base.Throwables;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.rest.deserializer.ExecuteDeserializer;
import org.n52.javaps.rest.model.Execute;
import org.n52.javaps.rest.model.JobCollection;
import org.n52.javaps.rest.model.ProcessCollection;
import org.n52.javaps.rest.model.StatusInfo;
import org.n52.javaps.rest.serializer.ExceptionSerializer;
import org.n52.javaps.rest.serializer.ProcessSerializer;
import org.n52.javaps.rest.serializer.ResultSerializer;
import org.n52.javaps.rest.serializer.StatusInfoSerializer;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toSet;

@Controller
@Configurable
public final class ProcessesApiImpl implements ProcessesApi {
    private Engine engine;
    private String serviceURL;
    private ServletContext context;
    private HttpServletRequest request;
    private ProcessSerializer processSerializer;
    private StatusInfoSerializer statusInfoSerializer;
    private ExceptionSerializer exceptionSerializer;
    private ResultSerializer resultSerializer;
    private ExecuteDeserializer executeDeserializer;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setContext(ServletContext context) {
        this.context = context;
    }

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Autowired
    public void setExceptionSerializer(ExceptionSerializer exceptionSerializer) {
        this.exceptionSerializer = exceptionSerializer;
    }

    @Autowired
    public void setProcessSerializer(ProcessSerializer processSerializer) {
        this.processSerializer = processSerializer;
    }

    @Autowired
    public void setExecuteDeserializer(ExecuteDeserializer executeDeserializer) {
        this.executeDeserializer = executeDeserializer;
    }

    @Autowired
    public void setResultSerializer(ResultSerializer resultSerializer) {
        this.resultSerializer = resultSerializer;
    }

    @Autowired
    public void setStatusInfoSerializer(StatusInfoSerializer statusInfoSerializer) {
        this.statusInfoSerializer = statusInfoSerializer;
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", ProcessesApi.BASE_URL);
    }

    public ResponseEntity<?> execute(Execute body, String id)
            throws EngineException {

        boolean syncExecute = request.getParameterMap()
                                     .entrySet().stream()
                                     .filter(e -> e.getKey().equalsIgnoreCase("sync-execute"))
                                     .map(Map.Entry::getValue)
                                     .filter(v -> v.length > 0)
                                     .map(v -> v[0])
                                     .findFirst()
                                     .map(Boolean::parseBoolean)
                                     .orElse(false);
        OwsCode owsCode = new OwsCode(id);

        List<ProcessData> inputs = executeDeserializer.readInputs(body.getInputs());
        List<OutputDefinition> outputs = executeDeserializer.readOutputs(body.getOutputs());

        JobId jobId = engine.execute(owsCode, inputs, outputs, ResponseMode.DOCUMENT);

        if (syncExecute) {
            try {
                Future<Result> future = engine.getResult(jobId);
                Result result = future.get();
                return ResponseEntity.ok(resultSerializer.serializeResult(result));
            } catch (InterruptedException e) {
                throw new EngineException(e);
            } catch (ExecutionException e) {
                Throwables.throwIfInstanceOf(e.getCause(), EngineException.class);
                throw new EngineException(e.getCause());
            }
        } else {
            String uri = String.format("%s/%s/jobs/%s", serviceURL, id, jobId.getValue());
            return ResponseEntity.created(URI.create(uri)).build();
        }
    }

    public ResponseEntity<?> getJobList(String id) {
        OwsCode owsCode = new OwsCode(id);
        JobCollection jobCollection = new JobCollection();
        engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue).forEach(jobCollection::addJobsItem);
        return ResponseEntity.ok(jobCollection);
    }

    public String getExecuteForm(String id, Model model) {
        OwsCode owsCode = new OwsCode(id);
        context.setAttribute("processId", id);
        context.setAttribute("jobSet", engine.getJobIdentifiers(owsCode).stream().map(JobId::getValue)
                                             .collect(toSet()));
        return "../../../jsp/test_client";
    }

    public org.n52.javaps.rest.model.ProcessOffering getProcessDescription(String id) throws ProcessNotFoundException {
        OwsCode owsCode = new OwsCode(id);
        return engine.getProcessDescription(owsCode)
                     .map(ProcessOffering::new)
                     .map(processSerializer::serializeProcessOffering)
                     .orElseThrow(() -> new ProcessNotFoundException(owsCode));
    }

    public ProcessCollection getProcesses() {
        Set<ProcessOffering> offerings = engine.getProcessDescriptions().stream().map(ProcessOffering::new)
                                               .collect(toSet());
        return processSerializer.createProcessCollection(offerings);
    }

    public ResponseEntity<?> getResult(String processID, String jobID) throws EngineException {

        JobId jobId = new JobId(jobID);
        OwsCode processId = new OwsCode(processID);

        if (!engine.hasProcessDescription(processId)) {
            throw new ProcessNotFoundException(processId);
        }

        if (!engine.hasJob(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        try {

            Future<org.n52.shetland.ogc.wps.Result> futureResult = engine.getResult(jobId);

            if (!futureResult.isDone()) {
                return ResponseEntity.badRequest()
                                     .body(exceptionSerializer.serializeException(
                                             "ResultNotReady", String.format("Job with id %s not ready.", jobId)));
            }

            return ResponseEntity.ok(resultSerializer.serializeResult(futureResult.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new EngineException(e);
        }
    }

    public StatusInfo getStatus(String processId, String jobID) throws EngineException {

        OwsCode identifier = new OwsCode(processId);
        if (!engine.hasProcessDescription(identifier)) {
            throw new ProcessNotFoundException(identifier);
        }
        JobId jobId = new JobId(jobID);
        if (!engine.hasJob(jobId)) {
            throw new JobNotFoundException(jobId);
        }
        return statusInfoSerializer.serialize(engine.getStatus(jobId), processId, jobID);

    }
}
