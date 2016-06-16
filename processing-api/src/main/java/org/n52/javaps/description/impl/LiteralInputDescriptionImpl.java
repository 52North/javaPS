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
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralInputDescriptionBuilder;
import org.n52.javaps.description.ReturningProcessInputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessInputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralInputDescriptionImpl
        extends AbstractProcessInputDescription
        implements LiteralInputDescription {

    private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralInputDescriptionImpl(
            AbstractLiteralInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.defaultLiteralDataDomain = Objects.requireNonNull(builder.getDefaultLiteralDataDomain());
        this.supportedLiteralDataDomains = builder.getSupportedLiteralDataDomains();
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public LiteralInputDescriptionImpl asLiteral() {
        return this;
    }

    @Override
    public <T> T visit(ReturningProcessInputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningProcessInputVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(supportedLiteralDataDomains);
    }

    public static LiteralInputDescriptionBuilder<?, ?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractLiteralInputDescriptionBuilder<LiteralInputDescriptionImpl, BuilderImpl> {
        @Override
        public LiteralInputDescriptionImpl build() {
            return new LiteralInputDescriptionImpl(this);
        }
    }

}
