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
package org.n52.javaps.algorithm;

import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.lifecycle.Constructable;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.utils.Context;

/**
 * @author Christian Autermann
 */
public class ContextAlgorithmRegistrator implements Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(ContextAlgorithmRegistrator.class);

    private LocalAlgorithmRepository repository;

    private Context context;

    @Override
    public void init() {
        Stream.concat(interfaceImplementations(), annotatedInstances()).peek(algorithm -> LOG.info("Registering {}",
                algorithm)).forEach(this.repository::addAlgorithm);
    }

    @Inject
    public void setContext(Context context) {
        this.context = context;
    }

    @Inject
    public void setRepository(LocalAlgorithmRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    private Stream<IAlgorithm> interfaceImplementations() {
        return this.context.getInstances(IAlgorithm.class).stream();
    }

    private Stream<Object> annotatedInstances() {
        return this.context.getAnnotatedInstances(Algorithm.class).stream();
    }

}
