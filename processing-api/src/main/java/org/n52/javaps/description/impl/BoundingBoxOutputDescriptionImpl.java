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
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.ogc.ows.OwsCRS;
import org.n52.javaps.description.BoundingBoxOutputDescription;
import org.n52.javaps.description.BoundingBoxOutputDescriptionBuilder;
import org.n52.javaps.description.ReturningProcessOutputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessOutputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements BoundingBoxOutputDescription {

    private final Set<OwsCRS> supportedCRS;
    private final OwsCRS defaultCRS;

    protected BoundingBoxOutputDescriptionImpl(
            AbstractBoundingBoxOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.supportedCRS = builder.getSupportedCRS();
        this.defaultCRS = Objects.requireNonNull(builder.getDefaultCRS());
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public OwsCRS getDefaultCRS() {
        return this.defaultCRS;
    }

    @Override
    public BoundingBoxOutputDescriptionImpl asBoundingBox() {
        return this;
    }

    @Override
    public boolean isBoundingBox() {
        return true;
    }

    @Override
    public <T> T visit(ReturningProcessOutputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningProcessOutputVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    public static BoundingBoxOutputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractBoundingBoxOutputDescriptionBuilder<BoundingBoxOutputDescriptionImpl, BuilderImpl> {

        @Override
        public BoundingBoxOutputDescriptionImpl build() {
            return new BoundingBoxOutputDescriptionImpl(this);
        }
    }

}
