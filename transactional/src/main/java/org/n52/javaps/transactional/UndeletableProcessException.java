/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.ows.OwsCode;

/**
 * {@link TransactionalEngineException} that can be thrown if a process cannot be deleted.
 */
public class UndeletableProcessException extends TransactionalEngineException {
    private static final long serialVersionUID = 6512402327929190325L;
    private final OwsCode id;

    /**
     * Creates a new {@link UndeletableProcessException}.
     *
     * @param id The identifier of the process that cannot be unregistered
     */
    public UndeletableProcessException(OwsCode id) {
        super(String.format("The process with the name %s can not be deleted.", id));
        this.id = id;
    }

    /**
     * Get the identifier of the process that cannot be unregistered.
     *
     * @return The identifier
     */
    public OwsCode getId() {
        return id;
    }
}
