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

import java.util.Objects;
import java.util.Set;

import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.LiteralOutputDescriptionBuilder;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public abstract class AbstractLiteralOutputDescriptionBuilder<T extends LiteralOutputDescription, B extends AbstractLiteralOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B> implements LiteralOutputDescriptionBuilder<T, B> {

   private LiteralDataDomain defaultLiteralDataDomain;
    private final ImmutableSet.Builder<LiteralDataDomain> supportedLiteralDataDomains = ImmutableSet.builder();


    LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return this.supportedLiteralDataDomains.build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public B withDefaultLiteralDataDomain(LiteralDataDomain literalDataDomain) {
        this.defaultLiteralDataDomain = Objects.requireNonNull(literalDataDomain);
        return (B) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public B withSupportedLiteralDataDomain(LiteralDataDomain domain) {
        if (domain != null) {
            this.supportedLiteralDataDomains.add(domain);
        }
        return (B) this;
    }

}
