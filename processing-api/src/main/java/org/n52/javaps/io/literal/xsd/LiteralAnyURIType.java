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
package org.n52.javaps.io.literal.xsd;

import java.net.URI;
import java.net.URISyntaxException;

import org.n52.javaps.io.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralAnyURIType extends AbstractXSDLiteralType<URI> {

    private static final long serialVersionUID = -612697473414894518L;

    @Override
    public String getName() {
        return ANY_URI;
    }

    @Override
    public URI parse(String value) throws DecodingException {
        try {
            return new URI(value);
        } catch (URISyntaxException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Class<URI> getPayloadType() {
        return URI.class;
    }

    @Override
    public String generate(URI value) {
        return value.toString();
    }

}
