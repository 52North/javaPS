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
package org.n52.javaps.annotation;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.DefaultFileSystem;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileLocator;
import org.apache.commons.configuration2.io.FileLocatorUtils;
import org.apache.commons.configuration2.io.FileSystem;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.util.JSONUtils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Benjamin Pross
 *
 */
@Properties
public abstract class ConfigurableClass {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableClass.class);

    private final JsonNode properties;

    public ConfigurableClass() {

        List<FileLocationStrategy> subs = Arrays.asList(
                new FileSystemLocationStrategy(),
                new ClasspathLocationStrategy());

        FileLocationStrategy strategy = new CombinedLocationStrategy(subs);

        Properties annotation = this.getClass().getAnnotation(Properties.class);

        if(annotation == null){
            LOGGER.warn("Class extends {}, but is not annotated with {} annotation.", this.getClass().getName(), Properties.class.getName());
            this.properties = JSONUtils.nodeFactory().objectNode();
            return;
        }

        //first try non-default property file
        String propertyFileName = annotation.propertyFileName();

        if(propertyFileName == null || propertyFileName.isEmpty()){
            //fall back to default property file
            propertyFileName = annotation.defaultPropertyFileName();
        }

        if(propertyFileName == null || propertyFileName.isEmpty()){
            LOGGER.warn("Class {} is annotated with {} annotation, but the annotation is empty.", this.getClass().getName(), Properties.class.getName());
            this.properties = JSONUtils.nodeFactory().objectNode();
            return;
        }

        FileSystem arg0 = new DefaultFileSystem();

        FileLocator locator =
                FileLocatorUtils.fileLocator().fileName(propertyFileName)
                .create();

        URL fileURL = strategy.locate(arg0, locator);

        JsonNode props;
        try {
            props = JSONUtils.loadURL(fileURL);

        } catch (IOException e) {
            LOGGER.error("Could not read property file for class {}" + this.getClass().getName(), e);
            props = JSONUtils.nodeFactory().objectNode();
        }
        this.properties = props;
    }

    protected final JsonNode getProperties() {
        return properties;
    }


}
