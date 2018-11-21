/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
import java.time.MonthDay;

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralMonthDayType extends AbstractXSDLiteralType<MonthDay> {

    private static final long serialVersionUID = -7094724779405693703L;

    @Override
    public String getName() {
        return G_MONTH_DAY;
    }

    @Override
    public MonthDay parse(String value) throws DecodingException {
        try {
            return MonthDay.parse(value);
        } catch (DateTimeException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<MonthDay> getPayloadType() {
        return MonthDay.class;
    }

    @Override
    public String generate(MonthDay value) {
        return value.toString();
    }

}
