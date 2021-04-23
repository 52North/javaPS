/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Provider;

import org.junit.Test;

import org.n52.svalbard.encode.stream.StreamWriter;
import org.n52.svalbard.encode.stream.StreamWriterRepository;
import org.n52.svalbard.encode.stream.xml.DocumentXmlStreamWriter;
import org.n52.svalbard.encode.stream.xml.ElementXmlStreamWriter;
import org.n52.svalbard.encode.stream.xml.ElementXmlStreamWriterRepository;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.InMemoryValueProcessData;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.io.BaseEncoding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSResponseWriterTest {

    private static final List<Provider<ElementXmlStreamWriter>> ELEMENT_WRITERS
            = Arrays.asList(WPSResponseWriter::new, WPSWriter::new);


    @Test
    public void test() throws EncodingException {
        BaseEncoding.base64().encode("<sömekey>somevalüe</sömekey>".getBytes(StandardCharsets.UTF_8));

        Result result = new Result();
        result.setJobId(new JobId("the-job-id"));
        result.setExpirationDate(OffsetDateTime.now());
        result.addOutput(new ReferenceProcessData(new OwsCode("output1"), new Format("application/xml"), URI.create("http://example.com/output1")));
        result.addOutput(new ReferenceProcessData(new OwsCode("output2"), new Format("application/json"), URI.create("http.//example.com/output2")));
        GroupProcessData group = new GroupProcessData(new OwsCode("output3"));
        result.addOutput(group);
        group.addElement(new InMemoryValueProcessData(new OwsCode("output4"), new Format("application/json"), "{\"soamekey\": \"somevalue\"}".getBytes(StandardCharsets.UTF_8)));
        group.addElement(new InMemoryValueProcessData(new OwsCode("output5"), new Format("application/xml", "ISO-8859-1"), "<sömekey>somevalüe</sömekey>".getBytes(StandardCharsets.ISO_8859_1)));
        group.addElement(new InMemoryValueProcessData(new OwsCode("output6"), new Format("application/xml"), "<sömekey>somevalüe</sömekey>".getBytes(StandardCharsets.UTF_8)));
        result.addOutput(new InMemoryValueProcessData(new OwsCode("output7"), new Format("application/xml").withBase64Encoding(), BaseEncoding.base64().encode("<sömekey>somevalüe</sömekey>".getBytes(StandardCharsets.UTF_8)).getBytes(StandardCharsets.US_ASCII)));
        ReferenceProcessData output;
        output = new ReferenceProcessData(new OwsCode("output8"), new Format("application/xml").withUTF8Encoding(), URI.create("http://example.com/asdf"));
        output.setBody(Body.reference(URI.create("http://example.com/body")));
        result.addOutput(output);

        output = new ReferenceProcessData(new OwsCode("output9"), new Format("application/xml").withUTF8Encoding(), URI.create("http://example.com/asdf"));
        output.setBody(Body.inline("<sömekey>somevalüe</sömekey>"));
        result.addOutput(output);

        write(createRepository(), result, System.out);
    }

    private StreamWriterRepository createRepository() {
        DocumentXmlStreamWriter writer
                = new DocumentXmlStreamWriter(new ElementXmlStreamWriterRepository(ELEMENT_WRITERS));
        StreamWriterRepository repository = new StreamWriterRepository();
        repository.set(Optional.of(Arrays.asList(() -> writer)));
        repository.init();
        return repository;
    }

    private <T> void write(StreamWriterRepository repo, T object, OutputStream out)
            throws EncodingException {
        @SuppressWarnings("unchecked")
        Class<? extends T> aClass = (Class<? extends T>) object.getClass();
        Optional<StreamWriter<? super T>> writer = repo.getWriter(MediaTypes.APPLICATION_XML, aClass);

        if (writer.isPresent()) {
            writer.get().write(object, out);
        }
    }
}
