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

import java.util.List;

import javax.inject.Inject;

import org.n52.iceland.ogc.wps.OutputDefinition;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessDescription;
import org.n52.javaps.algorithm.ProcessOutputs;
import org.n52.javaps.io.OutputHandlerRepository;

public class ProcessOutputEncoderImpl implements ProcessOutputEncoder {

    private final OutputHandlerRepository outputHandlerRepository;

    @Inject
    public ProcessOutputEncoderImpl(OutputHandlerRepository outputHandlerRepository) {
        this.outputHandlerRepository = outputHandlerRepository;
    }

    @Override
    public Result create(TypedProcessDescription description, List<OutputDefinition> outputDefinitions,
                         ProcessOutputs outputs) {
        /* TODO implement org.n52.javaps.ProcessOutputEncoderImpl.create() */
        throw new UnsupportedOperationException("org.n52.javaps.ProcessOutputEncoderImpl.create() not yet implemented");
    }

}
