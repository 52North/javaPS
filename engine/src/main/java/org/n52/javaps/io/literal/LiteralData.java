/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import java.util.Objects;
import java.util.Optional;

import org.n52.javaps.io.Data;

public final class LiteralData implements Data<Object> {
    private static final long serialVersionUID = -7088293056427203440L;
    private Optional<String> unitOfMeasurement;
    private final Object payload;

    public LiteralData(Object payload) {
        this(payload, null);
    }

    public LiteralData(Object payload, String unitOfMeasurment) {
        this.payload = Objects.requireNonNull(payload);
        this.unitOfMeasurement = Optional.ofNullable(unitOfMeasurment);
    }

    public Optional<String> getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = Optional.ofNullable(unitOfMeasurement);
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Override
    public Class<?> getSupportedClass() {
        return Object.class;
    }
}
