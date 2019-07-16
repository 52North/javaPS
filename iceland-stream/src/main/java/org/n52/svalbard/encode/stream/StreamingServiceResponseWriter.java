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
package org.n52.svalbard.encode.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamingServiceResponseWriter implements ResponseWriter<OwsServiceResponse> {
    public static final ResponseWriterKey KEY = new ResponseWriterKey(OwsServiceResponse.class);

    private MediaType contentType;

    private final StreamWriterRepository repository;

    public StreamingServiceResponseWriter(StreamWriterRepository streamWriterRepository) {
        this.repository = streamWriterRepository;
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
    public void write(OwsServiceResponse t,
            OutputStream out,
            ResponseProxy responseProxy) throws IOException, EncodingException {
        StreamWriterKey key = new StreamWriterKey(t.getClass(), t.getContentType());
        StreamWriter<Object> writer = repository.getWriter(key).orElseThrow(() -> new NoEncoderForKeyException(key));
        writer.write(t, out);
    }

    @Override
    public boolean supportsGZip(OwsServiceResponse t) {
        return true;
    }

    @Override
    public Set<ResponseWriterKey> getKeys() {
        return Collections.singleton(KEY);

    }

    @Override
    public boolean hasForcedHttpStatus(OwsServiceResponse t) {
        if (t instanceof OwsExceptionReportResponse) {
            return ((OwsExceptionReportResponse) t).getOwsExceptionReport().getStatus() != null;
        }
        return ResponseWriter.super.hasForcedHttpStatus(t);
    }

    @Override
    public HTTPStatus getForcedHttpStatus(OwsServiceResponse t) {
        if (t instanceof OwsExceptionReportResponse) {
            OwsExceptionReportResponse exceptionReportResponse = (OwsExceptionReportResponse) t;
            return exceptionReportResponse.getOwsExceptionReport().getStatus();
        }
        return ResponseWriter.super.getForcedHttpStatus(t);
    }

}
