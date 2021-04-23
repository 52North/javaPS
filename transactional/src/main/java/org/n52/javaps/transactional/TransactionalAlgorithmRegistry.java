/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;

/**
 * Registry to programmatically add and delete {@linkplain ApplicationPackage application packages}.
 *
 * @author Christian Autermann
 */
public interface TransactionalAlgorithmRegistry {
    /**
     * Updates the process specified in the {@link ApplicationPackage}.
     *
     * @param applicationPackage The description of the new process, a {@link ApplicationPackage}
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UnsupportedProcessException If the process is not supported.
     * @throws UndeletableProcessException If the process cannot be updated.
     */
    void update(ApplicationPackage applicationPackage)
            throws UndeletableProcessException, ProcessNotFoundException, UnsupportedProcessException;

    /**
     * Deletes the process with the specified id.
     *
     * @param id The identifier of the process.
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UndeletableProcessException If the process is not deletable.
     */
    void unregister(OwsCode id) throws ProcessNotFoundException, UndeletableProcessException;

    /**
     * Deletes the process with the specified id.
     *
     * @param id The identifier of the process.
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UndeletableProcessException If the process is not deletable.
     */
    default void unregister(String id) throws ProcessNotFoundException, UndeletableProcessException {
        unregister(new OwsCode(id));
    }

    /**
     * Deletes the process specified in the application package.
     *
     * @param applicationPackage The {@link ApplicationPackage} of the process.
     * @throws ProcessNotFoundException    If the process could not be found.
     * @throws UndeletableProcessException If the process is not deletable.
     */
    default void unregister(ApplicationPackage applicationPackage)
            throws ProcessNotFoundException, UndeletableProcessException {
        unregister(applicationPackage.getProcessDescription().getProcessDescription().getId());
    }

    /**
     * Adds a new process to this service.
     *
     * @param applicationPackage The description of the new process, a {@link ApplicationPackage}.
     * @return The id of the new process.
     * @throws DuplicateProcessException   If a process with the same identifier already exists.
     * @throws UnsupportedProcessException If the process is not supported.
     */
    OwsCode register(ApplicationPackage applicationPackage)
            throws DuplicateProcessException, UnsupportedProcessException;
}
