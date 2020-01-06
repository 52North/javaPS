/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io.literal.xsd;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDateType extends AbstractXSDLiteralType<LocalDate> {

    private static final long serialVersionUID = -7185987979855377555L;

    @Override
    public String getName() {
        return DATE;
    }

    @Override
    public LocalDate parse(String value) throws DecodingException {
        try {
            return LocalDate.from(DateTimeFormatter.ISO_DATE.parse(value));
        } catch (DateTimeException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<LocalDate> getPayloadType() {
        return LocalDate.class;
    }

    @Override
    public String generate(LocalDate value) {
        return value.toString();
    }

}
