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
package org.n52.javaps.coding.stream;

import org.n52.iceland.component.AbstractSimilarityKeyRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamWriterRepository
        extends AbstractSimilarityKeyRepository<StreamWriterKey, StreamWriter<?>>
        implements Constructable {

    private Collection<Provider<StreamWriter<?>>> writers;

    @Override
    public void init() {
        Objects.requireNonNull(this.writers);
        setProducers(this.writers);
    }

    public <T> Optional<StreamWriter<? super T>> getWriter(MediaType mediaType, Class<? extends T> type) {
        return getWriter(new StreamWriterKey(type, mediaType));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<StreamWriter<? super T>> getWriter(StreamWriterKey key) {
        return get(key).map(x -> (StreamWriter<T>)x);
    }

    @Autowired(required = false)
    public void set(Collection<Provider<StreamWriter<?>>> writers) {
        this.writers = writers;
    }
}
