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

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamWriterRepository
        extends AbstractSimilarityKeyRepository<StreamWriterKey, StreamWriter<?>, StreamWriterFactory>
        implements Constructable {

    @Autowired(required = false)
    private Collection<StreamWriter<?>> writers;
    @Autowired(required = false)
    private Collection<StreamWriterFactory> writerFactories;

    @Override
    public void init() {
        setProducers(getProviders(writers, writerFactories));
    }

    public <T> Optional<StreamWriter<T>> getWriter(MediaType mediaType, Class<? extends T> type) {
        return getWriter(new StreamWriterKey(type, mediaType));
    }

    public <T> Optional<StreamWriter<T>> getWriter(StreamWriterKey key) {
        @SuppressWarnings("unchecked")
        StreamWriter<T> writer = (StreamWriter<T>) get(key);
        return Optional.ofNullable(writer);
    }

}
