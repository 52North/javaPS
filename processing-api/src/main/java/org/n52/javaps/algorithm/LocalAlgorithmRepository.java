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
import java.util.function.Supplier;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.description.ProcessDescription;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.algorithm.annotation.AnnotatedAlgorithm;
import org.n52.javaps.io.complex.GeneratorRepository;
import org.n52.javaps.io.complex.ParserRepository;
import org.n52.javaps.io.literal.LiteralTypeRepository;

/**
 * A static repository to retrieve the available algorithms.
 *
 * @author foerster
 *
 */
public class LocalAlgorithmRepository implements AlgorithmRepository {

    private static final Logger LOG = LoggerFactory.getLogger(LocalAlgorithmRepository.class);

    private final Map<OwsCode, ProcessDescription> descriptions = new HashMap<>();
    private final Map<OwsCode, Supplier<IAlgorithm>> algorithms = new HashMap<>();
    private final ParserRepository parserRepository;
    private final GeneratorRepository generatorRepository;
    private final LiteralTypeRepository literalTypeRepository;
    private final AutowireCapableBeanFactory beanFactory;

    @Inject
    public LocalAlgorithmRepository(ParserRepository parserRepository,
                                    GeneratorRepository generatorRepository,
                                    LiteralTypeRepository literalTypeRepository,
                                    ApplicationContext applicationContext) {
        this.parserRepository = Objects.requireNonNull(parserRepository);
        this.generatorRepository = Objects.requireNonNull(generatorRepository);
        this.literalTypeRepository = Objects.requireNonNull(literalTypeRepository);
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    public Optional<IAlgorithm> getAlgorithm(OwsCode id) {
        return Optional.ofNullable(this.algorithms.get(id)).map(Supplier::get);
    }

    @Override
    public Optional<ProcessDescription> getProcessDescription(OwsCode id) {
        return Optional.ofNullable(this.descriptions.get(id));
    }

    @Override
    public Set<OwsCode> getAlgorithmNames() {
        return new HashSet<>(this.algorithms.keySet());
    }

    @Override
    public boolean containsAlgorithm(OwsCode id) {
        return this.descriptions.containsKey(id);
    }

    public void addAlgorithm(String className) {
        Objects.requireNonNull(className, "className");
        try {
            addAlgorithm(getClass().getClassLoader().loadClass(className));
        } catch (ClassNotFoundException ex) {
            LOG.error("Could not load algorithm class " + className, ex);
        }
    }

    public void addAlgorithm(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        instantiate(clazz).ifPresent(instance -> {
            ProcessDescription description = instance.getDescription();
            if (this.descriptions.put(description.getId(), description) != null) {
                LOG.warn("Duplicate algorithm identifier: {}", description.getId());
            }
            Supplier<Error> error = () -> new Error("Could not instantiate algorithm " + description.getId());
            this.algorithms.put(description.getId(), () -> instantiate(clazz).orElseThrow(error));
            LOG.info("Algorithm class {} with id {} registered", clazz, description.getId());

        });
    }

    public void addAlgorithm(IAlgorithm instance) {
        Objects.requireNonNull(instance, "instance");
        ProcessDescription description = instance.getDescription();
        if (this.descriptions.put(description.getId(), description) != null) {
            LOG.warn("Duplicate algorithm identifier: {}", description.getId());
        }
        this.algorithms.put(description.getId(), () -> instance);
        LOG.info("Algorithm {} with id {} registered", instance, description.getId());
    }

    public void addAlgorithm(Object object) {
        Objects.requireNonNull(object);
        if (object instanceof IAlgorithm) {
            addAlgorithm((IAlgorithm) object);
        } else if (object instanceof Class<?>) {
            addAlgorithm((Class<?>) object);
        } else if (object!=null && object.getClass().isAnnotationPresent(Algorithm.class)) {
            addAlgorithm(new AnnotatedAlgorithm(parserRepository, generatorRepository, literalTypeRepository, object));
        } else {
            LOG.error("Could not add algorithm {}", object);
        }
    }

    private Optional<IAlgorithm> instantiate(Class<?> clazz) {
        Object instance;
        try {
            instance = beanFactory.createBean(clazz);
        } catch (BeansException ex) {
            LOG.warn("Could not instantiate algorithm", ex);
            return Optional.empty();
        }

        if (clazz.isAnnotationPresent(Algorithm.class) && !(instance instanceof AnnotatedAlgorithm)) {
            return Optional.of(new AnnotatedAlgorithm(parserRepository, generatorRepository, literalTypeRepository, instance));
        } else if (instance instanceof IAlgorithm) {
            return Optional.of((IAlgorithm) instance);
        } else {
            LOG.warn("Algorithm class is not annotated and does not implement IAlgorithm: {}", clazz);
            return Optional.empty();
        }
    }

}
