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
package org.n52.javaps.io.literal;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LiteralAnyURIBinding extends AbstractLiteralDataBinding {

    private static final long serialVersionUID = -1148340565647119514L;

    private transient URI uri;

    public LiteralAnyURIBinding(URI uri) {
        this.uri = uri;
    }

    public URI getURI() {
        return uri;
    }

    @Override
    public URI getPayload() {
        return uri;
    }

    @Override
    public Class<URI> getSupportedClass() {
        return URI.class;
    }

    private synchronized void writeObject(java.io.ObjectOutputStream oos) throws IOException {
        oos.writeObject(uri.toString());
    }

    private synchronized void readObject(java.io.ObjectInputStream oos) throws IOException, ClassNotFoundException {
        try {
            uri = new URI((String) oos.readObject());
        } catch (URISyntaxException ex) {
        }
    }

}
