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
package org.n52.javaps.engine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.OutputHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GeneratingProcessData extends ValueProcessData {

    private final OutputHandler outputHandler;

    private final TypedProcessOutputDescription<?> outputDescription;

    private final Data<?> data;

    private final OutputDefinition outputDefinition;

    GeneratingProcessData(TypedProcessOutputDescription<?> outputDescription, OutputDefinition outputDefinition,
            OutputHandler outputHandler, Data<?> data) {
        super(outputDescription.getId(), outputDefinition.getFormat());
        this.outputHandler = Objects.requireNonNull(outputHandler);
        this.outputDescription = Objects.requireNonNull(outputDescription);
        this.outputDefinition = Objects.requireNonNull(outputDefinition);
        this.data = Objects.requireNonNull(data);
    }

    @Override
    public InputStream getData() throws IOException {
        try {
            return this.outputHandler.generate(this.outputDescription, this.data, this.outputDefinition.getFormat());
        } catch (EncodingException ex) {
            throw new IOException(ex);
        }
    }

}
