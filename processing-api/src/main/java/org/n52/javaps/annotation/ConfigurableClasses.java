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

import org.n52.iceland.util.JSONUtils;
import org.n52.iceland.util.Optionals;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ConfigurableClasses {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableClasses.class);
    private static final Map<Class<?>, Optional<JsonNode>> CONFIGURATIONS = new ConcurrentHashMap<>();

    public static Optional<JsonNode> get(Class<?> clazz) {
        return CONFIGURATIONS.computeIfAbsent(clazz, ConfigurableClasses::load);
    }

    private static Optional<JsonNode> load(Class<?> clazz) {
        Properties annotation = clazz.getAnnotation(Properties.class);
        if (annotation == null) {
            LOGGER.warn("Class extends {}, but is not annotated with {} annotation.", clazz.getName(), Properties.class.getName());
        } else {
            Optional<String> fileName = getPropertiesFileName(annotation);
            if (!fileName.isPresent()) {
                LOGGER.warn("Class {} is annotated with {} annotation, but the annotation is empty.", clazz.getName(), Properties.class.getName());
            } else {
                URL fileURL = locateFile(fileName.get());
                try {
                    return Optional.of(JSONUtils.loadURL(fileURL));
                } catch (IOException e) {
                    LOGGER.error("Could not read property file for class " + clazz.getName(), e);
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
        return Optionals.or(Optional.ofNullable(annotation.propertyFileName()).map(Strings::emptyToNull),
                            Optional.ofNullable(annotation.defaultPropertyFileName()).map(Strings::emptyToNull));
    }

}
