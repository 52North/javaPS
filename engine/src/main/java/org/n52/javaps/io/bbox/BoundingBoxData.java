/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io.bbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;

public final class BoundingBoxData implements Data<OwsBoundingBox> {
    private static final long serialVersionUID = -3219612972795621553L;

    private transient OwsBoundingBox boundingBox;

    public BoundingBoxData(OwsBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public OwsBoundingBox getPayload() {
        return this.boundingBox;
    }

    @Override
    public Class<?> getSupportedClass() {
        return OwsBoundingBox.class;
    }

    private synchronized void writeObject(java.io.ObjectOutputStream oos) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BoundingBoxInputOutputHandler generator = new BoundingBoxInputOutputHandler();
        InputStream in = generator.generate(null, this, new Format());
        IOUtils.copy(in, out);
    }

    private void readObject(java.io.ObjectInputStream oos) throws IOException, ClassNotFoundException,
            DecodingException {
        BoundingBoxInputOutputHandler parser  = new BoundingBoxInputOutputHandler();
        BoundingBoxData data = (BoundingBoxData) parser.parse(null, oos, new Format());
        this.boundingBox = data.boundingBox;
    }
}
