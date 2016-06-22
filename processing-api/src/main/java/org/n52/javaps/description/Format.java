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

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Format implements Comparable<Format> {
    private static final Comparator<Format> COMPARATOR;
    private static final Set<String> CHARSETS;
    public static final String BASE64_ENCODING = "base64";
    public static final String DEFAULT_ENCODING = "UTF-8";


    static {
        Function<Format, String> mimeType = ((Function<Format, Optional<String>>) Format::getMimeType).andThen(o -> o.orElse(null));
        Function<Format, String> schema = ((Function<Format, Optional<String>>) Format::getSchema).andThen(o -> o.orElse(null));
        Function<Format, String> encoding = ((Function<Format, Optional<String>>) Format::getEncoding).andThen(o -> o.orElse(null));

        COMPARATOR = Comparator.nullsLast(Comparator.comparing(mimeType, Comparator.nullsFirst(Comparator.naturalOrder())))
                .thenComparing(Comparator.comparing(schema, Comparator.nullsFirst(Comparator.naturalOrder())))
                .thenComparing(Comparator.comparing(encoding, Comparator.nullsFirst(Comparator.naturalOrder())));
    }

    static {
        CHARSETS = ImmutableSet.copyOf(Charset.availableCharsets().keySet());
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

    public Format withEncoding(Charset encoding) {
        return withEncoding(encoding.name());
    }

    public Format withEncoding(String encoding) {
        return new Format(getMimeType().orElse(null),
                          encoding,
                          getSchema().orElse(null));
    }

    public Format withBase64Encoding() {
        return withEncoding(BASE64_ENCODING);
    }

    public Format withUTF8Encoding() {
        return withEncoding(DEFAULT_ENCODING);
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
        return comparator().compare(this, that);
    }


    public boolean isXML() {
        return getMimeType().map(String::toLowerCase).filter(x -> x.endsWith("xml")).isPresent();
    }

    public boolean isCharacterEncoding() {
        Set<String> charsets = getAvailableCharsets();
        return getEncoding().filter(charsets::contains).isPresent();
    }

    public boolean isBase64() {
        return getEncoding().filter(BASE64_ENCODING::equalsIgnoreCase).isPresent();
    }

    public static Set<String> getAvailableCharsets() {
        return Collections.unmodifiableSet(CHARSETS);
    }

    public static Comparator<Format> comparator() {
        return COMPARATOR;
    }
}
