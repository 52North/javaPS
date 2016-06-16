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

public class LiteralStringBinding extends AbstractLiteralDataBinding {
    private static final long serialVersionUID = 4918615178134884183L;
    private final String payload;

    public LiteralStringBinding(String payload) {
        this.payload = payload;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Class<String> getSupportedClass() {
        return String.class;
    }
}
