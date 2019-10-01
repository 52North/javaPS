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

import com.google.common.io.ByteStreams;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.JobNotFoundException;
import org.n52.javaps.engine.OutputNotFoundException;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
public final class ProcessesApiExtensionImpl implements ProcessesApiExtension {
    private final Engine engine;

    @Autowired
    private ProcessesApiExtensionImpl(Engine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    @Override
    public ResponseEntity<?> getOutput(String processID, String jobID, String outputID) throws EngineException {
        JobId jobId = new JobId(jobID);
        OwsCode processId = new OwsCode(processID);
        OwsCode outputId = new OwsCode(outputID);

        if (!engine.hasProcessDescription(processId)) {
            throw new ProcessNotFoundException(processId);
        }

        if (!engine.hasJob(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        try {
            Future<Result> future = engine.getResult(jobId);
            if (!future.isDone()) {
                throw new OutputNotFoundException();
            }
            ProcessData output = future.get().getOutputs().stream()
                                       .filter(x -> x.getId().equals(outputId)).findFirst()
                                       .orElseThrow(OutputNotFoundException::new);
            return getResponseEntity(output);
        } catch (InterruptedException | ExecutionException e) {
            throw new OutputNotFoundException(e);
        }
    }

    private ResponseEntity<?> getResponseEntity(ProcessData output) throws EngineException {
        if (output.isReference()) {
            return ResponseEntity.status(HttpStatus.FOUND).location(output.asReference().getURI()).build();
        } else if (output.isGroup()) {
            throw new EngineException("Grouped outputs are not supported");
        } else if (output.isValue()) {
            final ValueProcessData valueProcessData = output.asValue();
            byte[] value;

            try (InputStream data = valueProcessData.getData();
                 ByteArrayOutputStream out = new ByteArrayOutputStream();) {
                ByteStreams.copy(data, out);
                value = out.toByteArray();
            } catch (IOException ex) {
                throw new EngineException(ex);
            }
            ByteArrayResource inputStreamResource = new ByteArrayResource(value);
            ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
            valueProcessData.getFormat().getMimeType().map(MediaType::valueOf).ifPresent(builder::contentType);
            return builder.contentLength(value.length).body(inputStreamResource);
        } else {
            throw new EngineException("Unsupported output type " + output);
        }
    }
}
