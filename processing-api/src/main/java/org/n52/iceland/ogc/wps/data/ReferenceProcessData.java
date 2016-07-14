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
package org.n52.iceland.ogc.wps.data;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.Format;
import org.n52.javaps.utils.HTTP;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferenceProcessData extends FormattedProcessData {

    private URI uri;
    private Optional<Body> body;

    public ReferenceProcessData(OwsCode id) {
        this(id, null, null, null);
    }

    public ReferenceProcessData(OwsCode id, URI uri) {
        this(id, null, uri, null);
    }

    public ReferenceProcessData(OwsCode id, Format format, URI uri) {
        this(id, format, uri, null);
    }

    public ReferenceProcessData(OwsCode id, Format format, URI uri, Body body) {
        super(id, format);
        this.uri = uri;
        this.body = Optional.ofNullable(body);
    }

    public ReferenceProcessData() {
        this(null, null, null, null);
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

    public ValueProcessData resolve()
            throws IOException {
        //TODO save as a file?
        final byte[] bytes;
        if (!getBody().isPresent()) {
            bytes = HTTP.get(getURI());
        } else {
            bytes = HTTP.post(getURI(), getBody().get().getBody()
                              .getBytes(StandardCharsets.UTF_8));
        }
        return new InMemoryProcessData(getId(), getFormat(), bytes);
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public ReferenceProcessData asReference() {
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", getId())
                .add("format", getFormat())
                .add("uri", getURI())
                .add("body", getBody().orElse(null))
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFormat(), getURI(), getBody());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReferenceProcessData other = (ReferenceProcessData) obj;
        return Objects.equals(getId(), other.getId()) &&
               Objects.equals(getFormat(), other.getFormat()) &&
               Objects.equals(getURI(), other.getURI()) &&
               Objects.equals(getBody(), other.getBody());
    }
}