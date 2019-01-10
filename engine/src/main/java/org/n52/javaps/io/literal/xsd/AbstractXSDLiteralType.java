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
package org.n52.javaps.io.literal.xsd;

import java.net.URI;
import java.util.Objects;

import org.n52.javaps.io.literal.LiteralType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractXSDLiteralType<T> implements LiteralType<T> {

    protected static final String ANY_SIMPLE_TYPE = "anySimpleType";

    protected static final String DURATION = "duration";

    protected static final String DATE_TIME = "dateTime";

    protected static final String TIME = "time";

    protected static final String DATE = "date";

    protected static final String G_YEAR_MONTH = "gYearMonth";

    protected static final String G_YEAR = "gYear";

    protected static final String G_MONTH_DAY = "gMonthDay";

    protected static final String G_DAY = "gDay";

    protected static final String G_MONTH = "gMonth";

    protected static final String BOOLEAN = "boolean";

    protected static final String BASE64_BINARY = "base64Binary";

    protected static final String HEX_BINARY = "hexBinary";

    protected static final String FLOAT = "float";

    protected static final String DOUBLE = "double";

    protected static final String ANY_URI = "anyURI";

    protected static final String STRING = "string";

    protected static final String NORMALIZED_STRING = "normalizedString";

    protected static final String LANGUAGE = "language";

    protected static final String DECIMAL = "decimal";

    protected static final String INTEGER = "integer";

    protected static final String NON_POSITIVE_INTEGER = "nonPositiveInteger";

    protected static final String LONG = "long";

    protected static final String NON_NEGATIVE_INTEGER = "nonNegativeInteger";

    protected static final String NEGATIVE_INTEGER = "negativeInteger";

    protected static final String INT = "int";

    protected static final String SHORT = "short";

    protected static final String BYTE = "byte";

    protected static final String UNSIGNED_LONG = "unsignedLong";

    protected static final String POSITIVE_INTEGER = "positiveInteger";

    protected static final String UNSIGNED_INT = "unsignedInt";

    protected static final String UNSIGNED_SHORT = "unsignedShort";

    protected static final String UNSIGNED_BYTE = "unsignedByte";

    protected static final URI BASE_URI = URI.create("https://www.w3.org/2001/XMLSchema-datatypes");

    protected static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

    protected static final String XML_SCHEMA_PREFIX = "xs";

    private static final long serialVersionUID = -7998164049879893066L;

    @Override
    public URI getURI() {
        return BASE_URI.resolve('#' + getName());
    }

    @Override
    public String toString() {
        return XML_SCHEMA_PREFIX + ":" + getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        LiteralType<?> other = (LiteralType<?>) obj;

        return Objects.equals(getURI(), other.getURI()) && Objects.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getURI(), getName());
    }

}
