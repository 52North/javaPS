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
package org.n52.javaps.description;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Format implements Comparable<Format> {
    private static final Comparator<Format> COMPARATOR;



    static {
        Function<Optional<String>, String> elseNull = o -> o.orElse(null);

        Function<Format, String> mimeType = ((Function<Format, Optional<String>>) Format::getMimeType).andThen(elseNull);
        Function<Format, String> schema = ((Function<Format, Optional<String>>) Format::getSchema).andThen(elseNull);
        Function<Format, String> encoding = ((Function<Format, Optional<String>>) Format::getEncoding).andThen(elseNull);


        COMPARATOR = Comparator.nullsLast(
                Comparator.comparing(mimeType, Comparator.nullsFirst(Comparator.naturalOrder())))
                .thenComparing(Comparator.comparing(schema, Comparator.nullsFirst(Comparator.naturalOrder())))
                .thenComparing(Comparator.comparing(encoding, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    private final Optional<String> mimeType;
    private final Optional<String> encoding;
    private final Optional<String> schema;

    public Format(String mimeType) {
        this(mimeType, null, null);
    }

    public Format(String mimeType, String encoding) {
        this(mimeType, encoding, null);
    }

    public Format(String mimeType, String encoding, String schema) {
        this.mimeType = Optional.ofNullable(emptyToNull(mimeType));
        this.encoding = Optional.ofNullable(emptyToNull(encoding));
        this.schema = Optional.ofNullable(emptyToNull(schema));
    }

    public Format() {
        this(null, null, null);
    }

    public Optional<String> getMimeType() {
        return mimeType;
    }

    public Optional<String> getEncoding() {
        return encoding;
    }

    public Optional<String> getSchema() {
        return schema;
    }

    public boolean isEmpty() {
        return !hasMimeType() && !hasEncoding() && !hasSchema();
    }

    public boolean hasSchema() {
        return getSchema().isPresent();
    }

    public boolean hasEncoding() {
        return getEncoding().isPresent();
    }

    public boolean hasMimeType() {
        return getMimeType().isPresent();
    }

    public boolean hasMimeType(String mimeType) {
        return getMimeType().orElse("").equalsIgnoreCase(nullToEmpty(mimeType));
    }

    public boolean hasEncoding(String encoding) {
        return getEncoding().orElse("").equalsIgnoreCase(nullToEmpty(encoding));
    }

    public boolean hasSchema(String schema) {
        return getSchema().orElse("").equalsIgnoreCase(nullToEmpty(schema));
    }

    public boolean hasMimeType(Format other) {
        return hasMimeType(other.getMimeType().orElse(null));
    }

    public boolean hasEncoding(Format other) {
        return hasEncoding(other.getEncoding().orElse(null));
    }

    public boolean hasSchema(Format other) {
        return hasSchema(other.getSchema().orElse(null));
    }

    public boolean matchesMimeType(String mimeType) {
        return !hasMimeType() || hasMimeType(mimeType);
    }

    public boolean matchesEncoding(String encoding) {
        return !hasEncoding() || hasEncoding(encoding);
    }

    public boolean matchesSchema(String schema) {
        return !hasSchema() || hasSchema(schema);
    }

    public boolean matchesMimeType(Format other) {
        return !hasMimeType() || hasMimeType(other);
    }

    public boolean matchesEncoding(Format other) {
        return !hasEncoding() || hasEncoding(other);
    }

    public boolean matchesSchema(Format other) {
        return !hasSchema() || hasSchema(other);
    }

    public Format withEncoding(String encoding) {
        return new Format(getMimeType().orElse(null),
                          encoding,
                          getSchema().orElse(null));
    }

    public Format withBase64Encoding() {
        return withEncoding("Base64");
    }

    public Format withUTF8Encoding() {
        return withEncoding("UTF-8");
    }

    public Format withSchema(String schema) {
        return new Format(getMimeType().orElse(null),
                          getEncoding().orElse(null),
                          schema);
    }

    public Format withMimeType(String mimeType) {
        return new Format(mimeType,
                          getEncoding().orElse(null),
                          getSchema().orElse(null));
    }

    public Format withoutMimeType() {
        return new Format(null,
                          getEncoding().orElse(null),
                          getSchema().orElse(null));
    }

    public Format withoutEncoding() {
        return new Format(getMimeType().orElse(null),
                          null,
                          getSchema().orElse(null));
    }

    public Format withoutSchema() {
        return new Format(getMimeType().orElse(null),
                          getEncoding().orElse(null),
                          null);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("mimeType", this.mimeType.orElse(null))
                .add("encoding", this.encoding.orElse(null))
                .add("schema", this.schema.orElse(null)).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mimeType, this.encoding, this.schema);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Format) {
            final Format that = (Format) obj;
            return Objects.equals(this.mimeType, that.getMimeType()) &&
                   Objects.equals(this.encoding, that.getEncoding()) &&
                   Objects.equals(this.schema, that.getSchema());
        }
        return false;
    }

    public Predicate<Format> matchingEncoding() {
        return this::hasEncoding;
    }

    public Predicate<Format> matchingSchema() {
        return this::hasSchema;
    }

    public Predicate<Format> matchingMimeType() {
        return this::hasMimeType;
    }

    public boolean isCompatible(Format other) {
        return (!this.hasEncoding() || this.hasEncoding(other)) &&
               (!this.hasSchema() || this.hasSchema(other)) &&
               (!this.hasMimeType() || this.hasMimeType(other));
    }

    public void setTo(Consumer<String> encoding,
                      Consumer<String> mimeType,
                      Consumer<String> schema) {
        getEncoding().ifPresent(encoding);
        getMimeType().ifPresent(mimeType);
        getSchema().ifPresent(schema);
    }

    @Override
    public int compareTo(Format that) {
        return COMPARATOR.compare(this, that);
    }
}
