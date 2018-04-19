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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.engine.ProcessExecutionContext;
import org.n52.javaps.algorithm.AbstractAlgorithm;
import org.n52.javaps.algorithm.ExecutionException;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.OutputHandlerRepository;
import org.n52.javaps.io.literal.LiteralTypeRepository;

public class AnnotatedAlgorithm extends AbstractAlgorithm {
    private static final Map<Class<?>, AnnotatedAlgorithmMetadata> CACHE = new ConcurrentHashMap<>();
    private final AnnotatedAlgorithmMetadata metadata;
    private final Object algorithmInstance;

    @Inject
    public AnnotatedAlgorithm(InputHandlerRepository parserRepository,
                              OutputHandlerRepository generatorRepository,
                              LiteralTypeRepository literalTypeRepository) {
        this(parserRepository, generatorRepository, literalTypeRepository, null);
    }

    public AnnotatedAlgorithm(InputHandlerRepository parserRepository, OutputHandlerRepository generatorRepository,
                              LiteralTypeRepository literalDataManager, Object algorithmInstance) {
        this.algorithmInstance = algorithmInstance == null ? this : algorithmInstance;
        this.metadata = CACHE
                .computeIfAbsent(this.algorithmInstance.getClass(), c ->
                        new AnnotatedAlgorithmMetadata(c, parserRepository, generatorRepository, literalDataManager));
    }

    @Override
    protected TypedProcessDescription createDescription() {
        return this.metadata.getDescription();
    }

    @Override
    public void execute(ProcessExecutionContext context) throws ExecutionException {
        this.metadata.getInputBindings().forEach((id, binding) -> binding.set(this.algorithmInstance, context.getInputs().get(id)));
        this.metadata.getExecuteBinding().execute(this.algorithmInstance);
        this.metadata.getOutputBindings().forEach((id, binding) -> context.getOutputs().put(id, binding.get(this.algorithmInstance)));
    }
}
