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
package org.n52.javaps.io.complex;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64InputStream;

import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.EncodingException;

/**
 * Basic interface for all Generators.
 *
 * @author Matthias Mueller, TU Dresden
 *
 */
public interface IGenerator extends IOHandler {
    InputStream encode(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException;

    default InputStream encodeBase64(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException {
        return new Base64InputStream(encode(description, data, format), true);
    }
}
