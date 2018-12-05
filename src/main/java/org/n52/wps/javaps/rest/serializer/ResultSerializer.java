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
package org.n52.wps.javaps.rest.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.OutputInfo;
import io.swagger.model.Result;
import io.swagger.model.ValueType;

public class ResultSerializer {

    private static final Logger log = LoggerFactory.getLogger(ResultSerializer.class);

    public static Result serializeResult(org.n52.shetland.ogc.wps.Result result) throws IOException {

        Result serializedResult = new Result();

        List<OutputInfo> outputs = new ArrayList<>();

        Iterator<ProcessData> processResults = result.getOutputs().iterator();

        while (processResults.hasNext()) {
            ProcessData processData = (ProcessData) processResults.next();

            OutputInfo output = new OutputInfo();

            output.setId(processData.getId().getValue());

            ValueType valueType = new ValueType();

            if (processData.isReference()) {
                valueType.setHref(processData.asReference().getURI().toString());
            } else if (processData.isValue()) {

                try {
                    JsonNode object = new ObjectMapper().readTree(processData.asValue().getData());
                    valueType.setInlineValue(object);
                } catch (Exception e) {
                    log.info("Could not read value as JSON node.");
                    StringWriter writer = new StringWriter();
                    String encoding = StandardCharsets.UTF_8.name();
                    try {
                        IOUtils.copy(processData.asValue().getData(), writer, encoding);
                    } catch (IOException e1) {
                        throw e;
                    }
                    valueType.setInlineValue(writer.toString());
                }

            }

            output.setValue(valueType);

            outputs.add(output);
        }

        serializedResult.setOutputs(outputs);

        return serializedResult;

    }

}
