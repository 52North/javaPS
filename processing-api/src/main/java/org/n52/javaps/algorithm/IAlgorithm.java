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

import org.n52.javaps.ogc.wps.description.ProcessDescription;
import org.n52.javaps.io.data.IData;
import org.n52.javaps.ogc.ows.OwsCode;

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

    default Class<?> getInputDataType(String id) {
        return getOutputDataType(new OwsCode(id));
    }

    default Class<?> getOutputDataType(String id) {
        return getOutputDataType(new OwsCode(id));
    }
    default Class<? extends IData> getInputDataType(OwsCode identifier) {
        if (getDescription() != null) {
            return getDescription().getInput(identifier).getBindingClass();
        } else {
            throw new IllegalStateException("Instance must have an process description");
        }
    }

    default Class<? extends IData> getOutputDataType(OwsCode identifier) {
        if (getDescription() != null) {
            return getDescription().getOutput(identifier).getBindingClass();
        } else {
            throw new IllegalStateException("Instance must have an process description");
        }
    }
}
