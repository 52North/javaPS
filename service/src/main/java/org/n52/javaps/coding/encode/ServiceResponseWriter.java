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
package org.n52.javaps.coding.encode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.http.MediaType;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.StreamWriterRepository;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ServiceResponseWriter implements ResponseWriter<AbstractServiceResponse> {
    static final ResponseWriterKey KEY = new ResponseWriterKey(AbstractServiceResponse.class);

    private MediaType contentType;
    private final StreamWriterRepository streamWriterRepository;

    public ServiceResponseWriter(
            StreamWriterRepository streamWriterRepository) {
        this.streamWriterRepository = streamWriterRepository;
    }

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    @Override
    public void write(AbstractServiceResponse t, OutputStream out, ResponseProxy responseProxy)
            throws IOException, OwsExceptionReport {
        try {
            streamWriterRepository.getWriter(new StreamWriterKey(t.getClass(), t.getContentType()))
                    .orElseThrow(() -> new NoApplicableCodeException()
                            .withMessage("No response encoder forund for %s",
                                         t.getClass()))
                    .write(t, out);
        } catch (XMLStreamException ex) {
            throw new NoApplicableCodeException().causedBy(ex)
                    .withMessage("Could not write response");
        }
    }

    @Override
    public boolean supportsGZip(AbstractServiceResponse t) {
        return true;
    }

    @Override
    public Set<ResponseWriterKey> getKeys() {
        return Collections.singleton(KEY);

    }
}
