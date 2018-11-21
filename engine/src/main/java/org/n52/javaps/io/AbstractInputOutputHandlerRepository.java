/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Provider;

import org.n52.shetland.ogc.wps.Format;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 *            the input output handler type
 */
public abstract class AbstractInputOutputHandlerRepository<T extends InputOutputHandler> implements FormatRepository {

    @Override
    public Set<Format> getSupportedFormats() {
        return stream().map(T::getSupportedFormats).flatMap(Set::stream).collect(toSet());
    }

    @Override
    public Set<Format> getSupportedFormats(Class<? extends Data<?>> binding) {
        return stream().filter(g -> g.isSupportedBinding(binding)).map(InputOutputHandler::getSupportedFormats).flatMap(
                Set::stream).collect(toSet());
    }

    protected Optional<T> getHandler(Class<? extends Data<?>> binding,
            Format format) {
        // TODO: try a chaining approach, by calculation all permutations and
        // look for matches.
        return stream().filter(g -> g.isSupportedBinding(binding)).filter(g -> g.isSupportedFormat(format)).findFirst();
    }

    protected Set<T> getHandlers() {
        return Collections.unmodifiableSet(stream().collect(toSet()));
    }

    protected abstract Set<Provider<T>> getHandlerProviders();

    protected Stream<T> stream() {
        return getHandlerProviders().stream().map(Provider::get);
    }
}
