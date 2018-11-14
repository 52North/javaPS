/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.wps.javaps.rest.deserializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.data.FormattedProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;

import io.swagger.model.InlineValue;
import io.swagger.model.Input;
import io.swagger.model.Output;
import io.swagger.model.TransmissionMode;

public class ExecuteDeserializer {

    public static List<OutputDefinition> readOutputs(List<Output> outputs) {

        List<OutputDefinition> outputDefinitions = new ArrayList<>();

        for (Output output : outputs) {
            OutputDefinition outputDefinition = new OutputDefinition();

            outputDefinition.setId(createId(output.getId()));

            outputDefinition.setFormat(getFormat(output.getFormat()));

            outputDefinition.setDataTransmissionMode(getTransmisionMode(output.getTransmissionMode()));

            outputDefinitions.add(outputDefinition);
        }

        return outputDefinitions;
    }

    private static DataTransmissionMode getTransmisionMode(TransmissionMode transmissionMode) {
        switch (transmissionMode) {
        case REFERENCE:
            return DataTransmissionMode.REFERENCE;
        case VALUE:
            return DataTransmissionMode.VALUE;
        default:
            return DataTransmissionMode.REFERENCE;
        }
    }

    private static Format getFormat(io.swagger.model.Format format) {
        return new Format(format.getMimeType(), format.getEncoding(), format.getSchema());
    }

    private static OwsCode createId(String id) {
        return new OwsCode(id);
    }

    public static List<ProcessData> readInputs(List<Input> inputs) throws URISyntaxException {

        List<ProcessData> processDataList = new ArrayList<>();

        for (Input input : inputs) {

            ProcessData processData = null;

            OwsCode id = createId(input.getId());

            if (input.getValue().getInlineValue() != null) {
                processData =
                        new StringValueProcessData(id, getFormat(input.getFormat()), input.getValue().getInlineValue());

            } else if (input.getValue().getValueReference() != null) {

                URI uri = new URI(input.getValue().getValueReference());

                processData = new ReferenceProcessData(id, getFormat(input.getFormat()), uri);
            }

            processDataList.add(processData);
        }

        return processDataList;
    }

}
