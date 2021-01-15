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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralHexBinaryType extends AbstractXSDLiteralType<byte[]> {

    private static final long serialVersionUID = 4543649633817027337L;

    @Override
    public String getName() {
        return HEX_BINARY;
    }

    @Override
    public byte[] parse(String value) throws DecodingException {
        try {
            return Hex.decodeHex(value.toCharArray());
        } catch (DecoderException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<byte[]> getPayloadType() {
        return byte[].class;
    }

    @Override
    public String generate(byte[] value) {
        return String.valueOf(Hex.encodeHex(value));
    }

}
