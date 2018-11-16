package org.n52.wps.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.bbox.BoundingBoxData;
import org.n52.javaps.io.bbox.json.JSONBoundingBoxInputOutputHandler;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;

public class TestJSONBBoxSerializer {

    @Test
    public void testJSONBBoxSerializer() throws URISyntaxException, IOException, EncodingException {
        
        JSONBoundingBoxInputOutputHandler boundingBoxInputOutputHandler = new JSONBoundingBoxInputOutputHandler();
        
        OwsBoundingBox boundingBox = new OwsBoundingBox(new double[]{51.9, 7.1}, new double[]{52, 7.2}, new URI("EPSG:4326"));
        
        InputStream in = boundingBoxInputOutputHandler.generate(null, new BoundingBoxData(boundingBox), new Format("application/json"));
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        
        String line = null;
        
        while((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        
    }
    
}
