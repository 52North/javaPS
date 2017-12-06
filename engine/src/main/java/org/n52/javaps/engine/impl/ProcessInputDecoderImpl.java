/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.engine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.engine.ProcessInputDecoder;
import org.n52.javaps.engine.UnsupportedInputFormatException;
import org.n52.javaps.algorithm.ProcessInputs;
import org.n52.javaps.description.TypedGroupInputDescription;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessInputDescriptionContainer;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.GroupInputData;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.InputHandlerRepository;

public class ProcessInputDecoderImpl implements ProcessInputDecoder {

    private final InputHandlerRepository inputHandlerRepository;

    @Inject
    public ProcessInputDecoderImpl(InputHandlerRepository inputHandlerRepository) {
        this.inputHandlerRepository = inputHandlerRepository;
    }

    @Override
    public ProcessInputs decode(TypedProcessDescription description, List<ProcessData> processInputs)
            throws InputDecodingException {
        return decodeContainer(description, processInputs);
    }

    private ProcessInputs decodeContainer(TypedProcessInputDescriptionContainer description,
                                          List<ProcessData> processInputs) throws InputDecodingException {

        Map<OwsCode, List<Data<?>>> data = new HashMap<>(processInputs.size());
        for (ProcessData input : processInputs) {
            Data<?> decodedInput = decode(description.getInput(input.getId()), input);
            data.computeIfAbsent(input.getId(), id -> new LinkedList<>()).add(decodedInput);
        }
        return new ProcessInputs(data);
    }

    private Data<?> decode(TypedProcessInputDescription<?> description, ProcessData input) throws InputDecodingException {
        if (input.isGroup()) {
            return decodeGroup(description.asGroup(), input.asGroup());
        } else if (input.isReference()) {
            return decodeReference(description, input.asReference());
        } else if (input.isValue()) {
            return decodeValueInput(description, input.asValue());
        } else {
            throw new AssertionError("Unsupported input type: " + input);
        }
    }

    private Data<?> decodeGroup(TypedGroupInputDescription description, GroupProcessData input)
            throws InputDecodingException {
        return new GroupInputData(decodeContainer(description, input.getElements()));
    }

    private Data<?> decodeReference(TypedProcessInputDescription<?> description, ReferenceProcessData input)
            throws InputDecodingException {
        ValueProcessData resolve;
        try {
            resolve = new ResolvableReferenceProcessData(input).resolve();
        } catch (IOException ex) {
            throw new InputDecodingException(input.getId(), ex);
        }
        return decode(description, resolve);
    }

    private Data<?> decodeValueInput(TypedProcessInputDescription<?> description, ValueProcessData input)
            throws InputDecodingException {
        Format format = input.getFormat();
        Class<? extends Data<?>> bindingType = description.getBindingType();

        InputHandler handler = this.inputHandlerRepository
                .getInputHandler(format, bindingType)
                .orElseThrow(noHandlerFound(input.getId()));

        try (InputStream data = input.getData()) {
            return handler.parse(description, data, format);
        } catch (IOException | DecodingException ex) {
            throw new InputDecodingException(input.getId(), ex);
        }
    }

    private static Supplier<InputDecodingException> noHandlerFound(OwsCode id) {
        return () -> new InputDecodingException(id, new UnsupportedInputFormatException());
    }

}
