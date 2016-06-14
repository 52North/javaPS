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
package org.n52.javaps.algorithm;

import static org.n52.javaps.description.annotation.parser.AnnotatedAlgorithmIntrospector.getInstrospector;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.annotation.binding.ExecuteMethodBinding;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.annotation.parser.AnnotatedAlgorithmIntrospector;
import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.ParserFactory;

/**
 *
 * @author tkunicki
 */
public abstract class AbstractAnnotatedAlgorithm extends AbstractDescriptorAlgorithm {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractAnnotatedAlgorithm.class);

    @Override
    protected ProcessDescription createDescription() {
        return getInstrospector(getAlgorithmClass()).getProcessDescription();
    }

    @Override
    public ProcessOutputs run(ProcessInputs inputs) {
        ProcessOutputs outputs = new ProcessOutputs();

        Object algorithm = getAlgorithmInstance();
        AnnotatedAlgorithmIntrospector introspector = getInstrospector(algorithm.getClass());
        Map<OwsCodeType, InputBinding<?, ?>> inputBindings = introspector.getInputBindingMap();
        Map<OwsCodeType, OutputBinding<?, ?>> outputBindings = introspector.getOutputBindingMap();
        ExecuteMethodBinding executeBinding = introspector.getExecuteMethodBinding();

        inputBindings.forEach((id, binding) -> binding.set(algorithm, inputs.get(id)));
        executeBinding.execute(algorithm);
        outputBindings.forEach((id, binding) -> outputs.put(id, binding.get(algorithm)));

        return outputs;
    }

    public Object getAlgorithmInstance() {
        return this;
    }

    public Class<?> getAlgorithmClass() {
        return getClass();
    }

    public static class Proxy extends AbstractAnnotatedAlgorithm {

        private final Class<?> proxiedClass;
        private final Object proxiedInstance;

        public Proxy(Class<?> proxiedClass) {
            this.proxiedClass = proxiedClass;
            try {
                this.proxiedInstance = proxiedClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException("unable to instantiate proxied algorithm instance");
            }
        }

        @Override
        public Class<?> getAlgorithmClass() {
            return proxiedClass;
        }

        @Override
        public Object getAlgorithmInstance() {
            return proxiedInstance;
        }

        @Override
        public void setGeneratorFactory(GeneratorFactory generatorFactory) {
        }

        @Override
        public void setParserFactory(ParserFactory parserFactory) {
        }
    }

}
