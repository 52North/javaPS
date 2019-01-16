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
package org.n52.javaps.io;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.n52.shetland.ogc.wps.Format;

public class OutputHandlerRepositoryImpl extends AbstractInputOutputHandlerRepository<OutputHandler> implements
        OutputHandlerRepository {

    private Set<Provider<OutputHandler>> handlers;

    @Override
    protected Set<Provider<OutputHandler>> getHandlerProviders() {
        return Collections.unmodifiableSet(this.handlers);
    }

    @Override
    public Optional<OutputHandler> getOutputHandler(Format format,
            Class<? extends Data<?>> binding) {
        return getHandler(binding, format);
    }

    @Override
    public Set<OutputHandler> getOutputHandlers() {
        return getHandlers();
    }

    @Inject
    public void setHandlers(Collection<Provider<OutputHandler>> handlers) {
        this.handlers = new HashSet<>(handlers);
    }

}
