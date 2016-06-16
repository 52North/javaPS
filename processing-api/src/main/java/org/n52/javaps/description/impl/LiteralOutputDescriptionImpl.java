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

import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.LiteralOutputDescriptionBuilder;
import org.n52.javaps.description.ReturningProcessOutputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessOutputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements LiteralOutputDescription {

private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralOutputDescriptionImpl(
            AbstractLiteralOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.defaultLiteralDataDomain = Objects.requireNonNull(builder.getDefaultLiteralDataDomain());
        this.supportedLiteralDataDomains = builder.getSupportedLiteralDataDomains();
    }

    @Override
    public LiteralOutputDescriptionImpl asLiteral() {
        return this;
    }

    @Override
    public boolean isLiteral() {
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

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(this.supportedLiteralDataDomains);
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    public static LiteralOutputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractLiteralOutputDescriptionBuilder<LiteralOutputDescription, BuilderImpl> {
        @Override
        public LiteralOutputDescriptionImpl build() {
            return new LiteralOutputDescriptionImpl(this);
        }
    }

}
