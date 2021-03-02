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
package org.n52.javaps.rest.deserializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.rest.model.Input;
import org.n52.javaps.rest.model.Output;
import org.n52.javaps.rest.model.TransmissionMode;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ExecuteDeserializer {
    private static final Logger log = LoggerFactory.getLogger(ExecuteDeserializer.class);
    private static final String VALUE_KEY = "value";
    private static final String INLINE_VALUE_KEY = "inlineValue";
    private static final String HREF_KEY = "href";
    private static final String FORMAT_KEY = "format";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String ENCODING_KEY = "encoding";
    private static final String SCHEMA_KEY = "schema";
    private static final Format FORMAT_TEXT_PLAIN = new Format("text/plain");
    private static final String BBOX_KEY = "bbox";
    private static final String REFERENCE_VALUE_KEY = "referenceValue";
    private final ObjectMapper objectMapper;

    @Autowired
    public ExecuteDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    public List<OutputDefinition> readOutputs(Output outputs) {
//        if (outputs == null) {
            return Collections.emptyList();
//        }
//        return outputs.stream().map(output -> {
//            OutputDefinition definition = new OutputDefinition();
//            definition.setId(createId(output.getId()));
//            org.n52.javaps.rest.model.Format format = output.getFormat();
//            if (format != null) {
//                definition.setFormat(new Format(format.getMimeType(), format.getEncoding(), format.getSchema()));
//            }
//            definition.setDataTransmissionMode(getTransmissionMode(output.getTransmissionMode()));
//            return definition;
//        }).collect(toList());
    }

    private DataTransmissionMode getTransmissionMode(TransmissionMode transmissionMode) {
        switch (transmissionMode) {
            case VALUE:
                return DataTransmissionMode.VALUE;
            case REFERENCE:
            default:
                return DataTransmissionMode.REFERENCE;
        }
    }

    private Format getFormat(JsonNode object) {
        return new Format(object.path(MIME_TYPE_KEY).asText(), object.path(ENCODING_KEY).asText(),
                object.path(SCHEMA_KEY).asText());
    }

    private OwsCode createId(String id) {
        return new OwsCode(id);
    }

    public List<ProcessData> readInputs(Input inputs) throws InputDecodingException {
        List<ProcessData> list = new ArrayList<>();
        if (inputs == null) {
            return list;
        }
        for (Entry<String, Object> input : inputs.entrySet()) {
            String idString = input.getKey();
            Object value = input.getValue();
            OwsCode id = createId(idString);
            try {
                list.add(readInput(id, value));
            } catch (JsonProcessingException ex) {
                throw new InputDecodingException(id, ex);
            }

        }
        return list;
    }

    private ProcessData readInput(OwsCode id, Object object) throws InputDecodingException, JsonProcessingException {
        JsonNode inputNode = null;
        if (object instanceof JsonNode) {
            inputNode = (JsonNode) object;
        } else {
            log.error("Input not instance of JsonNode.");
            return null;
        }
        JsonNode valueNode = inputNode.path(VALUE_KEY);
        if (valueNode.isObject()) {
            ObjectNode value = (ObjectNode) valueNode;
            // complex data
            if (value.has(INLINE_VALUE_KEY)) {
                Format format = FORMAT_TEXT_PLAIN;
                if (inputNode.has(FORMAT_KEY)) {
                    format = getFormat(inputNode.get(FORMAT_KEY));
                }
                String stringValue;
                JsonNode inlineValue = value.path(INLINE_VALUE_KEY);
                if (inlineValue.isValueNode()) {
                    stringValue = inlineValue.asText();
                } else if (inlineValue.isNull()) {
                    stringValue = "";
                } else {
                    stringValue = objectMapper.writeValueAsString(inlineValue);
                }
                return new StringValueProcessData(id, format, stringValue);
            } else if (value.has(HREF_KEY)) {
                try {
                    URI uri = new URI(value.get(HREF_KEY).asText());
                    Format format = null;
                    JsonNode formatNode = inputNode.get(FORMAT_KEY);

                    if (formatNode != null) {
                        format = getFormat(formatNode);
                    } else {
                        log.info("Could not get format.");
                    }
                    return new ReferenceProcessData(id, format, uri);
                } catch (URISyntaxException e) {
                    throw new InputDecodingException(id, e);
                }
            }
        } else if (valueNode.isValueNode()) {
            return new StringValueProcessData(id, FORMAT_TEXT_PLAIN, valueNode.asText());

        } else if (inputNode.path(BBOX_KEY).isArray()) {
            return new StringValueProcessData(id, new Format("application/json"),
                    new ObjectMapper().writeValueAsString(object));
        }

        return null;
    }

}
