/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.stream.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.n52.janmayen.component.AbstractSimilarityKeyRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.svalbard.encode.stream.StreamWriterKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ElementXmlStreamWriterRepository extends AbstractSimilarityKeyRepository<StreamWriterKey,
        ElementXmlStreamWriter> implements Constructable {

    private Collection<Provider<ElementXmlStreamWriter>> writers;

    public ElementXmlStreamWriterRepository(Collection<Provider<ElementXmlStreamWriter>> writers) {
        this.writers = writers;
        if (writers != null) {
            init();
        }
    }

    public ElementXmlStreamWriterRepository() {
        this(null);
    }

    @Inject
    public void set(Optional<Collection<Provider<ElementXmlStreamWriter>>> writers) {
        this.writers = writers.orElseGet(Collections::emptyList);
    }

    @Override
    public void init() {
        Objects.requireNonNull(this.writers);
        setProducers(this.writers);
    }

    public Optional<ElementXmlStreamWriter> get(StreamWriterKey key,
            XmlStreamWritingContext ctx) {
        Optional<ElementXmlStreamWriter> writer = get(key);
        writer.ifPresent(x -> x.setContext(ctx));
        return writer;
    }

    @Override
    public Set<StreamWriterKey> keys() {
        return super.keys();
    }

}
