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

public class LiteralByteBinding extends AbstractLiteralDataBinding {
    private static final long serialVersionUID = 6673960070211065274L;
    private final Byte payload;

    public LiteralByteBinding(Byte payload) {
        this.payload = payload;
    }

    @Override
    public Byte getPayload() {
        return payload;
    }

    @Override
    public Class<Byte> getSupportedClass() {
        return Byte.class;
    }
}
