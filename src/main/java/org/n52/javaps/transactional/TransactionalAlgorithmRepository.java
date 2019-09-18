/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.transactional;

import org.n52.javaps.algorithm.AlgorithmRepository;
import org.n52.javaps.algorithm.IAlgorithm;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;

import java.util.Optional;
import java.util.Set;

public interface TransactionalAlgorithmRepository extends AlgorithmRepository {
    Set<OwsCode> getAlgorithmNames();

    Optional<IAlgorithm> getAlgorithm(OwsCode id);

    @Override
    default Optional<TypedProcessDescription> getProcessDescription(OwsCode id) {
        return getAlgorithm(id).map(IAlgorithm::getDescription);
    }

    @Override
    default boolean containsAlgorithm(OwsCode id) {
        return getApplicationPackage(id).isPresent();
    }

    default Optional<ApplicationPackage> getApplicationPackage(String id) {
        return getApplicationPackage(new OwsCode(id));
    }

    Optional<ApplicationPackage> getApplicationPackage(OwsCode id);

    OwsCode register(ApplicationPackage applicationPackage)
            throws DuplicateProcessException, UnsupportedProcessException;

    boolean isSupported(ApplicationPackage applicationPackage);

    void unregister(OwsCode id) throws ProcessNotFoundException;

}
