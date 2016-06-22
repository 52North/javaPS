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

import java.util.Optional;

import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralDataDomainBuilder;
import org.n52.javaps.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.ogc.ows.OwsPossibleValues;


public class LiteralDataDomainImpl implements LiteralDataDomain {

    private final OwsPossibleValues valueDescription;
    private final Optional<OwsDomainMetadata> dataType;
    private final Optional<OwsDomainMetadata> uom;
    private final Optional<String> defaultValue;

    public LiteralDataDomainImpl(AbstractLiteralDataDomainBuilder<?, ?> builder) {
        this.valueDescription = builder.getValueDescription();
        this.dataType = Optional.ofNullable(builder.getDataType());
        this.uom = Optional.ofNullable(builder.getUom());
        this.defaultValue = Optional.ofNullable(builder.getDefaultValue());
    }

    @Override
    public OwsPossibleValues getValueDescription() {
        return this.valueDescription;
    }

    @Override
    public Optional<OwsDomainMetadata> getDataType() {
        return this.dataType;
    }

    @Override
    public Optional<OwsDomainMetadata> getUOM() {
        return this.uom;
    }

    @Override
    public Optional<String> getDefaultValue() {
        return this.defaultValue;
    }

    public static LiteralDataDomainBuilder<?,?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractLiteralDataDomainBuilder<LiteralDataDomain, BuilderImpl> {
        @Override
        public LiteralDataDomainImpl build() {
            return new LiteralDataDomainImpl(this);
        }
    }
}
