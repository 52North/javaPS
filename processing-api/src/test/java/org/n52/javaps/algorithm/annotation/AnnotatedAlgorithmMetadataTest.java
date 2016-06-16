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
package org.n52.javaps.algorithm.annotation;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.javaps.description.Format;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.io.GeneratorRepository;
import org.n52.javaps.io.IGenerator;
import org.n52.javaps.io.IParser;
import org.n52.javaps.io.ParserRepository;
import org.n52.javaps.io.data.IComplexData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AnnotatedAlgorithmMetadataTest {

    private static final Set<Format> FORMATS = new HashSet<>(Arrays.asList(
            new Format("text/xml", "UTF-8", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", "UTF-16", "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", null, "http://www.opengis.net/gml/3.2"),
            new Format("text/xml", null, null),
            new Format("text/xml", "UTF-8", null),
            new Format("text/xml", "UTF-16", null)));

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void test() {
        AnnotatedAlgorithmMetadata metadata = new AnnotatedAlgorithmMetadata(TestProcess.class, new ParserRepositoryImpl(), new GeneratorRepositoryImpl());
        ProcessDescription processDescription = metadata.getDescription();

        errors.checkThat(processDescription.getVersion(), is("1.0.0"));
        errors.checkThat(processDescription.getTitle().getValue(), is("Test Process"));
        errors.checkThat(processDescription.getAbstract().map(OwsLanguageString::getValue).orElse(null), is("Test Abstract"));
        errors.checkThat(processDescription.getId(), is(new OwsCodeType(TestProcess.class.getCanonicalName())));
        errors.checkThat(processDescription.getInputDescriptions().size(), is(8));
        errors.checkThat(processDescription.getOutputDescriptions().size(), is(7));
    }

    public static enum TestEnum {
        A,
        B,
        C
    }

    public static class TestIData implements IComplexData {
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

    private static class GeneratorRepositoryImpl implements GeneratorRepository {

        @Override
        public Set<IGenerator> getGenerators() {
            return Collections.emptySet();
        }

        @Override
        public Optional<IGenerator> getGenerator(
                Format format, Class<? extends IComplexData> binding) {
            return Optional.empty();
        }

        @Override
        public Set<Format> getSupportedFormats() {
            return Collections.unmodifiableSet(FORMATS);
        }

        @Override
        public Set<Format> getSupportedFormats(
                Class<? extends IComplexData> binding) {
            return getSupportedFormats();
        }
    }

    private static class ParserRepositoryImpl implements ParserRepository {

        @Override
        public Set<IParser> getParsers() {
            return Collections.emptySet();
        }

        @Override
        public Optional<IParser> getParser(
                Format format, Class<? extends IComplexData> binding) {
            return Optional.empty();
        }

        @Override
        public Set<Format> getSupportedFormats() {
            return Collections.unmodifiableSet(FORMATS);
        }

        @Override
        public Set<Format> getSupportedFormats(
                Class<? extends IComplexData> binding) {
            return getSupportedFormats();
        }
    }
}
