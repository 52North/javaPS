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
package org.n52.javaps.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.wps.response.ExecuteResponse;
import org.n52.shetland.ogc.wps.response.GetResultResponse;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.stream.MissingStreamWriterException;
import org.n52.svalbard.encode.stream.StreamWriter;
import org.n52.svalbard.encode.stream.StreamWriterKey;
import org.n52.svalbard.encode.stream.StreamWriterRepository;
import org.n52.svalbard.encode.stream.UnsupportedStreamWriterInputException;
import org.n52.svalbard.encode.stream.xml.XmlStreamWriterKey;

import com.google.common.io.ByteStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamingRawWriter implements StreamWriter<OwsServiceResponse> {

    private static final Set<StreamWriterKey> KEYS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            new StreamWriterKey(ExecuteResponse.class, new MediaType()), new StreamWriterKey(GetResultResponse.class,
                    new MediaType()))));

    private final StreamWriterRepository streamWriterRepository;

    @Inject
    public StreamingRawWriter(StreamWriterRepository streamWriterRepository) {
        this.streamWriterRepository = Objects.requireNonNull(streamWriterRepository);
    }

    @Override
    public void write(OwsServiceResponse object,
            OutputStream outputStream) throws EncodingException {

        Result result = null;

        if (object instanceof GetResultResponse) {
            result = ((GetResultResponse) object).getResult();
        } else if (object instanceof ExecuteResponse) {
            result = ((ExecuteResponse) object).getResult().filter(r -> r.getResponseMode() == ResponseMode.RAW)
                    .orElseThrow(() -> new UnsupportedStreamWriterInputException(object));
        } else {
            throw new UnsupportedStreamWriterInputException(object);
        }

        ProcessData data = result.getOutputs().iterator().next();
        if (data.isValue()) {
            try (InputStream dataStream = data.asValue().getData()) {
                ByteStreams.copy(dataStream, outputStream);
            } catch (IOException ex) {
                throw new EncodingException(ex);
            }
        } else if (data.isReference() || data.isGroup()) {
            getStreamWriter(data).write(data, outputStream);
        } else {
            throw new UnsupportedStreamWriterInputException(object);
        }
    }

    private <T> StreamWriter<? super T> getStreamWriter(T data) {
        StreamWriterKey key = new XmlStreamWriterKey(data.getClass());
        return streamWriterRepository.getWriter(key).orElseThrow(() -> new MissingStreamWriterException(key));
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return KEYS;
    }
}
