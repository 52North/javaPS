/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
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

import java.util.Set;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.algorithm.descriptor.ProcessDescription;

/**
 * @author Bastian Schaeffer, University of Muenster, Theodor Foerster, ITC
 *
 */
public interface IAlgorithmRepository extends Constructable, Destroyable {
    public Set<OwsCodeType> getAlgorithmNames();

    IAlgorithm getAlgorithm(OwsCodeType processID);

    default IAlgorithm getAlgorithm(String processId) {
        return getAlgorithm(new OwsCodeType(processId));
    }

    ProcessDescription getProcessDescription(OwsCodeType processID);

    default ProcessDescription getProcessDescription(String processID) {
        return getProcessDescription(new OwsCodeType(processID));
    }

    boolean containsAlgorithm(OwsCodeType processID);

    default boolean containsAlgorithm(String processId) {
        return containsAlgorithm(new OwsCodeType(processId));
    }

    @Override
    default void init() {}

    @Override
    default void destroy() {}
}
