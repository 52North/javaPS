/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;
import org.n52.javaps.description.TypedBoundingBoxOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.OutputHandler;

import com.google.common.io.CharStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxOutputHandlerTest {
    private static final URI EPSG_4326 = URI.create("EPSG:4326");

    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private OutputHandler outputHandler;
    private TypedProcessDescriptionFactory descriptionFactory;
    private TypedBoundingBoxOutputDescription outputDescription;

    @Before
    public void setup() {
        this.outputHandler = new BoundingBoxInputOutputHandler();
        this.descriptionFactory = new TypedProcessDescriptionFactory();
        this.outputDescription = descriptionFactory.boundingBoxOutput()
                .withIdentifier("input")
                .withDefaultCRS(EPSG_4326)
                .withSupportedCRS(EPSG_4326)
                .build();
    }

    @Test
    public void testEncoding() throws IOException, EncodingException {
        OwsBoundingBox bbox = new OwsBoundingBox(new double[] { 1.0d, 2.0d, 3.0d },
                                                 new double[] { 4.0d, 5.0d, 6.0d }, EPSG_4326);
        Charset charset = StandardCharsets.ISO_8859_1;
        Format format = Format.TEXT_XML.withEncoding(charset);
        BoundingBoxData data = new BoundingBoxData(bbox);

        InputStream stream = this.outputHandler.generate(outputDescription, data, format);

        try (Reader reader = new InputStreamReader(stream, charset)) {
            String string = CharStreams.toString(reader);
            errors.checkThat(string, is("<ows:BoundingBox xmlns:ows=\"http://www.opengis.net/ows/2.0\" crs=\"EPSG:4326\" dimensions=\"3\"><ows:LowerCorner>1.0 2.0 3.0</ows:LowerCorner><ows:UpperCorner>4.0 5.0 6.0</ows:UpperCorner></ows:BoundingBox>"));
        }
    }


     @Test
    public void testEncodingBase64() throws IOException, EncodingException {
        OwsBoundingBox bbox = new OwsBoundingBox(new double[] { 1.0d, 2.0d, 3.0d },
                                                 new double[] { 4.0d, 5.0d, 6.0d }, EPSG_4326);
        Charset charset = StandardCharsets.UTF_8;
        Format format = Format.TEXT_XML.withBase64Encoding();
        BoundingBoxData data = new BoundingBoxData(bbox);

        InputStream stream = this.outputHandler.generate(outputDescription, data, format);

        try (Reader reader = new InputStreamReader(new Base64InputStream(stream, false), charset)) {
            String string = CharStreams.toString(reader);
            errors.checkThat(string, is("<ows:BoundingBox xmlns:ows=\"http://www.opengis.net/ows/2.0\" crs=\"EPSG:4326\" dimensions=\"3\"><ows:LowerCorner>1.0 2.0 3.0</ows:LowerCorner><ows:UpperCorner>4.0 5.0 6.0</ows:UpperCorner></ows:BoundingBox>"));
        }
    }

}
