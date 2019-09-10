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
package org.n52.wp.javaps.transactional.rest;

import org.n52.javaps.engine.EngineException;
import org.n52.shetland.ogc.ows.OwsCode;

public class NotUndeployableProcessException extends EngineException {
    private static final long serialVersionUID = 6512402327929190325L;
    private final OwsCode id;

    public NotUndeployableProcessException(OwsCode id) {
        super(String.format("The process with the name %s can not be undeployed.", id));
        this.id = id;
    }

    public OwsCode getId() {
        return id;
    }
}
