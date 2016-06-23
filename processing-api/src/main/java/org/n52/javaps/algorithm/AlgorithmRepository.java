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

import java.util.Optional;
import java.util.Set;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.n52.javaps.ogc.wps.description.ProcessDescription;
import org.n52.javaps.ogc.ows.OwsCode;

/**
 * @author Bastian Schaeffer, University of Muenster, Theodor Foerster, ITC
 *
 */
public interface AlgorithmRepository extends Constructable, Destroyable {

    public Set<OwsCode> getAlgorithmNames();

    Optional<IAlgorithm> getAlgorithm(OwsCode id);

    default Optional<IAlgorithm> getAlgorithm(String id) {
        return getAlgorithm(new OwsCode(id));
    }

    Optional<ProcessDescription> getProcessDescription(OwsCode id);

    default Optional<ProcessDescription> getProcessDescription(String id) {
        return getProcessDescription(new OwsCode(id));
    }

    boolean containsAlgorithm(OwsCode id);

    default boolean containsAlgorithm(String id) {
        return containsAlgorithm(new OwsCode(id));
    }

    @Override
    default void init() {}

    @Override
    default void destroy() {}
}
