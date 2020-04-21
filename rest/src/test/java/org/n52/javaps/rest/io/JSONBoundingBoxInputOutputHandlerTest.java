/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.google.common.primitives.Doubles;
import org.junit.Before;
import org.junit.Test;
import org.n52.janmayen.Json;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.bbox.BoundingBoxData;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class JSONBoundingBoxInputOutputHandlerTest {
    private static final double X_MIN = 51.9;
    private static final double Y_MIN = 7.1;
    private static final double X_MAX = 52.0;
    private static final double Y_MAX = 7.2;
    private static final URI EPSG_4326 = URI.create("EPSG:4326");
    private static final String BBOX_JSON = String.format(Locale.ROOT, "{\"bbox\":[%s,%s,%s,%s],\"crs\":\"%s\"}", X_MIN,
            Y_MIN, X_MAX, Y_MAX, EPSG_4326);
    private static final double DELTA = 0.0d;
    private static final Format FORMAT = new Format("application/json");
    private JSONBoundingBoxInputOutputHandler handler;

    @Before
    public void setup() {
        handler = new JSONBoundingBoxInputOutputHandler(new ObjectMapper(), Json.nodeFactory());
    }

    @Test
    public void testJSONBBoxSerializer() throws IOException, EncodingException {
        OwsBoundingBox bbox = new OwsBoundingBox(new double[] { X_MIN, Y_MIN }, new double[] { X_MAX, Y_MAX },
                EPSG_4326);
        BoundingBoxData data = new BoundingBoxData(bbox);

        try (InputStream in = handler.generate(null, data, FORMAT);
                InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            assertThat(CharStreams.toString(reader), is(equalTo(BBOX_JSON)));
        }

    }

    @Test
    public void testJSONBBoxDeserializer() throws IOException, DecodingException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(BBOX_JSON.getBytes(StandardCharsets.UTF_8))) {
            Data<?> data = handler.parse(null, input, FORMAT);
            assertThat(data, is(instanceOf(BoundingBoxData.class)));
            OwsBoundingBox bbox = ((BoundingBoxData) data).getPayload();
            assertThat(bbox.getCRS().orElse(null), is(equalTo(EPSG_4326)));
            assertThat(Doubles.asList(bbox.getLowerCorner()),
                    contains(is(closeTo(X_MIN, DELTA)), is(closeTo(Y_MIN, DELTA))));
            assertThat(Doubles.asList(bbox.getUpperCorner()),
                    contains(is(closeTo(X_MAX, DELTA)), is(closeTo(Y_MAX, DELTA))));
        }

    }

}
