/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.javaps.description.TypedBoundingBoxInputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.InputHandler;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;

import com.google.common.primitives.Doubles;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxInputHandlerTest {
    private static final URI EPSG_4326 = URI.create("EPSG:4326");

    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private InputHandler inputHandler;
    private TypedProcessDescriptionFactory descriptionFactory;
    private TypedBoundingBoxInputDescription inputDescription;

    @Before
    public void setup() {
        this.inputHandler = new BoundingBoxInputOutputHandler();
        this.descriptionFactory = new TypedProcessDescriptionFactory();
        this.inputDescription = descriptionFactory.boundingBoxInput()
                .withIdentifier("input")
                .withDefaultCRS(EPSG_4326)
                .withSupportedCRS(EPSG_4326)
                .build();
    }

    @Test
    public void testDecoding() throws IOException, DecodingException {

        String value
                = "<ows:BoundingBox xmlns:ows=\"http://www.opengis.net/ows/2.0\" dimensions=\"3\" crs=\"EPSG:4326\"><ows:LowerCorner>1 2 3</ows:LowerCorner><ows:UpperCorner>4 5 6</ows:UpperCorner></ows:BoundingBox>";

        Charset charset = StandardCharsets.ISO_8859_1;
        Format format = Format.TEXT_XML.withEncoding(charset);
        Data<?> data = this.inputHandler.parse(inputDescription, new ByteArrayInputStream(value.getBytes(charset)), format);

        errors.checkThat(data, is(instanceOf(BoundingBoxData.class)));
        OwsBoundingBox payload = ((BoundingBoxData) data).getPayload();
        errors.checkThat(payload, is(notNullValue()));
        errors.checkThat(payload.getCRS(), is(Optional.of(EPSG_4326)));
        errors.checkThat(payload.getDimension(), is(3));
        errors.checkThat(Doubles.asList(payload.getLowerCorner()), Matchers.contains(1.0d, 2.0d, 3.0d));
        errors.checkThat(Doubles.asList(payload.getUpperCorner()), Matchers.contains(4.0d, 5.0d, 6.0d));
    }


}
