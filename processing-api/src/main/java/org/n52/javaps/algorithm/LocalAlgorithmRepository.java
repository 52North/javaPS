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
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.util.Optionals;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.algorithm.annotation.AnnotatedAlgorithm;
import org.n52.javaps.io.GeneratorRepository;
import org.n52.javaps.io.ParserRepository;
import org.n52.javaps.ogc.wps.description.ProcessDescription;


/**
 * A static repository to retrieve the available algorithms.
 *
 * @author foerster
 *
 */
public class LocalAlgorithmRepository implements AlgorithmRepository {

    private static final Logger LOG = LoggerFactory.getLogger(LocalAlgorithmRepository.class);

    private final Map<OwsCode, ProcessDescription> descriptions = new HashMap<>();
    private final Map<OwsCode, Class<?>> algorithmClasses = new HashMap<>();
    private final Map<OwsCode, IAlgorithm> algorithmInstances = new HashMap<>();
    private final ParserRepository parserRepository;
    private final GeneratorRepository generatorRepository;
    private final AutowireCapableBeanFactory beanFactory;

    @Inject
    public LocalAlgorithmRepository(ParserRepository parserRepository,
                                    GeneratorRepository generatorRepository,
                                    ApplicationContext applicationContext) {
        this.parserRepository = Objects.requireNonNull(parserRepository);
        this.generatorRepository = Objects.requireNonNull(generatorRepository);
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }


    @Override
    public Optional<IAlgorithm> getAlgorithm(OwsCode id) {
        return Optionals.or(Optional.ofNullable(this.algorithmInstances.get(id)),
                            Optional.ofNullable(this.algorithmClasses.get(id))
                                    .map(this::mayInstantiate));
    }

    @Override
    public Optional<ProcessDescription> getProcessDescription(OwsCode id) {
        return Optional.ofNullable(this.descriptions.get(id));
    }

    @Override
    public Set<OwsCode> getAlgorithmNames() {
        return new HashSet<>(this.descriptions.keySet());
    }

    @Override
    public boolean containsAlgorithm(OwsCode id) {
        return this.descriptions.containsKey(id);
    }


    public boolean addAlgorithm(String className) {
        Objects.requireNonNull(className);
        try {
            Class<?> algorithmClass = getClass().getClassLoader().loadClass(className);
            return addAlgorithm(algorithmClass);
        } catch (ClassNotFoundException ex) {
            LOG.error("Could not load algorithm class " + className, ex);
            return false;
        }
    }

    public boolean addAlgorithm(Class<?> clazz) {
        Objects.requireNonNull(clazz);

        IAlgorithm instance;
        try {
            instance = instantiate(clazz);
        } catch (InstantiationException | IllegalAccessException | ClassCastException ex) {
            LOG.info("Could not instantiate class {}", clazz);
            return false;
        }
        ProcessDescription description = instance.getDescription();
        OwsCode id = description.getId();

        if (this.descriptions.put(id, description) != null) {
            LOG.warn("Duplicate algorithm identifier: {}", id);
        }

        this.algorithmClasses.put(id, clazz);
        LOG.info("Algorithm class {} with id {} registered", clazz, id);
        return true;
    }

     public boolean addAlgorithm(IAlgorithm instance) {
         Objects.requireNonNull(instance);

        ProcessDescription description = instance.getDescription();
        OwsCode id = description.getId();

        if (this.descriptions.put(id, description) != null) {
            LOG.warn("Duplicate algorithm identifier: {}", id);
        }

        this.algorithmInstances.put(id, instance);
        LOG.info("Algorithm {} with id {} registered", instance, id);
        return true;
    }

    private IAlgorithm mayInstantiate(Class<?> clazz) {
        try {
            return instantiate(clazz);
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    private IAlgorithm instantiate(Class<?> clazz)
            throws InstantiationException, IllegalAccessException,
                   ClassCastException {
        IAlgorithm instance = (IAlgorithm) beanFactory.createBean(clazz);
        if (clazz.isAnnotationPresent(Algorithm.class) && !(instance instanceof AnnotatedAlgorithm)) {
            return new AnnotatedAlgorithm(parserRepository, generatorRepository, instance);
        } else {
            return instance;
        }
    }

}
