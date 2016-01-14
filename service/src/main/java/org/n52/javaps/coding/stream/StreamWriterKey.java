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
package org.n52.javaps.coding.stream;

import org.n52.iceland.component.ClassBasedComponentKey;
import org.n52.iceland.util.ClassHelper;
import org.n52.iceland.util.Similar;
import org.n52.iceland.util.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamWriterKey extends ClassBasedComponentKey<Object>
        implements Similar<StreamWriterKey> {
    private final MediaType mediaType;

    public StreamWriterKey(Class<? extends Object> type, MediaType mediaType) {
        super(type);
        this.mediaType = mediaType;
    }

    @Override
    public int getSimilarity(StreamWriterKey that) {
        if (!this.mediaType.isCompatible(that.getMediaType())) {
            return -1;
        }
        return ClassHelper.getSimiliarity(
                    this.getType() != null ? this.getType() : Object.class,
                    that.getType() != null ? that.getType() : Object.class);
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

}
