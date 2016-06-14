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
package org.n52.javaps.io;

import java.util.ArrayList;
import java.util.List;

import org.n52.javaps.annotation.ConfigurableClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Extending subclasses of AbstractGenerator shall provide functionality to
 * generate serviceable output data for the processes offered by the 52N WPS
 * framework.
 *
 * @author Matthias Mueller
 *
 */

public abstract class AbstractIOHandler extends ConfigurableClass implements IOHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(AbstractIOHandler.class);

    protected List<Format> supportedFormats;

    protected List<Class<?>> supportedIDataTypes;

//    protected List<Object> properties;

    protected List<Object> formats;

    public AbstractIOHandler() {
        this.supportedFormats = new ArrayList<Format>();
        this.supportedIDataTypes = new ArrayList<Class<?>>();

        JsonNode formats = properties != null? properties.get("formats") : null;

        if(formats == null){
            LOGGER.info("Parser class {} has no formats.", this.getClass().getName());
            return;
        }

        if(!(formats instanceof ArrayNode)){
            LOGGER.info("Formats is not an ArrayNode for parser class {}.", this.getClass().getName());
            LOGGER.info("No formats added.");
            return;
        }

        readFormats((ArrayNode)formats);

    }

    private void readFormats(ArrayNode formats) {

        boolean hasDefault = false;

        for (JsonNode format : formats) {

            String mimeType = getStringValue(format, "mimetype");
            String schema = getStringValue(format, "schema");
            String encoding = getStringValue(format, "encoding");
            boolean defaultFormat = getBooleanValue(format, "default");

            if(defaultFormat && hasDefault){
                LOGGER.info("Multiple default formats found for Class {}.", this.getClass().getName());
                LOGGER.info("Using first default format.");
                defaultFormat = false;
            }else if(defaultFormat){
                hasDefault = true;
            }

            supportedFormats.add(new Format(mimeType, schema, encoding, defaultFormat));
        }

    }

    private String getStringValue(JsonNode node, String fieldName){
        JsonNode nodeValue = node.get(fieldName);

        if(!(nodeValue instanceof TextNode)){
            LOGGER.info("Field {} of format is not a TextNode.", fieldName);
            LOGGER.info("Returning empty String.");
            return "";
        }

        return nodeValue.asText();
    }

    private boolean getBooleanValue(JsonNode node, String fieldName){
        JsonNode nodeValue = node.get(fieldName);

        if(!(nodeValue instanceof BooleanNode)){
            LOGGER.info("Field {} of format is not a BooleanNode.", fieldName);
            LOGGER.info("Returning false.");
            return false;
        }

        return nodeValue.asBoolean();
    }

    /**
     * Returns true if the given format is supported, else false.
     */
    public boolean isSupportedFormat(String format) {
        List<Format> sf = getSupportedFormats();
        for (Format f : sf) {
            if (f.equals(format)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array having the supported formats (mimeTypes).
     */
    public List<Format> getSupportedFormats() {
        return supportedFormats;
    }

    public Class<?>[] getSupportedDataBindings() {
        return supportedIDataTypes.toArray(new Class<?>[supportedIDataTypes.size()]);
    }

    public boolean isSupportedDataBinding(Class<?> binding) {
        for (Class<?> currentBinding : supportedIDataTypes) {
            if (binding.equals(currentBinding)) {
                return true;
            }
        }
        return false;
    }

}
