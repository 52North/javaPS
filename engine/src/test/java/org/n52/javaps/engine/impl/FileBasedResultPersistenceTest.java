/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.engine.impl;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.n52.javaps.engine.EngineException;
import org.n52.javaps.engine.EngineProcessExecutionContext;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.JobStatus;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;

public class FileBasedResultPersistenceTest {

    @Test
    public void testSaveBase64() {

        OwsCode processId = new OwsCode("FileBasedResultPersistenceTestProcess");
        OwsCode outputId = new OwsCode("FileBasedResultPersistenceTestOutput");
        JobId jobId = new JobId("javaps-unit-test-" + UUID.randomUUID().toString().substring(0, 5));

        FileBasedResultPersistence fileBasedResultPersistence = new FileBasedResultPersistence();

        try {
            fileBasedResultPersistence.setBasePath(File.createTempFile("javaps-unit-test", ".tmp").getParentFile());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        EngineProcessExecutionContext context = Mockito.mock(EngineProcessExecutionContext.class);

        Mockito.when(context.getProcessId()).thenReturn(processId);
        Mockito.when(context.getJobStatus()).thenReturn(JobStatus.succeeded());
        Mockito.when(context.getResponseMode()).thenReturn(ResponseMode.DOCUMENT);
        Mockito.when(context.getJobId()).thenReturn(jobId);

        OutputDefinition outputDefinition = Mockito.mock(OutputDefinition.class);

        Mockito.when(outputDefinition.getDataTransmissionMode()).thenReturn(DataTransmissionMode.VALUE);

        Map<OwsCode, OutputDefinition> outputDefinitions = new HashMap<>();

        outputDefinitions.put(outputId, outputDefinition);

        Mockito.when(context.getOutputDefinitions()).thenReturn(outputDefinitions);

        List<ProcessData> encodedOutputs = new ArrayList<>();

        ValueProcessData output = Mockito.mock(GeneratingProcessData.class);

        Mockito.when(output.getId()).thenReturn(outputId);
        Mockito.when(output.isValue()).thenReturn(true);
        Mockito.when(output.asValue()).thenReturn(output);

        Mockito.when(output.getFormat()).thenReturn(new Format("image/png", "base64"));

        InputStream in = getClass().getClassLoader().getResourceAsStream("wps1266352013022967603.dat");

        try {
            Mockito.when(output.getData()).thenReturn(in);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        encodedOutputs.add(output);

        try {
            Mockito.when(context.getEncodedOutputs()).thenReturn(encodedOutputs);
        } catch (Throwable e) {
            fail(e.getMessage());
        }

        fileBasedResultPersistence.save(context);

        try {
            InputStream result = fileBasedResultPersistence.getResult(jobId).getOutputs().get(0).asValue().getData();

            StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            IOUtils.copy(result, writer, encoding);

            Base64.getDecoder().decode(writer.toString());

        } catch (EngineException | IOException e) {
            fail(e.getMessage());
        }

    }

}
