/*
 * Copyright (C) 2020 by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.javaps.rest.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.n52.javaps.rest.model.OutputInfo;
import org.n52.javaps.rest.model.Result;
import org.n52.javaps.rest.model.ValueType;
import org.n52.javaps.rest.model.InlineValue;
import org.n52.javaps.rest.model.ReferenceValue;
import org.apache.commons.io.IOUtils;
import org.n52.javaps.engine.OutputEncodingException;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResultSerializer extends AbstractSerializer {

    private static final Logger log = LoggerFactory.getLogger(ResultSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Result serializeResult(org.n52.shetland.ogc.wps.Result result) throws OutputEncodingException {
        return new Result().outputs(getOutputInfos(result.getOutputs()));
    }

    private List<OutputInfo> getOutputInfos(List<ProcessData> outputs) throws OutputEncodingException {
        List<OutputInfo> outputInfos = new ArrayList<>();
        for (ProcessData processData : outputs) {
            try {
                outputInfos.add(createOutput(processData));
            } catch (IOException | IllegalArgumentException e) {
                throw new OutputEncodingException(processData.getId(), e);
            }
        }
        return outputInfos;
    }

    private OutputInfo createOutput(ProcessData processData) throws IOException {
        OutputInfo output = new OutputInfo();
        output.setId(processData.getId().getValue());
        output.setValue(createValueType(processData));
        return output;
    }

    private ValueType createValueType(ProcessData processData) throws IOException {
        if (processData.isReference()) {
            return createReferenceValue(processData.asReference());
        } else if (processData.isValue()) {
            return createInlineValue(processData.asValue());
        } else if (processData.isGroup()) {
            throw new IllegalArgumentException("group outputs are not supported");
        } else {
            throw new IllegalArgumentException("outputs type not supported: " + processData);
        }
    }

    private ValueType createInlineValue(ValueProcessData valueProcessData) throws IOException {
        try {

            return new InlineValue().inlineValue(objectMapper.readTree(valueProcessData.getData()));
        } catch (JsonParseException e) {
            log.info("Could not read value as JSON node.");
            StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            try {
                IOUtils.copy(valueProcessData.getData(), writer, encoding);
            } catch (IOException e1) {
                throw e;
            }
            return new InlineValue().inlineValue(writer.toString());
        }
    }

    private ValueType createReferenceValue(ReferenceProcessData referenceProcessData) {
        return new ReferenceValue().href(referenceProcessData.getURI().toString());
    }

}
