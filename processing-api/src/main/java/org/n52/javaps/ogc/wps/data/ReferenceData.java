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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.utils.HTTP;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferenceData extends FormattedData {

    private URI uri;
    private Optional<Body> body;

    public ReferenceData(OwsCode id) {
        this(id, null, null, null);
    }

    public ReferenceData(OwsCode id, URI uri) {
        this(id, null, uri, null);
    }

    public ReferenceData(OwsCode id, Format format, URI uri) {
        this(id, format, uri, null);
    }

    public ReferenceData(OwsCode id, Format format, URI uri, Body body) {
        super(id, format);
        this.uri = Objects.requireNonNull(uri);
        this.body = Optional.ofNullable(body);
    }

    public URI getURI() {
        return uri;
    }

    public void setURI(URI uri) {
        this.uri = Objects.requireNonNull(uri);
    }

    public Optional<Body> getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = Optional.ofNullable(body);
    }

    public ValueData resolve()
            throws IOException {
        //TODO save as a file?
        final byte[] bytes;
        if (!getBody().isPresent()) {
            bytes = HTTP.get(getURI());
        } else {
            bytes = HTTP.post(getURI(), getBody().get().getBody()
                              .getBytes(StandardCharsets.UTF_8));
        }
        return new InMemoryData(getId(), getFormat(), bytes);
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public ReferenceData asReference() {
        return this;
    }
}