/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.encode.stream;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterFactory;
import org.n52.iceland.coding.encode.ResponseWriterKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ServiceResponseWriterFactory implements ResponseWriterFactory {

    private final StreamWriterRepository responseWriterFactory;

    @Inject
    public ServiceResponseWriterFactory(StreamWriterRepository responseWriterFactory) {
        this.responseWriterFactory = responseWriterFactory;
    }

    @Override
    public Set<ResponseWriterKey> getKeys() {
        return Collections.singleton(StreamingServiceResponseWriter.KEY);
    }

    @Override
    public ResponseWriter<?> create(ResponseWriterKey key) {
        return new StreamingServiceResponseWriter(responseWriterFactory);
    }

}
