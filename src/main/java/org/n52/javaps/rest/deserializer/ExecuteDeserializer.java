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
package org.n52.javaps.rest.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.javaps.rest.model.Input;
import org.n52.javaps.rest.model.Output;
import org.n52.javaps.rest.model.TransmissionMode;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
public class ExecuteDeserializer {
    private static final String VALUE_KEY = "value";
    private static final String INLINE_VALUE_KEY = "inlineValue";
    private static final String HREF_KEY = "href";
    private static final String FORMAT_KEY = "format";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String ENCODING_KEY = "encoding";
    private static final String SCHEMA_KEY = "schema";
    private static final Format FORMAT_TEXT_PLAIN = new Format("text/plain");
    private static final String BBOX_KEY = "bbox";
    private final ObjectMapper objectMapper;

    @Autowired
    public ExecuteDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    public List<OutputDefinition> readOutputs(List<Output> outputs) {
        return outputs.stream().map(output -> {
            OutputDefinition definition = new OutputDefinition();
            definition.setId(createId(output.getId()));
            org.n52.javaps.rest.model.Format format = output.getFormat();
            if (format == null) {
                definition.setFormat(Format.TEXT_PLAIN);
            } else {
                definition.setFormat(new Format(format.getMimeType(), format.getEncoding(), format.getSchema()));
            }
            definition.setDataTransmissionMode(getTransmissionMode(output.getTransmissionMode()));
            return definition;
        }).collect(toList());
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
        return new Format(object.path(MIME_TYPE_KEY).asText(),
                          object.path(ENCODING_KEY).asText(),
                          object.path(SCHEMA_KEY).asText());
    }

    private OwsCode createId(String id) {
        return new OwsCode(id);
    }

    public List<ProcessData> readInputs(List<Input> inputs) throws InputDecodingException {
        List<ProcessData> list = new ArrayList<>();
        for (Input input : inputs) {
            OwsCode id = createId(input.getId());
            try {
                list.add(readInput(id, input.getInput()));
            } catch (JsonProcessingException ex) {
                throw new InputDecodingException(id, ex);
            }
        }
        return list;
    }

    private ProcessData readInput(OwsCode id, JsonNode map) throws InputDecodingException, JsonProcessingException {
        JsonNode valueNode = map.path(VALUE_KEY);
        if (valueNode.isObject()) {
            ObjectNode value = (ObjectNode) valueNode;
            // complex data
            if (value.has(INLINE_VALUE_KEY)) {
                Format format = FORMAT_TEXT_PLAIN;
                if (map.has(FORMAT_KEY)) {
                    format = getFormat(map.get(FORMAT_KEY));
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
                    URI uri = new URI(value.get(HREF_KEY).toString());
                    Format format = getFormat(map.get(FORMAT_KEY));
                    return new ReferenceProcessData(id, format, uri);
                } catch (URISyntaxException e) {
                    throw new InputDecodingException(id, e);
                }
            }
        } else if (valueNode.isValueNode()) {
            return new StringValueProcessData(id, FORMAT_TEXT_PLAIN, valueNode.asText());

        } else if (map.path(BBOX_KEY).isObject()) {
            return new StringValueProcessData(id, new Format("application/json"),
                                              new ObjectMapper().writeValueAsString(map));
        }

        return null;
    }

}
