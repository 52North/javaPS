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
package org.n52.javaps.description.impl;

import java.util.Objects;
import java.util.Set;

import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.ComplexOutputDescriptionBuilder;
import org.n52.javaps.description.Format;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractComplexOutputDescriptionBuilder<T extends ComplexOutputDescription, B extends AbstractComplexOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B> implements ComplexOutputDescriptionBuilder<T, B> {

    private final ImmutableSet.Builder<Format> supportedFormats = ImmutableSet
            .builder();
    private Format defaultFormat;

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withDefaultFormat(Format format) {
        this.defaultFormat = Objects.requireNonNull(format);
        this.supportedFormats.add(format);
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withSupportedFormat(Format format) {
        if (format != null) {
            this.supportedFormats.add(format);
        }
        return (B) this;
    }

    Set<Format> getSupportedFormats() {
        return supportedFormats.build();
    }

    Format getDefaultFormat() {
        return defaultFormat;
    }

}
