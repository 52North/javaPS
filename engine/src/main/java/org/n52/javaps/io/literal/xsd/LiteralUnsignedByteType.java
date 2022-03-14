/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
public class LiteralUnsignedByteType extends AbstractLiteralIntegerType<Short> {

    private static final long serialVersionUID = 5694423498295836742L;

    @Override
    public String getName() {
        return UNSIGNED_BYTE;
    }

    @Override
    public Short parse(String value) throws DecodingException {
        return asBigInteger(value).shortValue();
    }

    @Override
    protected BigInteger min() {
        return BigInteger.valueOf(255);
    }

    @Override
    protected BigInteger max() {
        return BigInteger.ZERO;
    }

    @Override
    public Class<Short> getPayloadType() {
        return Short.class;
    }

    @Override
    public String generate(Short value) {
        return value.toString();
    }

}
