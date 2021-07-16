/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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

/**
 * Transactional {@link AlgorithmRepository} that is able to accept {@linkplain ApplicationPackage application packages}
 * at runtime.
 *
 * @author Christian Autermann
 * @see ListenableTransactionalAlgorithmRepository
 */
public interface TransactionalAlgorithmRepository extends AlgorithmRepository {
    @Override
    default Optional<TypedProcessDescription> getProcessDescription(OwsCode id) {
        return getAlgorithm(id).map(IAlgorithm::getDescription);
    }

    @Override
    default boolean containsAlgorithm(OwsCode id) {
        return getApplicationPackage(id).isPresent();
    }

    /**
     * Get the {@link ApplicationPackage} with the specified {@code id}.
     *
     * @param id The identifier of the {@link ApplicationPackage}.
     * @return The {@link ApplicationPackage} or {@code Optional.empty()} if the identifier is unknown.
     */
    default Optional<ApplicationPackage> getApplicationPackage(String id) {
        return getApplicationPackage(new OwsCode(id));
    }

    /**
     * Get the {@link ApplicationPackage} with the specified {@code id}.
     *
     * @param id The identifier of the {@link ApplicationPackage}.
     * @return The {@link ApplicationPackage} or {@code Optional.empty()} if the identifier is unknown.
     */
    Optional<ApplicationPackage> getApplicationPackage(OwsCode id);

    /**
     * Checks if the {@link ApplicationPackage} is supported by this repository.
     *
     * @param applicationPackage The {@link ApplicationPackage}.
     * @return If the {@link ApplicationPackage} is supported.
     */
    boolean isSupported(ApplicationPackage applicationPackage);

    /**
     * Registers the {@link ApplicationPackage} in this repository.
     *
     * @param applicationPackage The {@link ApplicationPackage}.
     * @return The identifier of the {@link ApplicationPackage}.
     * @throws DuplicateProcessException   If the identifier of the {@link ApplicationPackage} already exists within
     *                                     this repository.
     * @throws UnsupportedProcessException If The {@link ApplicationPackage} is not supported.
     * @see #isSupported(ApplicationPackage)
     */
    OwsCode register(ApplicationPackage applicationPackage)
            throws DuplicateProcessException, UnsupportedProcessException;

    /**
     * Unregisters the {@link ApplicationPackage}.
     *
     * @param applicationPackage The {@link ApplicationPackage}.
     * @throws ProcessNotFoundException    If the {@link ApplicationPackage} is not registered in this repository. *
     * @throws UndeletableProcessException If the {@link ApplicationPackage} cannot be deleted.
     */
    default void unregister(ApplicationPackage applicationPackage)
            throws ProcessNotFoundException, UndeletableProcessException {
        unregister(applicationPackage.getProcessDescription().getProcessDescription().getId());
    }

    /**
     * Unregisters the {@link ApplicationPackage} with the specified {@code id}.
     *
     * @param id The identifier of the {@link ApplicationPackage}
     * @throws ProcessNotFoundException    If the {@code id} is not associated with an registered {@link
     *                                     ApplicationPackage}.
     * @throws UndeletableProcessException If the {@link ApplicationPackage} cannot be deleted.
     */
    void unregister(OwsCode id) throws ProcessNotFoundException, UndeletableProcessException;

}
