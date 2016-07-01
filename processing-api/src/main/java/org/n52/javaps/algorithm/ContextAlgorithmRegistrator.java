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

import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.javaps.algorithm.annotation.Algorithm;

/**
 * @author Christian Autermann
 */
public class ContextAlgorithmRegistrator implements Constructable, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(ContextAlgorithmRegistrator.class);

    private LocalAlgorithmRepository localAlgorithmRepository;
    private ApplicationContext applicationContext;

    @Override
    public void init() {
        Stream.concat(intefaceImplementations(), annotatedInstances())
                .peek(algorithm -> LOG.info("Registering {}", algorithm))
                .forEach(this.localAlgorithmRepository::addAlgorithm);
    }

    @Inject
    public void setLocalAlgorithmRepository(LocalAlgorithmRepository localAlgorithmRepository) {
        this.localAlgorithmRepository = Objects.requireNonNull(localAlgorithmRepository);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = Objects.requireNonNull(applicationContext);
    }

    private Stream<IAlgorithm> intefaceImplementations() throws BeansException {
        return this.applicationContext.getBeansOfType(IAlgorithm.class).values().stream();
    }

    private Stream<Object> annotatedInstances() throws BeansException {
        return this.applicationContext.getBeansWithAnnotation(Algorithm.class).values().stream();
    }

}
