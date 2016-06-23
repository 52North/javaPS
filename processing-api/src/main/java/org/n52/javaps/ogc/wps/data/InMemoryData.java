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
package org.n52.javaps.ogc.wps.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class InMemoryData extends ValueData {

    private byte[] data;

    public InMemoryData(OwsCode id) {
        this(id, null, null);
    }

    public InMemoryData(OwsCode id, byte[] data) {
        this(id, null, data);
    }

    public InMemoryData(OwsCode id, Format format, byte[] data) {
        super(id, format);
        this.data = data;
    }

    public void setData(byte[] data) {
        this.data = Objects.requireNonNull(data);
    }

    @Override
    public InputStream getData() {
        return new ByteArrayInputStream(data);
    }

}
