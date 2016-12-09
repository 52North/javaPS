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
package org.n52.javaps.service.xml;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Provider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.svalbard.encode.stream.StreamWriter;
import org.n52.svalbard.encode.stream.StreamWriterRepository;
import org.n52.svalbard.encode.stream.xml.DocumentXmlStreamWriter;
import org.n52.svalbard.encode.stream.xml.ElementXmlStreamWriter;
import org.n52.svalbard.encode.stream.xml.ElementXmlStreamWriterRepository;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ProcessOfferings;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.janmayen.http.MediaTypes;
import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.algorithm.annotation.AnnotatedAlgorithmMetadata;
import org.n52.javaps.algorithm.annotation.ComplexInput;
import org.n52.javaps.algorithm.annotation.ComplexOutput;
import org.n52.javaps.algorithm.annotation.Execute;
import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.algorithm.annotation.LiteralOutput;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandler;
import org.n52.javaps.io.OutputHandlerRepository;
import org.n52.javaps.io.complex.ComplexData;
import org.n52.javaps.io.literal.LiteralType;
import org.n52.javaps.io.literal.LiteralTypeRepository;
import org.n52.javaps.io.literal.xsd.LiteralIntType;
import org.n52.javaps.io.literal.xsd.LiteralStringType;
import org.n52.shetland.ogc.wps.response.DescribeProcessResponse;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WpsProcessDescriptionWriterTest {

    private static final List<Provider<ElementXmlStreamWriter>> ELEMENT_WRITERS
            = Arrays.asList(WPSResponseWriter::new, WPSWriter::new);

    @Test
    public void test() throws EncodingException {
        StreamWriterRepository repo = createRepository();

        ProcessOffering processOffering = new ProcessOffering(getProcessDescription());

        DescribeProcessResponse response = new DescribeProcessResponse();

        response.setService("WPS");
        response.setVersion("2.0.0");
        response.setContentType(MediaTypes.APPLICATION_XML);

        response.setOfferings(new ProcessOfferings(Collections.singleton(processOffering)));

        write(repo, response, System.out);

    }

    private static final Set<Format> FORMATS = new HashSet<>(Arrays.asList(
            new Format("text/xml", "UTF-8", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", "UTF-16", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", (String)null, "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", (String)null, null),
            new Format("text/xml", "UTF-8", null),
            new Format("text/xml", "UTF-16", null)));

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    public ProcessDescription getProcessDescription() {
        IORepo ioRepo = new IORepo();
        return new AnnotatedAlgorithmMetadata(TestProcess.class, ioRepo, ioRepo, new LiteralDataManagerImpl()).getDescription();
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

    public static enum TestEnum {
        A,
        B,
        C
    }

    public static class TestIData implements ComplexData<Object> {
        private static final long serialVersionUID = 8586931812896959156L;
        private final Object object;

        public TestIData(Object object) {
            this.object = object;
        }

        @Override
        public Object getPayload() {
            return this.object;
        }

        @Override
        public Class<?> getSupportedClass() {
            return Object.class;
        }
    }

    @Algorithm(title = "Test Process",
               abstrakt = "Test Abstract",
               version = "1.0.0")
    public static class TestProcess {

        @LiteralInput(identifier = "input5",
                      abstrakt = "input5 abstract",
                      title = "input5 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      defaultValue = "A",
                      uom = "m")
        public TestEnum input5;

        @ComplexInput(identifier = "input6",
                      abstrakt = "input6 abstract",
                      title = "input6 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      maximumMegaBytes = 10,
                      binding = TestIData.class)
        public Object input6;

        @LiteralInput(identifier = "input7",
                      title = "input7 title",
                      abstrakt = "input7 abstract",
                      minOccurs = 1,
                      maxOccurs = 1,
                      defaultValue = "1",
                      uom = "m")
        public int input7;

        @LiteralInput(identifier = "input8",
                      title = "input8 title",
                      abstrakt = "input8 abstract",
                      minOccurs = 1,
                      maxOccurs = 10,
                      defaultValue = "1",
                      uom = "m")
        public List<Integer> input8;
        @LiteralOutput(identifier = "output5",
                       abstrakt = "output5 abstract",
                       title = "output5 title",
                       uom = "m")
        public TestEnum output5;
        @ComplexOutput(identifier = "output6",
                       abstrakt = "output6 abstract",
                       title = "output6 title",
                       binding = TestIData.class)
        public Object output6;
        @LiteralOutput(identifier = "output7",
                       title = "output7 title",
                       abstrakt = "output7 abstract",
                       uom = "m")
        public int output7;

        @LiteralInput(identifier = "input1",
                      abstrakt = "input1 abstract",
                      title = "input1 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      defaultValue = "1", uom = "m")
        public void setInput1(int input) {
        }

        @LiteralInput(identifier = "input2",
                      abstrakt = "input2 abstract",
                      title
                      = "input2 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      defaultValue = "asdf",
                      uom = "m")
        public void setInput2(String input) {
        }

        @ComplexInput(identifier = "input3",
                      abstrakt = "input3 abstract",
                      title = "input3 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      maximumMegaBytes = 10,
                      binding = TestIData.class)
        public void setInput3(Object input) {
        }

        @LiteralInput(identifier = "input4",
                      abstrakt = "input4 abstract",
                      title = "input4 title",
                      minOccurs = 1,
                      maxOccurs = 1,
                      defaultValue = "A",
                      uom = "m")
        public void setInput4(TestEnum input) {
        }

        @Execute
        public void execute() {
        }

        @LiteralOutput(identifier = "output1",
                       abstrakt = "output1 abstract",
                       title = "output1 title",
                       uom = "m")
        public int getOutput1() {
            return 0;
        }

        @LiteralOutput(identifier = "output2",
                       abstrakt = "output2 abstract",
                       title = "output2 title",
                       uom = "m")
        public String getOutput2() {
            return null;
        }

        @ComplexOutput(identifier = "output3",
                       abstrakt = "output3 abstract",
                       title = "output3 title",
                       binding = TestIData.class)
        public Object getOutput3() {
            return null;
        }

        @LiteralOutput(identifier = "output4",
                       abstrakt = "output4 abstract",
                       title = "output4 title",
                       uom = "m")
        public TestEnum getOutput4() {
            return null;
        }
    }

    private static class IORepo implements OutputHandlerRepository, InputHandlerRepository {

        @Override
        public Set<OutputHandler> getOutputHandlers() {
            return Collections.emptySet();
        }

        @Override
        public Optional<OutputHandler> getOutputHandler(Format format, Class<? extends Data<?>> binding) {
            return Optional.empty();
        }

        @Override
        public Set<Format> getSupportedFormats() {
            return Collections.unmodifiableSet(FORMATS);
        }

        @Override
        public Set<Format> getSupportedFormats(Class<? extends Data<?>> binding) {
            return getSupportedFormats();
        }
        @Override
        public Set<InputHandler> getInputHandlers() {
            return Collections.emptySet();
        }

        @Override
        public Optional<InputHandler> getInputHandler(Format format, Class<? extends Data<?>> binding) {
            return Optional.empty();
        }
    }

    private static class LiteralDataManagerImpl implements LiteralTypeRepository {
        @Override
        @SuppressWarnings("unchecked")
        public <T> LiteralType<T> getLiteralType(
                Class<? extends LiteralType<?>> literalType, Class<?> payloadType) {

            if (literalType == null || literalType.equals(LiteralType.class)) {
                if (payloadType != null) {
                    if (payloadType.equals(String.class)) {
                        return (LiteralType<T>) new LiteralStringType();
                    } else if (payloadType.equals(Integer.class)) {
                        return (LiteralType<T>) new LiteralIntType();
                    } else {
                        throw new Error("Unsupported payload type");
                    }
                } else {
                    throw new Error("Neither payload type nro literal type given");
                }
            } else {
                try {
                    return (LiteralType<T>) literalType.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new Error(ex);
                }
            }
        }
    }
}
