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

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDoubleType extends AbstractXSDLiteralType<Double> {

    private static final long serialVersionUID = -3743100438134923945L;

    @Override
    public String getName() {
        return DOUBLE;
    }

    @Override
    public Double parse(String value) throws DecodingException {
        try {
            return Double.parseDouble(value);
        } catch (IllegalArgumentException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<Double> getPayloadType() {
        return Double.class;
    }

    @Override
    public String generate(Double value) {
        return value.toString();
    }

}
