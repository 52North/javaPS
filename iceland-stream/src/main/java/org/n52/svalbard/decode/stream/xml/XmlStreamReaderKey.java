/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.stream.xml;

import java.util.Objects;

import javax.xml.namespace.QName;

import org.n52.janmayen.http.MediaTypes;
import org.n52.svalbard.decode.stream.StreamReaderKey;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlStreamReaderKey extends StreamReaderKey {

    private final QName name;

    public XmlStreamReaderKey(QName name) {
        super(MediaTypes.APPLICATION_XML);
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            return Objects.equals(getName(), ((XmlStreamReaderKey) obj).getName());
        }
    }

    public QName getName() {
        return name;
    }

}
