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
package org.n52.javaps.io.literal.xsd;

import java.time.DateTimeException;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralMonthType extends AbstractXSDLiteralType<Month> {

    private static final long serialVersionUID = -3702726667847500719L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("--MM");

    @Override
    public String getName() {
        return G_MONTH;
    }

    @Override
    public Month parse(OwsCode name, String value) throws OwsExceptionReport {
        try {
            return Month.from(FORMATTER.parse(value));
        } catch (DateTimeException ex) {
            throw new InvalidParameterValueException(name.getValue(), value).causedBy(ex);
        }
    }

    @Override
    public Class<Month> getPayloadType() {
        return Month.class;
    }

    @Override
    public String generate(OwsCode name, Month value) throws OwsExceptionReport {
        return FORMATTER.format(value);
    }

}
