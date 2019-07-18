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
package org.n52.wps.javaps.rest.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Input;
import io.swagger.model.Output;
import io.swagger.model.TransmissionMode;
import org.n52.javaps.io.bbox.json.JSONBoundingBoxInputOutputHandler;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ExecuteDeserializer {
    private static final String VALUE_KEY = "value";
    private static final String INLINE_VALUE_KEY = "inlineValue";
    private static final String HREF_KEY = "href";
    private static final String FORMAT_KEY = "format";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String ENCODING_KEY = "encoding";
    private static final String SCHEMA_KEY = "schema";
    private static final Format FORMAT_TEXT_PLAIN = new Format("text/plain");

    public List<OutputDefinition> readOutputs(List<Output> outputs) {
        return outputs.stream().map(output -> {
            OutputDefinition definition = new OutputDefinition();
            definition.setId(createId(output.getId()));
            definition.setFormat(getFormat(output.getFormat()));
            definition.setDataTransmissionMode(getTransmisionMode(output.getTransmissionMode()));
            return definition;
        }).collect(toList());
    }

    private DataTransmissionMode getTransmisionMode(TransmissionMode transmissionMode) {
        switch (transmissionMode) {
            case VALUE:
                return DataTransmissionMode.VALUE;
            case REFERENCE:
            default:
                return DataTransmissionMode.REFERENCE;
        }
    }

    private Format getFormat(Object object) {
        String mimeType = "";
        String encoding = "";
        String schema = "";

        if (object instanceof Map<?, ?>) {

            Map<?, ?> map = (Map<?, ?>) object;

            mimeType = map.containsKey(MIME_TYPE_KEY) ? map.get(MIME_TYPE_KEY).toString() : "";
            encoding = map.containsKey(ENCODING_KEY) ? map.get(ENCODING_KEY).toString() : "";
            schema = map.containsKey(SCHEMA_KEY) ? map.get(SCHEMA_KEY).toString() : "";

        } else if (object instanceof io.swagger.model.Format) {

            io.swagger.model.Format format = (io.swagger.model.Format) object;

            return new Format(format.getMimeType(), format.getEncoding(), format.getSchema());
        }
        return new Format(mimeType, encoding, schema);
    }

    private OwsCode createId(String id) {
        return new OwsCode(id);
    }

    public List<ProcessData> readInputs(List<Input> inputs) throws URISyntaxException, JsonProcessingException {
        List<ProcessData> processDataList = new ArrayList<>();
        for (Input input : inputs) {
            OwsCode id = createId(input.getId());
            Object object = input.getInput();
            if (object instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) object;
                processDataList.add(readInput(id, map));
            }
        }
        return processDataList;
    }

    private ProcessData readInput(OwsCode id, Map<?, ?> map) throws URISyntaxException, JsonProcessingException {
        if (map.containsKey(VALUE_KEY)) {

            Object value = map.get(VALUE_KEY);

            if (value instanceof Map<?, ?>) {
                // complex data
                Map<?, ?> complexValueMap = (Map<?, ?>) value;

                if (complexValueMap.containsKey(INLINE_VALUE_KEY)) {

                    Format format = new Format("text/plain");

                    if (map.containsKey(FORMAT_KEY)) {
                        format = getFormat(map.get(FORMAT_KEY));
                    }

                    return new StringValueProcessData(id, format, complexValueMap.get(INLINE_VALUE_KEY).toString());

                } else if (complexValueMap.containsKey(HREF_KEY)) {

                    URI uri = new URI(complexValueMap.get(HREF_KEY).toString());

                    Format format = new Format();

                    if (map.containsKey(FORMAT_KEY)) {
                        format = getFormat(map.get(FORMAT_KEY));
                    }

                    return new ReferenceProcessData(id, format, uri);
                }
            } else if (value instanceof String) {
                return new StringValueProcessData(id, FORMAT_TEXT_PLAIN, (String) value);
            }
        } else if (map.containsKey(JSONBoundingBoxInputOutputHandler.BBOX)) {
            return new StringValueProcessData(id, JSONBoundingBoxInputOutputHandler.FORMAT_APPLICATION_JSON, new ObjectMapper().writeValueAsString(map));
        }

        return null;
    }

}
