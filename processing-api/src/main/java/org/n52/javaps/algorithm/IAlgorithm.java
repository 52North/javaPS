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

import java.util.Collections;
import java.util.List;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.ParserFactory;
import org.n52.javaps.io.data.IData;

/**
 * @author Bastian Schaeffer, University of Muenster, Theodor Foerster, ITC
 *
 */
public interface IAlgorithm {

    ProcessOutputs run(ProcessInputs inputData) throws ExecutionException;

    default List<String> getErrors() {
        return Collections.emptyList();
    }

    ProcessDescription getDescription();

    /**
     * Checks if the processDescription complies to the process itself and fits
     * any schema or other dependencies.
     *
     * @param version
     *                The version of the ProcessDescription to check
     *
     * @return true if the ProcessDescription is valid, false otherwise
     */
    boolean processDescriptionIsValid(String version);

    default Class<?> getInputDataType(String id) {
        return getOutputDataType(new OwsCodeType(id));
    }

    default Class<?> getOutputDataType(String id) {
        return getOutputDataType(new OwsCodeType(id));
    }
    default Class<? extends IData> getInputDataType(OwsCodeType identifier) {
        if (getDescription() != null) {
            return getDescription().getInput(identifier).getBindingClass();
        } else {
            throw new IllegalStateException("Instance must have an process description");
        }
    }

    default Class<? extends IData> getOutputDataType(OwsCodeType identifier) {
        if (getDescription() != null) {
            return getDescription().getOutput(identifier).getBindingClass();
        } else {
            throw new IllegalStateException("Instance must have an process description");
        }
    }

    void setGeneratorFactory(GeneratorFactory generatorFactory);

    void setParserFactory(ParserFactory parserFactory);

}
