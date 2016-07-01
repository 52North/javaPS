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
package org.n52.javaps.io.literal;

import java.io.Serializable;
import java.net.URI;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsDomainMetadata;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface LiteralType<T> extends Serializable {

    URI getURI();

    String getName();

    Class<T> getPayloadType();

    default OwsDomainMetadata getDataType() {
        return new OwsDomainMetadata(getURI(), getName());
    }

    default LiteralData<T> parseToBinding(OwsCode name, String value) throws OwsExceptionReport {
        return parseToBinding(name, value, null);
    }

    default LiteralData<T> parseToBinding(OwsCode name, String value, String uom) throws OwsExceptionReport {
        return new LiteralData<>(parse(name, value), uom);
    }

    T parse(OwsCode name, String value) throws OwsExceptionReport;

    String generate(OwsCode name, T value) throws OwsExceptionReport;

}
