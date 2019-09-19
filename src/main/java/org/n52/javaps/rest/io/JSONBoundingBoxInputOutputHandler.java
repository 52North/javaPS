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
package org.n52.javaps.rest.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.janmayen.stream.Streams;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.OutputHandler;
import org.n52.javaps.io.bbox.BoundingBoxData;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

@Component
public class JSONBoundingBoxInputOutputHandler implements InputHandler, OutputHandler {
    private static final String CRS_KEY = "crs";
    private static final String BBOX_KEY = "bbox";
    private static final Set<Format> FORMATS = Collections.singleton(new Format("application/json"));
    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);

    @Override
    public Set<Format> getSupportedFormats() {
        return FORMATS;
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    private final ObjectMapper objectMapper;
    private final JsonNodeFactory jsonNodeFactory;

    @Autowired
    public JSONBoundingBoxInputOutputHandler(ObjectMapper objectMapper,
                                             JsonNodeFactory jsonNodeFactory) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.jsonNodeFactory = Objects.requireNonNull(jsonNodeFactory);
    }

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException {
        InputStream result = null;
        if (data instanceof BoundingBoxData) {
            OwsBoundingBox boundingBox = ((BoundingBoxData) data).getPayload();
            ObjectNode root = jsonNodeFactory.objectNode();
            ArrayNode arrayNode = root.putArray(BBOX_KEY);
            Streams.stream(boundingBox.getLowerCorner()).forEach(arrayNode::add);
            Streams.stream(boundingBox.getUpperCorner()).forEach(arrayNode::add);
            boundingBox.getCRS().map(URI::toString).ifPresent(crs -> root.put(CRS_KEY, crs));
            result = new ByteArrayInputStream(objectMapper.writeValueAsBytes(root));
        }
        return result;
    }

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description, InputStream input, Format format)
            throws IOException, DecodingException {
        JsonNode root = new ObjectMapper().readTree(input);
        JsonNode node = root.get(BBOX_KEY);
        double[] lower = IntStream.range(0, node.size() / 2)
                                  .mapToObj(node::get)
                                  .mapToDouble(JsonNode::asDouble)
                                  .toArray();
        double[] upper = IntStream.range(node.size() / 2, node.size())
                                  .mapToObj(node::get)
                                  .mapToDouble(JsonNode::asDouble)
                                  .toArray();
        return new BoundingBoxData(new OwsBoundingBox(lower, upper, parseCRS(root)));
    }

    private URI parseCRS(JsonNode bboxNode) throws DecodingException {
        URI crs;
        if (bboxNode.path(CRS_KEY).isTextual()) {
            try {
                crs = new URI(bboxNode.path(CRS_KEY).asText());
            } catch (URISyntaxException e) {
                throw new DecodingException(e);
            }
        } else {
            crs = null;
        }
        return crs;
    }

}
