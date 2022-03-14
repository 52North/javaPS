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
package org.n52.javaps.engine.impl;

import com.google.common.io.ByteStreams;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.shetland.ogc.wps.data.impl.InMemoryValueProcessData;
import org.n52.shetland.util.HTTP;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ResolvableReferenceProcessData extends ReferenceProcessData {

    public ResolvableReferenceProcessData(OwsCode id) {
        this(id, null, null, null);
    }

    public ResolvableReferenceProcessData(OwsCode id, URI uri) {
        this(id, null, uri, null);
    }

    public ResolvableReferenceProcessData(OwsCode id, Format format, URI uri) {
        this(id, format, uri, null);
    }

    public ResolvableReferenceProcessData(OwsCode id, Format format, URI uri, Body body) {
        super(id, format, uri, body);
    }

    public ResolvableReferenceProcessData() {
        this(null, null, null, null);
    }

    public ResolvableReferenceProcessData(ReferenceProcessData data) {
        this(data.getId(), data.getFormat(), data.getURI(), data.getBody().orElse(null));
    }

    public ValueProcessData resolve() throws IOException {
        // TODO save as a file?
        final byte[] bytes;
        if (!getBody().isPresent()) {
            switch (getURI().getScheme()) {
                case "file":
                    bytes = getFromLocalDisc();
                    break;
                default:
                    bytes = HTTP.get(getURI());
            }
        } else {
            bytes = HTTP.post(getURI(), getBody().get().getBody().getBytes(StandardCharsets.UTF_8));
        }
        return new InMemoryValueProcessData(getId(), getFormat(), bytes);
    }

    private byte[] getFromLocalDisc() throws IOException {
        URL fileURL = getURI().toURL();
        try (InputStream is = new FileInputStream(fileURL.getFile())) {
            return ByteStreams.toByteArray(is);
        }
    }

}
