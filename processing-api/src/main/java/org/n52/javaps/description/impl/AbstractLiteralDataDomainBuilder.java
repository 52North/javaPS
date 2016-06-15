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

import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralDataDomainBuilder;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 * @param <T>
 * @param <B>
 */
public abstract class AbstractLiteralDataDomainBuilder<T extends LiteralDataDomain, B extends AbstractLiteralDataDomainBuilder<T, B>>
        implements LiteralDataDomainBuilder<T, B> {

    private OwsAllowedValues allowedValues = OwsAllowedValues.any();
    private OwsDomainMetadata dataType;
    private OwsDomainMetadata uom;
    private String defaultValue;

    OwsAllowedValues getAllowedValues() {
        return allowedValues;
    }

    OwsDomainMetadata getDataType() {
        return dataType;
    }

    String getDefaultValue() {
        return defaultValue;
    }

    OwsDomainMetadata getUom() {
        return uom;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public B withDataType(OwsDomainMetadata dataType) {
        this.dataType = Objects.requireNonNull(dataType);
        return (B) this;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public B withDefaultValue(String value) {
        this.defaultValue = Strings.emptyToNull(value);
        return (B) this;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public B withUOM(OwsDomainMetadata uom) {
        this.uom = uom;
        return (B) this;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public B withAllowedValues(OwsAllowedValues allowedValues) {
        if (allowedValues == null) {
            this.allowedValues = OwsAllowedValues.any();
        } else {
            this.allowedValues = allowedValues;
        }
        return (B) this;
    }

}
