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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.wps.Format;

import com.google.common.base.MoreObjects;

public class ByteArrayValueProcessData extends ValueProcessData {

    private final byte[] bytes;

    public ByteArrayValueProcessData(OwsCode id, byte[] bytes) {
        this(id, null, bytes);
    }

    public ByteArrayValueProcessData(OwsCode id, Format format, byte[] bytes) {
        super(id, format);
        this.bytes = Objects.requireNonNull(bytes);
    }

    public ByteArrayValueProcessData(byte[] bytes) {
        this(null, null, bytes);
    }

    @Override
    public InputStream getData() {
        return new ByteArrayInputStream(this.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFormat(), this.bytes);
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
        final ByteArrayValueProcessData other = (ByteArrayValueProcessData) obj;
        return Objects.equals(getId(), other.getId()) &&
               Objects.equals(getFormat(), other.getFormat()) &&
               Arrays.equals(this.bytes, other.bytes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", getId())
                .add("format", getFormat())
                .add("value", this.bytes)
                .toString();
    }

}
