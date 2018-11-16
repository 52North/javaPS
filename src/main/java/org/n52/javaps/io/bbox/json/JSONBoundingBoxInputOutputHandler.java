package org.n52.javaps.io.bbox.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.OutputHandler;
import org.n52.javaps.io.bbox.BoundingBoxData;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONBoundingBoxInputOutputHandler implements InputHandler, OutputHandler {
    
    private static final Logger log = LoggerFactory.getLogger(JSONBoundingBoxInputOutputHandler.class);
    
    public static final Format FORMAT_APPLICATION_JSON = new Format("application/json");

    public static final String LOWER_CORNER_KEY = "lowerCorner";
    
    public static final String UPPER_CORNER_KEY = "upperCorner";
    
    public static final String CRS_KEY = "crs";

    public static final Set<Format> FORMATS = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(FORMAT_APPLICATION_JSON)));
    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);
    
    @Override
    public Set<Format> getSupportedFormats() {
        return FORMATS;
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description,
            Data<?> data,
            Format format) throws IOException, EncodingException {
        
        InputStream result = null;
        
        if(data instanceof BoundingBoxData) {
            OwsBoundingBox boundingBox = ((BoundingBoxData)data).getPayload();
            
            double[] lowerCorner = boundingBox.getLowerCorner();
            double[] upperCorner = boundingBox.getUpperCorner();
            
            String lowerCornerString = lowerCorner[0] + " " + lowerCorner[1];
            String upperCornerString = upperCorner[0] + " " + upperCorner[1];
            
            String crs = "";
            
            try {
                crs = boundingBox.getCRS().get().toString();
            } catch (Exception e) {
                log.info("No crs present.");
            }

            ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode rootNode = objectMapper.createObjectNode();

            rootNode.put(LOWER_CORNER_KEY, lowerCornerString);
            rootNode.put(UPPER_CORNER_KEY, upperCornerString);
            rootNode.put(CRS_KEY, crs);
            
            result = new ByteArrayInputStream(rootNode.toString().getBytes());
        }
        
        return result;
    }

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description,
            InputStream input,
            Format format) throws IOException, DecodingException {
                
        JsonNode bboxNode = new ObjectMapper().readTree(input);
        
        JsonNode lowerCornerNode = bboxNode.path(LOWER_CORNER_KEY);
        JsonNode upperCornerNode = bboxNode.path(UPPER_CORNER_KEY);
        JsonNode crsNode = bboxNode.path(CRS_KEY);
        
        OwsBoundingBox boundingBox;
        try {
            boundingBox = createOWSBoundingbox(lowerCornerNode.asText(), upperCornerNode.asText(), crsNode.asText());
        } catch (URISyntaxException e) {
            throw new DecodingException(e);
        }
        
        return new BoundingBoxData(boundingBox);
    }

    private OwsBoundingBox createOWSBoundingbox(String lowerCornerNodeString,
            String upperCornerNodeString,
            String crsNodeString) throws URISyntaxException {

        String[] lowerCornerStringArray = lowerCornerNodeString.split(" ");
        String[] upperCornerStringArray = upperCornerNodeString.split(" ");
        
        double minX = Double.parseDouble(lowerCornerStringArray[1]);
        double minY = Double.parseDouble(lowerCornerStringArray[0]);
        double maxX = Double.parseDouble(upperCornerStringArray[1]);
        double maxY = Double.parseDouble(upperCornerStringArray[0]);
        
        double[] lowerCorner = new double[]{minX, minY};
        double[] upperCorner = new double[]{maxX, maxY};
        URI crs = new URI(crsNodeString);
        return new OwsBoundingBox(lowerCorner, upperCorner, crs);
    }

}
