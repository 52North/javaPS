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
package org.n52.javaps.io.literal.xsd;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import org.n52.javaps.io.DecodingException;

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
    public Day parse(String value) throws DecodingException {
        try {
            return new Day(PARSER.parse(value).get(ChronoField.DAY_OF_MONTH));
        } catch (DateTimeException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<Day> getPayloadType() {
        return Day.class;
    }

    @Override
    public String generate(Day value) {
        return String.format("--%02d", value.getAsInt());
    }

}
