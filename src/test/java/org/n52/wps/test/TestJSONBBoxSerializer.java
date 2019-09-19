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
package org.n52.wps.test;

import org.junit.Test;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.bbox.BoundingBoxData;
import org.n52.javaps.rest.io.JSONBoundingBoxInputOutputHandler;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class TestJSONBBoxSerializer {

    @Test
    public void testJSONBBoxSerializer() throws URISyntaxException, IOException, EncodingException {

        JSONBoundingBoxInputOutputHandler boundingBoxInputOutputHandler = new JSONBoundingBoxInputOutputHandler();

        OwsBoundingBox boundingBox = new OwsBoundingBox(new double[]{51.9, 7.1}, new double[]{52, 7.2}, new URI("EPSG:4326"));

        InputStream in = boundingBoxInputOutputHandler.generate(null, new BoundingBoxData(boundingBox), new Format("application/json"));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

    }

    @Test
    public void testJSONBBoxDeserializer() {

        String jsonBBoxString = "{\"bbox\":[51.9,7.1,52.0,7.2],\"crs\":\"EPSG:4326\"}";

        JSONBoundingBoxInputOutputHandler boundingBoxInputOutputHandler = new JSONBoundingBoxInputOutputHandler();

        try {
            Data<?> boundingBox = boundingBoxInputOutputHandler.parse(null, new ByteArrayInputStream(jsonBBoxString.getBytes()), null);

            assertTrue(boundingBox instanceof BoundingBoxData);

            BoundingBoxData boundingBoxData = (BoundingBoxData) boundingBox;

            assertTrue(boundingBoxData.getPayload().getLowerCorner()[0] == 51.9d);
        } catch (IOException | DecodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
