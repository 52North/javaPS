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
package org.n52.javaps.coding.stream.xml.impl;

import java.util.Collections;
import java.util.Set;

import org.n52.javaps.coding.stream.StreamWriter;
import org.n52.javaps.coding.stream.StreamWriterFactory;
import org.n52.javaps.coding.stream.StreamWriterKey;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Ows20StreamWriterFactory implements StreamWriterFactory {

    private static final ImmutableSet<StreamWriterKey> KEYS
            = ImmutableSet.<StreamWriterKey>builder()
            .add(Ows20ExceptionReportWriter.KEY)
            .build();

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public StreamWriter<?> create(StreamWriterKey key) {
        if (key.equals(Ows20ExceptionReportWriter.KEY)) {
            return new Ows20ExceptionReportWriter();
        }
        throw new IllegalArgumentException();
    }

}
