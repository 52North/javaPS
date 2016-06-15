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

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCRS;
import org.n52.javaps.description.BoundingBoxInputDescription;
import org.n52.javaps.description.BoundingBoxInputDescriptionBuilder;
import org.n52.javaps.description.ReturningProcessInputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxInputDescriptionImpl extends AbstractProcessInputDescription
        implements BoundingBoxInputDescription {

    private final Set<OwsCRS> supportedCRS;
    private final Optional<OwsCRS> defaultCRS;

    protected BoundingBoxInputDescriptionImpl(
            AbstractBoundingBoxInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.supportedCRS = builder.getSupportedCRS();
        this.defaultCRS = Optional.ofNullable(builder.getDefaultCRS());
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public Optional<OwsCRS> getDefaultCRS() {
        return this.defaultCRS;
    }

    @Override
    public boolean isBoundingBox() {
        return true;
    }

    @Override
    public BoundingBoxInputDescriptionImpl asBoundingBox() {
        return this;
    }

    @Override
    public <T> T visit(ReturningProcessInputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static BoundingBoxInputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractBoundingBoxInputDescriptionBuilder<BoundingBoxInputDescriptionImpl, BuilderImpl> {
        @Override
        public BoundingBoxInputDescriptionImpl build() {
            return new BoundingBoxInputDescriptionImpl(this);
        }
    }

}
