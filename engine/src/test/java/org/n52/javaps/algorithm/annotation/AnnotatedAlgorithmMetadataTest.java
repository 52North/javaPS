/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.algorithm.annotation;

import static org.hamcrest.Matchers.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
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

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AnnotatedAlgorithmMetadataTest {

    private static final Set<Format> FORMATS = new HashSet<>(Arrays.asList(
            new Format("text/xml", "UTF-8", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", "UTF-16", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", (String)null, "http://www.opengis.net/gml/3.2"),
            new Format("text/xml"),
            new Format("text/xml", "UTF-8"),
            new Format("text/xml", "UTF-16")));

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void test() {
        IORepo repo = new IORepo();
        AnnotatedAlgorithmMetadata metadata = new AnnotatedAlgorithmMetadata(TestProcess.class, repo, repo, new LiteralDataManagerImpl());
        ProcessDescription processDescription = metadata.getDescription();

        errors.checkThat(processDescription.getVersion(), is("1.0.0"));
        errors.checkThat(processDescription.getTitle().getValue(), is("Test Process"));
        errors.checkThat(processDescription.getAbstract().map(OwsLanguageString::getValue).orElse(null), is("Test Abstract"));
        errors.checkThat(processDescription.getId(), is(new OwsCode(TestProcess.class.getCanonicalName())));
        errors.checkThat(processDescription.getInputDescriptions().size(), is(8));
        errors.checkThat(processDescription.getOutputDescriptions().size(), is(7));

        errors.checkThat(processDescription.getInput("input8").getOccurence().getMax().get(),is(BigInteger.TEN));
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
                      title = "input2 title",
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
        public Optional<OutputHandler> getOutputHandler(
                Format format, Class<? extends Data<?>> binding) {
            return Optional.empty();
        }

        @Override
        public Set<InputHandler> getInputHandlers() {
            return Collections.emptySet();
        }

        @Override
        public Optional<InputHandler> getInputHandler(
                Format format, Class<? extends Data<?>> binding) {
            return Optional.empty();
        }

        @Override
        public Set<Format> getSupportedFormats() {
            return Collections.unmodifiableSet(FORMATS);
        }

        @Override
        public Set<Format> getSupportedFormats(
                Class<? extends Data<?>> binding) {
            return getSupportedFormats();
        }
    }

    static class LiteralDataManagerImpl implements LiteralTypeRepository {

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
