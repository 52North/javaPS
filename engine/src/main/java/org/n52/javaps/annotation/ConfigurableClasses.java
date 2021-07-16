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
package org.n52.javaps.annotation;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

import org.n52.janmayen.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

/**
 * This class searches for a <code>Properties</code> annotation and tries to
 * parse the properties to a JSON object. First it checks, if the
 * propertyFilename of the annotation leads to a existing file. If this is the
 * case, the properties are parsed. If the propertyFilename doesn't lead to an
 * existing file, the defaultPropertyFilename is checked.
 *
 * @author Christian Autermann
 */
public class ConfigurableClasses {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableClasses.class);

    private static final Map<Class<?>, Optional<JsonNode>> CONFIGURATIONS = new ConcurrentHashMap<>();

    private static final String COULD_NOT_READ_PROPERTY = "Could not read property file for class ";

    public static Optional<JsonNode> get(Class<?> clazz) {
        return CONFIGURATIONS.computeIfAbsent(clazz, ConfigurableClasses::load);
    }

    private static Optional<JsonNode> load(Class<?> clazz) {
        Properties annotation = clazz.getAnnotation(Properties.class);
        if (annotation == null) {
            LOGGER.warn("Class extends {}, but is not annotated with {} annotation.", clazz.getName(), Properties.class
                    .getName());
        } else {
            Optional<String> fileName = getPropertiesFileName(annotation);
            Optional<String> defaultFileName = getDefaultPropertiesFileName(annotation);
            if (!fileName.isPresent() && !defaultFileName.isPresent()) {
                LOGGER.warn("Class {} is annotated with {} annotation, but the annotation is empty.", clazz.getName(),
                        Properties.class.getName());
            }

            URL fileURL = null;
            // always check propertyFilename first
            if (fileName.isPresent()) {
                fileURL = locateFile(fileName.get());
                // check if the strategies found something
                if (fileURL != null) {
                    try {
                        return Optional.of(Json.loadURL(fileURL));
                    } catch (IOException e) {
                        LOGGER.error(COULD_NOT_READ_PROPERTY  + clazz.getName(), e);
                    }
                }
            }
            if (defaultFileName.isPresent()) {
                fileURL = locateFile(defaultFileName.get());
                try {
                    return Optional.of(Json.loadURL(fileURL));
                } catch (IOException e) {
                    LOGGER.error(COULD_NOT_READ_PROPERTY + clazz.getName(), e);
                }
            }
        }
        return Optional.empty();
    }

    private static URL locateFile(String fileName) {
        FileLocationStrategy strategy = new CombinedLocationStrategy(Arrays.asList(new FileSystemLocationStrategy(),
                new ClasspathLocationStrategy()));
        FileSystem fileSystem = new DefaultFileSystem();
        FileLocator locator = FileLocatorUtils.fileLocator().locationStrategy(strategy).fileName(fileName).create();
        return strategy.locate(fileSystem, locator);
    }

    private static Optional<String> getPropertiesFileName(Properties annotation) {
        return Optional.ofNullable(annotation.propertyFileName()).map(Strings::emptyToNull);
    }

    private static Optional<String> getDefaultPropertiesFileName(Properties annotation) {
        return Optional.ofNullable(annotation.defaultPropertyFileName()).map(Strings::emptyToNull);
    }

}
