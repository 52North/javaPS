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
package org.n52.javaps.io.literal;

public class LiteralBase64BinaryBinding extends AbstractLiteralDataBinding {
    private static final long serialVersionUID = -9025105142295309281L;
    private final byte[] binary;

    public LiteralBase64BinaryBinding(byte[] binary) {
        this.binary = binary;
    }

    public byte[] getBinary() {
        return binary;
    }

    @Override
    public byte[] getPayload() {
        return binary;
    }

    @Override
    public Class<byte[]> getSupportedClass() {
        return byte[].class;
    }
}