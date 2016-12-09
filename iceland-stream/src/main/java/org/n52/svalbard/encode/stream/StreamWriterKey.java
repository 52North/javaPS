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
package org.n52.svalbard.encode.stream;

import java.util.Objects;

import org.n52.janmayen.component.ClassBasedComponentKey;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.similar.Similar;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamWriterKey extends ClassBasedComponentKey<Object> implements Similar<StreamWriterKey> {
    private final MediaType mediaType;

    public StreamWriterKey(Class<? extends Object> type, MediaType mediaType) {
        super(type);
        this.mediaType = mediaType;
    }

    @Override
    public int getSimilarity(StreamWriterKey that) {
        int mediaTypeSimilarity = getMediaType().getSimilarity(that.getMediaType());

        if (mediaTypeSimilarity < 0) {
            return mediaTypeSimilarity;
        }
        int typeSimilarity = getSimiliarity(
                this.getType() != null ? this.getType() : Object.class,
                that.getType() != null ? that.getType() : Object.class);

        if (typeSimilarity < 0) {
            return typeSimilarity;
        }

        return typeSimilarity + mediaTypeSimilarity;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        StreamWriterKey that = (StreamWriterKey) other;
        return Objects.equals(this.getMediaType(), that.getMediaType()) &&
               Objects.equals(this.getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMediaType(), getType());
    }

    public static int getSimiliarity(Class<?> superClass, Class<?> clazz) {
        if (clazz.isArray()) {
            if (!superClass.isArray()) {
                return -1;
            } else {
                return getSimiliarity(superClass.getComponentType(), clazz.getComponentType());
            }
        }
        if (superClass == clazz) {
            return 0;
        } else {

            int difference = -1;
            if (clazz.getSuperclass() != null) {
                difference = getSimiliarity1(superClass, clazz.getSuperclass(), -1);
            }
            if (difference != 0 && superClass.isInterface()) {
                for (Class<?> i : clazz.getInterfaces()) {
                    difference = getSimiliarity1(superClass, i, difference);
                    if (difference == 0) {
                        break;
                    }
                }
            }
            return difference < 0 ? -1 : 1 + difference;
        }
    }
    
    private static int getSimiliarity1(Class<?> superClass, Class<?> clazz, int difference) {
        if (superClass.isAssignableFrom(clazz)) {
            int cd = getSimiliarity(superClass, clazz);
            return (cd >= 0) ? ((difference < 0) ? cd : Math.min(difference, cd)) : difference;
        } else {
            return difference;
        }
    }

}
