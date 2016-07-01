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

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsCode;

import com.google.common.io.BaseEncoding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralBase64BinaryType extends AbstractXSDLiteralType<byte[]> {

    private static final long serialVersionUID = -7151307323380819521L;

    @Override
    public String getName() {
        return BASE64_BINARY;
    }

    @Override
    public byte[] parse(OwsCode name, String value) throws OwsExceptionReport {
        try {
            return BaseEncoding.base64().decode(value);
        } catch (IllegalArgumentException ex) {
            throw new InvalidParameterValueException(name.getValue(), value).causedBy(ex);
        }
    }

    @Override
    public Class<byte[]> getPayloadType() {
        return byte[].class;
    }

    @Override
    public String generate(OwsCode name, byte[] value) throws OwsExceptionReport {
        return BaseEncoding.base64().encode(value);
    }

}
