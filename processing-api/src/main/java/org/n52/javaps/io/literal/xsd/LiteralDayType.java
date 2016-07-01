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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDayType extends AbstractXSDLiteralType<Day> {

    private static final long serialVersionUID = -8426376970434674190L;
    private static final DateTimeFormatter PARSER = DateTimeFormatter.ofPattern("---dd");

    @Override
    public String getName() {
        return G_DAY;
    }

    @Override
    public Day parse(OwsCode name, String value) throws OwsExceptionReport {
        try {
            return new Day(PARSER.parse(value).get(ChronoField.DAY_OF_MONTH));
        } catch (DateTimeException ex) {
            throw new InvalidParameterValueException(name.getValue(), value).causedBy(ex);
        }
    }

    @Override
    public Class<Day> getPayloadType() {
        return Day.class;
    }

    @Override
    public String generate(OwsCode name, Day value) throws OwsExceptionReport {
        return String.format("--%02d", value.getAsInt());
    }

}
