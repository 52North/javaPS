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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.n52.javaps.algorithm.AbstractAlgorithm;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.io.GeneratorRepository;
import org.n52.javaps.io.ParserRepository;
import org.n52.javaps.ogc.wps.description.ProcessDescription;

public class AnnotatedAlgorithm extends AbstractAlgorithm {
    private static final Map<Class<?>, AnnotatedAlgorithmMetadata> CACHE = new ConcurrentHashMap<>();
    private final AnnotatedAlgorithmMetadata metadata;
    private final Object algorithmInstance;

    @Inject
    public AnnotatedAlgorithm(ParserRepository parserRepository, GeneratorRepository generatorRepository) {
        this(parserRepository, generatorRepository, null);
    }

    public AnnotatedAlgorithm(ParserRepository parserRepository, GeneratorRepository generatorRepository,
                              Object algorithmInstance) {
        this.algorithmInstance = algorithmInstance == null ? this : algorithmInstance;
        this.metadata = CACHE
                .computeIfAbsent(this.algorithmInstance.getClass(), c ->
                        new AnnotatedAlgorithmMetadata(c, parserRepository, generatorRepository));
    }

    @Override
    protected ProcessDescription createDescription() {
        return this.metadata.getDescription();
    }

    @Override
    public ProcessOutputs run(ProcessInputs inputs) {
        ProcessOutputs outputs = new ProcessOutputs();
        this.metadata.getInputBindings().forEach((id, binding) -> binding.set(this.algorithmInstance, inputs.get(id)));
        this.metadata.getExecuteBinding().execute(this.algorithmInstance);
        this.metadata.getOutputBindings().forEach((id, binding) -> outputs.put(id, binding.get(this.algorithmInstance)));
        return outputs;
    }
}
