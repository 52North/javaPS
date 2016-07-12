/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io.bbox;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.iceland.ogc.ows.OwsBoundingBox;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedBoundingBoxInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedBoundingBoxOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.Data;

import com.google.common.io.CharStreams;
import com.google.common.primitives.Doubles;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxDataHandlerTest {
    private static final URI EPSG_4326 = URI.create("EPSG:4326");

    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private BoundingBoxDataHandler handler;
    private TypedProcessDescriptionFactory descriptionFactory;
    private TypedBoundingBoxInputDescription inputDescription;
    private TypedBoundingBoxOutputDescription outputDescription;

    @Before
    public void setup() {
        this.handler = new BoundingBoxDataHandler();
        this.descriptionFactory = new TypedProcessDescriptionFactory();
        this.outputDescription = descriptionFactory.boundingBoxOutput()
                .withIdentifier("input")
                .withDefaultCRS(EPSG_4326)
                .withSupportedCRS(EPSG_4326)
                .build();
        this.inputDescription = descriptionFactory.boundingBoxInput()
                .withIdentifier("input")
                .withDefaultCRS(EPSG_4326)
                .withSupportedCRS(EPSG_4326)
                .build();
    }

    @Test
    public void testDecoding() throws IOException {

        String value
                = "<ows:BoundingBox xmlns:ows=\"http://www.opengis.net/ows/2.0\" dimension=\"3\" crs=\"EPSG:4326\"><ows:LowerCorner>1 2 3</ows:LowerCorner><ows:UpperCorner>4 5 6</ows:UpperCorner></ows:BoundingBox>";

        Charset charset = StandardCharsets.ISO_8859_1;
        Data<?> decode = this.handler
                .parse(inputDescription, new ByteArrayInputStream(value.getBytes(charset)), new Format("text/xml", charset));

        errors.checkThat(decode, is(instanceOf(BoundingBoxData.class)));
        OwsBoundingBox payload = ((BoundingBoxData) decode).getPayload();
        errors.checkThat(payload, is(notNullValue()));
        errors.checkThat(payload.getCRS(), is(Optional.of(EPSG_4326)));
        errors.checkThat(payload.getDimension(), is(3));
        errors.checkThat(Doubles.asList(payload.getLowerCorner()), Matchers.contains(1.0d, 2.0d, 3.0d));
        errors.checkThat(Doubles.asList(payload.getUpperCorner()), Matchers.contains(4.0d, 5.0d, 6.0d));
    }

    @Test
    public void testEncoding() throws IOException {
        OwsBoundingBox bbox = new OwsBoundingBox(new double[] { 1.0d, 2.0d, 3.0d },
                                                 new double[] { 4.0d, 5.0d, 6.0d }, EPSG_4326);
        Charset charset = StandardCharsets.ISO_8859_1;
        InputStream stream = this.handler.generate(outputDescription, new BoundingBoxData(bbox), new Format("text/xml", charset));

        try (Reader reader =new InputStreamReader(stream, charset)) {
            String string = CharStreams.toString(reader);
            errors.checkThat(string, is("<ows:BoundingBox xmlns:ows=\"http://www.opengis.net/ows/2.0\" crs=\"EPSG:4326\" dimension=\"3\"><ows:LowerCorner>1.0 2.0 3.0</ows:LowerCorner><ows:UpperCorner>4.0 5.0 6.0</ows:UpperCorner></ows:BoundingBox>"));
        }
    }

}
