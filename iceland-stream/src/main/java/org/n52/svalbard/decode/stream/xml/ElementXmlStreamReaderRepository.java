/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.stream.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.n52.janmayen.component.AbstractSimilarityKeyRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.svalbard.decode.stream.StreamReaderKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ElementXmlStreamReaderRepository extends AbstractSimilarityKeyRepository<StreamReaderKey,
        ElementXmlStreamReader> implements Constructable {

    private Collection<Provider<ElementXmlStreamReader>> readers;

    public ElementXmlStreamReaderRepository(Collection<Provider<ElementXmlStreamReader>> readers) {
        this.readers = readers;
        if (readers != null) {
            init();
        }
    }

    public ElementXmlStreamReaderRepository() {
        this(null);
    }

    @Inject
    public void set(Optional<Collection<Provider<ElementXmlStreamReader>>> readers) {
        this.readers = readers.orElseGet(Collections::emptyList);
    }

    @Override
    public void init() {
        Objects.requireNonNull(this.readers);
        setProducers(this.readers);
    }

    public Optional<ElementXmlStreamReader> get(XmlStreamReaderKey key) {
        Optional<ElementXmlStreamReader> reader = super.get(key);
        // writer.ifPresent(x -> x.setContext(ctx));
        return reader;
    }

    @Override
    public Set<StreamReaderKey> keys() {
        return super.keys();
    }

}
