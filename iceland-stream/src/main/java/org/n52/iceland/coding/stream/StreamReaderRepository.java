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
package org.n52.iceland.coding.stream;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.component.AbstractSimilarityKeyRepository;
import org.n52.iceland.lifecycle.Constructable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamReaderRepository
        extends AbstractSimilarityKeyRepository<StreamReaderKey, StreamReader<?>>
        implements Constructable {

    private static final Logger LOG = LoggerFactory.getLogger(StreamReaderRepository.class);

    private Collection<Provider<StreamReader<?>>> readers;

    @Override
    public void init() {
        Objects.requireNonNull(this.readers);
        setProducers(this.readers);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<StreamReader<T>> getReader(StreamReaderKey key) {
        return get(key).map(x -> (StreamReader<T>) x);
    }

    @Inject
    public void set(Optional<Collection<Provider<StreamReader<?>>>> readers) {
        this.readers = readers.orElseGet(Collections::emptyList);
    }

}
