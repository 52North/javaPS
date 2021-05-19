/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io.literal;

import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralDescription;

import java.net.URI;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface LiteralTypeRepository {

    default <T> LiteralType<T> getLiteralType(Class<? extends LiteralType<?>> literalType) {
        return getLiteralType(literalType, null);
    }

    <T> LiteralType<T> getLiteralType(Class<? extends LiteralType<?>> literalType, Class<?> payloadType);

    Optional<LiteralType<?>> getLiteralType(String name);

    Optional<LiteralType<?>> getLiteralType(URI uri);

    default Optional<LiteralType<?>> getLiteralType(OwsDomainMetadata dataType) {
        if (dataType == null) {
            return Optional.empty();
        }
        Optional<LiteralType<?>> literalType = dataType.getReference().flatMap(this::getLiteralType);
        if (literalType.isPresent()) {
            return literalType;
        }
        return dataType.getValue().flatMap(this::getLiteralType);
    }

    default Optional<LiteralType<?>> getLiteralType(LiteralDataDomain literalDataDomain) {
        return Optional.ofNullable(literalDataDomain)
                       .flatMap(LiteralDataDomain::getDataType)
                       .flatMap(this::getLiteralType);
    }

    default Optional<LiteralType<?>> getLiteralType(LiteralDescription literalDescription) {
        return Optional.ofNullable(literalDescription)
                       .map(LiteralDescription::getDefaultLiteralDataDomain)
                       .flatMap(this::getLiteralType);
    }
}
