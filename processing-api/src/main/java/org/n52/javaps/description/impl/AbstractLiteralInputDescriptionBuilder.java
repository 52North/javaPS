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

import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralInputDescriptionBuilder;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public abstract class AbstractLiteralInputDescriptionBuilder<T extends LiteralInputDescription, B extends LiteralInputDescriptionBuilder<T, B>>
        extends AbstractProcessInputDescriptionBuilder<T, B>
        implements LiteralInputDescriptionBuilder<T, B> {

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