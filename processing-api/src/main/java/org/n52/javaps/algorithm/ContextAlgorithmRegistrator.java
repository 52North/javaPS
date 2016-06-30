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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;

/**
 * @author Christian Autermann
 */
public class ContextAlgorithmRegistrator implements Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(ContextAlgorithmRegistrator.class);

    private LocalAlgorithmRepository localAlgorithmRepository;
    private Optional<Collection<IAlgorithm>> algorithms;

    @Override
    public void init() {
        this.algorithms.ifPresent(collection -> collection.stream().forEach(algorithm -> {
            LOG.info("Registering {}", algorithm);
            this.localAlgorithmRepository.addAlgorithm(algorithm);
        }));
    }

    @Inject
    public void setLocalAlgorithmRepository(LocalAlgorithmRepository localAlgorithmRepository) {
        this.localAlgorithmRepository = localAlgorithmRepository;
    }

    @Inject
    public void setAlgorithms(Optional<Collection<IAlgorithm>> algorithms) {
        this.algorithms = Objects.requireNonNull(algorithms);
    }

}
