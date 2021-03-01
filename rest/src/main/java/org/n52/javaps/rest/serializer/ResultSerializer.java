/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.n52.javaps.engine.OutputEncodingException;
import org.n52.javaps.rest.model.Format;
import org.n52.javaps.rest.model.InlineOrRefData;
import org.n52.javaps.rest.model.Result;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ResultSerializer extends AbstractSerializer {

    private static final Logger log = LoggerFactory.getLogger(ResultSerializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean isRaw;

    public Object serializeResult(org.n52.shetland.ogc.wps.Result result) throws OutputEncodingException {
        isRaw = result.getResponseMode().equals(ResponseMode.RAW);
        if (isRaw) {
            // if (result.getOutputs().size() > 1) {
            // TODO throw exception
            // io.swagger.model.Exception exception = new
            // io.swagger.model.Exception();
            // exception.setCode(code);
            // return exception;
            // }
            for (ProcessData processData : result.getOutputs()) {
                if (processData.isValue()) {
                    try {
                        createRawOutput(processData.asValue());
                    } catch (IOException | IllegalArgumentException e) {
                        throw new OutputEncodingException(processData.getId(), e);
                    }
                }
            }
        }
        return createResult(result.getOutputs(), isRaw);
    }

    private Result createResult(List<ProcessData> outputs,
            boolean isRaw) {

        Result result = new Result();

        for (ProcessData processData : outputs) {
            String id = processData.getId().getValue();
            if (processData.isValue()) {
                ValueProcessData valueProcessData = processData.asValue();
                try {
                    Object value = createRawOutput(valueProcessData);
                    if (value instanceof ObjectNode) {
                        ObjectNode valueAsObjectNode = (ObjectNode) value;
                        if (!valueAsObjectNode.findPath("bbox").isMissingNode()) {
                            // bbox data, simply put id and value in result
                            // hashmap
                            result.put(id, value);
                        } else {
                            result.put(id, createInlineOrRefData(valueProcessData));
                        }
                    } else {
                        result.put(id, createInlineOrRefData(valueProcessData));
                    }
                } catch (IOException e) {
                    log.error("Could not create output with id: " + id, e);
                }

            } else if (processData.isReference()) {
                return createReferenceValue(processData.asReference());
            }
        }

        return result;
    }

    private Object createInlineOrRefData(ValueProcessData valueProcessData) {
        InlineOrRefData result = new InlineOrRefData();
        result.setFormat(transformFormat(valueProcessData.getFormat()));
        return result;
    }

    private Format transformFormat(org.n52.shetland.ogc.wps.Format format) {
        Format result = new Format();
        if (format.getMimeType().isPresent()) {
            result.setMimeType(format.getMimeType().get());
        }
        if (format.getSchema().isPresent()) {
            result.setSchema(format.getSchema().get());
        }
        if (format.getEncoding().isPresent()) {
            result.setEncoding(format.getEncoding().get());
        }
        return result;
    }

    private Result createReferenceValue(ReferenceProcessData referenceProcessData) {
        Result result = new Result();
        InlineOrRefData inlineOrRefData = new InlineOrRefData();
        inlineOrRefData.setHref(referenceProcessData.getURI().toString());
        result.put(referenceProcessData.getId().getValue(), inlineOrRefData);
        return result;
    }

    private Object createRawOutput(ValueProcessData valueProcessData) throws IOException {
        try {
            return objectMapper.readTree(valueProcessData.getData());
        } catch (JsonParseException e) {
            log.info("Could not read value as JSON node.");
            StringWriter writer = new StringWriter();
            String encoding = StandardCharsets.UTF_8.name();
            try {
                IOUtils.copy(valueProcessData.getData(), writer, encoding);
            } catch (IOException e1) {
                throw e;
            }
            return writer.toString();
        }
    }

}
