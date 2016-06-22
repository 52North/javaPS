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
package org.n52.javaps.description;


import java.net.URI;

import org.n52.javaps.ogc.ows.OwsDomainMetadata;

import com.google.common.base.Strings;

import org.n52.javaps.ogc.ows.OwsPossibleValues;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface LiteralDataDomainBuilder<T extends LiteralDataDomain, B extends LiteralDataDomainBuilder<T, B>> extends Builder<T, B> {
    B withDefaultValue(String value);

    B withDataType(OwsDomainMetadata dataType);

    default B withDataType(URI reference, String value) {
        return withDataType(Strings.emptyToNull(value) == null ? null : new OwsDomainMetadata(reference, value));
    }

    default B withDataType(String value) {
        return withDataType(Strings.emptyToNull(value) == null ? null : new OwsDomainMetadata(value));
    }

    B withUOM(OwsDomainMetadata uom);

    default B withUOM(URI reference, String value) {
        return withUOM(Strings.emptyToNull(value) == null ? null : new OwsDomainMetadata(reference, value));
    }

    default B withUOM(String value) {
        return withUOM(Strings.emptyToNull(value) == null ? null : new OwsDomainMetadata(value));
    }

    B withValueDescription(OwsPossibleValues allowedValues);

}
