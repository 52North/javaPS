/*
 * Copyright (C) 2013-2015 Christian Autermann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.n52.javaps.description.impl;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.LiteralOutputDescriptionBuilder;

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
    public <T> T visit(ReturningVisitor<T> visitor) {
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
