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
package org.n52.javaps.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.commons.WPSConfig;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.annotation.Process;
import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.ParserFactory;

/**
 * A static repository to retrieve the available algorithms.
 *
 * @author foerster
 *
 */
public class LocalAlgorithmRepository implements IAlgorithmRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalAlgorithmRepository.class);
    private final Map<OwsCodeType, ProcessDescription> processDescriptionMap = new HashMap<>();
    private final Map<OwsCodeType, IAlgorithm> algorithmMap = new HashMap<>();
    private GeneratorFactory generatorFactory;
    private ParserFactory parserFactory;

    @Override
    public IAlgorithm getAlgorithm(OwsCodeType className) {
        if (getAlgorithmNames().contains(className)) {
            return algorithmMap.get(className);
        }
        return null;
    }

    @Override
    public Set<OwsCodeType> getAlgorithmNames() {
        return Stream.concat(processDescriptionMap.keySet().stream(), algorithmMap.keySet().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean containsAlgorithm(OwsCodeType className) {
        return getAlgorithmNames().contains(className);
    }

    private IAlgorithm loadAlgorithm(String algorithmClassName) throws Exception {
        Class<?> algorithmClass = LocalAlgorithmRepository.class.getClassLoader().loadClass(algorithmClassName);
        IAlgorithm algorithm = null;
        if (IAlgorithm.class.isAssignableFrom(algorithmClass)) {
            algorithm = IAlgorithm.class.cast(algorithmClass.newInstance());
        } else if (algorithmClass.isAnnotationPresent(Process.class)) {
            // we have an annotated algorithm that doesn't implement IAlgorithm
            // wrap it in a proxy class
            algorithm = new AbstractAnnotatedAlgorithm.Proxy(algorithmClass);
        } else {
            throw new Exception("Could not load algorithm " + algorithmClassName + " does not implement IAlgorithm or have a Algorithm annotation.");
        }

        algorithm.setGeneratorFactory(generatorFactory);
        algorithm.setParserFactory(parserFactory);

        boolean isNoProcessDescriptionValid = false;

        for (String supportedVersion : WPSConfig.SUPPORTED_VERSIONS) {
            isNoProcessDescriptionValid = isNoProcessDescriptionValid && !algorithm.processDescriptionIsValid(supportedVersion);
        }

        if (isNoProcessDescriptionValid) {
            LOGGER.warn("Algorithm description is not valid: " + algorithmClassName);// TODO
                                                                                     // add
                                                                                     // version
                                                                                     // to
                                                                                     // exception/log
            throw new Exception("Could not load algorithm " + algorithmClassName + ". ProcessDescription Not Valid.");
        }

        return algorithm;
    }

    public boolean addAlgorithm(Object processID) {
        if (!(processID instanceof String)) {
            return false;
        }
        String algorithmClassName = (String) processID;

        try {

            IAlgorithm algorithm = loadAlgorithm(algorithmClassName);
            OwsCodeType id = new OwsCodeType(algorithmClassName);
            processDescriptionMap.put(id, algorithm.getDescription());
            algorithmMap.put(id, algorithm);
            LOGGER.info("Algorithm class registered: " + algorithmClassName);

            return true;
        } catch (Exception e) {
            LOGGER.error("Exception while trying to add algorithm {}", algorithmClassName);
            LOGGER.error(e.getMessage());

        }

        return false;

    }

    @Override
    public ProcessDescription getProcessDescription(OwsCodeType processID) {
        if (getAlgorithmNames().contains(processID)) {
            return processDescriptionMap.get(processID);
        }
        return null;
    }
}
