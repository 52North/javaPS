/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.handler;

import java.util.Objects;

import org.n52.iceland.request.handler.AbstractOperationHandler;
import org.n52.javaps.engine.Engine;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractEngineHandler extends AbstractOperationHandler {

    private final Engine engine;

    public AbstractEngineHandler(Engine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    protected Engine getEngine() {
        return engine;
    }

}
