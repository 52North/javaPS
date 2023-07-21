/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import org.n52.javaps.io.DecodingException;

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
    public Month parse(String value) throws DecodingException {
        try {
            return Month.from(FORMATTER.parse(value));
        } catch (DateTimeException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<Month> getPayloadType() {
        return Month.class;
    }

    @Override
    public String generate(Month value) {
        return FORMATTER.format(value);
    }

}
