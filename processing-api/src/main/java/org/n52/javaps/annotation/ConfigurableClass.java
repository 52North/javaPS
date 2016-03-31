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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.ClasspathLocationStrategy;
import org.apache.commons.configuration2.io.CombinedLocationStrategy;
import org.apache.commons.configuration2.io.FileLocationStrategy;
import org.apache.commons.configuration2.io.FileSystemLocationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Benjamin Pross
 *
 */
@Properties
public abstract class ConfigurableClass {

    private static Logger LOGGER = LoggerFactory.getLogger(ConfigurableClass.class);

    protected Configuration config;

    public ConfigurableClass() {

        List<FileLocationStrategy> subs = Arrays.asList(
                new FileSystemLocationStrategy(),
                new ClasspathLocationStrategy());
        
        FileLocationStrategy strategy = new CombinedLocationStrategy(subs);
        
        Parameters params = new Parameters();
        
        PropertiesBuilderParameters properties = params.properties();
        
        Properties annotation = this.getClass().getAnnotation(Properties.class);

        if(annotation == null){
            LOGGER.warn("Class extends {}, but is not annotated with {} annotation.", this.getClass().getName(), Properties.class.getName());
            return;
        }

        String propertyFileName = annotation.propertyFileName();

        if(propertyFileName == null || propertyFileName.equals("")){
            propertyFileName = annotation.defaultPropertyFileName();
        }

        if(propertyFileName == null || propertyFileName.equals("")){
            LOGGER.warn("Class {} is annotated with {} annotation, but the annotation is empty.", this.getClass().getName(), Properties.class.getName());
            return;
        }
        
        properties.setLocationStrategy(strategy);
        
        properties.setFileName(propertyFileName);
        
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class).configure(properties);
                
        try {
            config = builder.getConfiguration();
        } catch (ConfigurationException e) {
            LOGGER.error("Could not read property file", this.getClass().getName(), Properties.class.getName());
            LOGGER.error(e.getMessage());
        }
    }

//    private void loadProperties(String propertyFileName) throws IOException{
//
//        File propertyFile = new File(propertyFileName);
//
//        if(!propertyFile.exists()){
//            LOGGER.warn("Property file {}, specified by class {}, doesn't exist.", propertyFileName, this.getClass().getName());
//            return;
//        }
//
//        FileReader fileReader = new FileReader(propertyFile);
//
//        this.properties = new java.util.Properties();
//
//        this.properties.load(fileReader);
//    }
}
