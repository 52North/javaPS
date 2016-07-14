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
package org.n52.javaps;

import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.data.GroupProcessData;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescriptionContainer;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.GroupOutputData;
import org.n52.javaps.io.OutputHandler;
import org.n52.javaps.io.OutputHandlerRepository;

import static java.util.stream.Collectors.toMap;

public class ProcessOutputEncoderImpl implements ProcessOutputEncoder {

    private final OutputHandlerRepository outputHandlerRepository;

    @Inject
    public ProcessOutputEncoderImpl(OutputHandlerRepository outputHandlerRepository) {
        this.outputHandlerRepository = outputHandlerRepository;
    }

    @Override
    public Result create(ProcessExecutionContext context) throws OutputEncodingException {
        Result result = new Result();
        result.setJobId(context.getJobId());
        // TODO set expiration date once the reference is working
        createData(context.getDescription(),
                   context.getOutputDefinitions(),
                   context.getOutputs(),
                   result::addOutput);
        return result;
    }

    private void createData(TypedProcessOutputDescriptionContainer description,
                            Collection<OutputDefinition> outputDefinitions,
                            ProcessOutputs outputs,
                            Consumer<ProcessData> sink) throws OutputEncodingException {
        Map<OwsCode, OutputDefinition> outputDefinitionsById = byId(outputDefinitions);
        for (Entry<OwsCode, Data<?>> output : outputs.entrySet()) {
            OwsCode id = output.getKey();
            TypedProcessOutputDescription<?> outputDescription = description.getOutput(id);
            OutputDefinition outputDefinition = outputDefinitionsById.get(id);
            Data<?> data = output.getValue();

            if (outputDescription.isGroup()) {
                sink.accept(createGroupData(outputDescription, outputDefinition, data));
            } else {
                sink.accept(createValueData(outputDescription, outputDefinition, data));
            }
        }
    }

    private Map<OwsCode, OutputDefinition> byId(Collection<OutputDefinition> definitions) {
        return definitions.stream().collect(toMap(OutputDefinition::getId, Function.identity()));
    }

    private ProcessData createValueData(TypedProcessOutputDescription<?> outputDescription,
                                        OutputDefinition outputDefinition, Data<?> data)
            throws OutputEncodingException {
        OutputHandler outputHandler = this.outputHandlerRepository
                .getOutputHandler(outputDefinition.getFormat(), data)
                .orElseThrow(noHandlerFound(outputDescription.getId()));
        return new GeneratingProcessData(outputDescription, outputDefinition, outputHandler, data);
    }

    private ProcessData createGroupData(TypedProcessOutputDescription<?> outputDescription,
                                        OutputDefinition outputDefinition, Data<?> data)
            throws OutputEncodingException {
        GroupProcessData groupProcessData = new GroupProcessData(outputDescription.getId());
        ProcessOutputs groupProcessOutputs = ((GroupOutputData) data).getPayload();

        createData(outputDescription.asGroup(),
                   outputDefinition.getOutputs(),
                   groupProcessOutputs,
                   groupProcessData::addElement);

        return groupProcessData;
    }

    private static Supplier<OutputEncodingException> noHandlerFound(OwsCode id) {
        return () -> new OutputEncodingException(id, new UnsupportedOutputFormatException());
    }

}
