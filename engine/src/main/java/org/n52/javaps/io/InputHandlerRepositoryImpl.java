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
package org.n52.javaps.io;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.n52.shetland.ogc.wps.Format;

/**
 * XMLParserFactory. Will be initialized within each Framework.
 *
 * @author foerster
 *
 */
public class InputHandlerRepositoryImpl extends AbstractInputOutputHandlerRepository<InputHandler> implements
        InputHandlerRepository {

    private Set<Provider<InputHandler>> handlers;

    @Override
    public Optional<InputHandler> getInputHandler(Format format,
            Class<? extends Data<?>> binding) {
        return getHandler(binding, format);
    }

    @Override
    public Set<InputHandler> getInputHandlers() {
        return getHandlers();
    }

    @Inject
    public void setHandlers(Collection<Provider<InputHandler>> handlers) {
        this.handlers = new HashSet<>(handlers);
    }

    @Override
    protected Set<Provider<InputHandler>> getHandlerProviders() {
        return Collections.unmodifiableSet(this.handlers);
    }
}
