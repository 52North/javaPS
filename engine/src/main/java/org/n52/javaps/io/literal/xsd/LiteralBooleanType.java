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
public class LiteralBooleanType extends AbstractXSDLiteralType<Boolean> {

    private static final long serialVersionUID = -3725729099095551317L;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    public String getName() {
        return BOOLEAN;
    }

    @Override
    public Boolean parse(String value) throws DecodingException {
        switch (value) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            default:
                throw new DecodingException("value is not a valid boolean");
        }
    }

    @Override
    public Class<Boolean> getPayloadType() {
        return Boolean.class;
    }

    @Override
    public String generate(Boolean value) {
        return value ? TRUE : FALSE;
    }

}
