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

import java.math.BigInteger;

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralByteType extends AbstractLiteralIntegerType<Byte> {

    private static final long serialVersionUID = 8338465395257876544L;

    @Override
    public String getName() {
        return BYTE;
    }

    @Override
    protected BigInteger min() {
        return BigInteger.valueOf(Byte.MIN_VALUE);
    }

    @Override
    protected BigInteger max() {
        return BigInteger.valueOf(Byte.MAX_VALUE);
    }

    @Override
    public Byte parse(String value) throws DecodingException {
        return asBigInteger(value).byteValue();
    }

    @Override
    public Class<Byte> getPayloadType() {
        return Byte.class;
    }

    @Override
    public String generate(Byte value) {
        return value.toString();
    }

}
