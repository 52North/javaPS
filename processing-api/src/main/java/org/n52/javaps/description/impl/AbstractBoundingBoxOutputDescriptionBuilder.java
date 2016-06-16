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

import java.util.Set;

import org.n52.javaps.ogc.ows.OwsCRS;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescriptionBuilder;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractBoundingBoxOutputDescriptionBuilder<T extends BoundingBoxOutputDescription, B extends AbstractBoundingBoxOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B> implements BoundingBoxOutputDescriptionBuilder<T, B> {

    private OwsCRS defaultCRS;
    private final ImmutableSet.Builder<OwsCRS> supportedCRS = ImmutableSet.builder();

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withDefaultCRS(OwsCRS defaultCRS) {
        this.defaultCRS = defaultCRS;
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withSupportedCRS(OwsCRS uom) {
        if (uom != null) {
            this.supportedCRS.add(uom);
        }
        return (B) this;
    }

    OwsCRS getDefaultCRS() {
        return defaultCRS;
    }

    Set<OwsCRS> getSupportedCRS() {
        return supportedCRS.build();
    }

}
