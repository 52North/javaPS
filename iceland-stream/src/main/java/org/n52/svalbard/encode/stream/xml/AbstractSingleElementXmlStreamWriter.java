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
package org.n52.svalbard.encode.stream.xml;

import java.util.Objects;

import javax.xml.stream.XMLStreamException;

import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractSingleElementXmlStreamWriter<S> extends AbstractMultiElementXmlStreamWriter {
    private final Class<? extends S> keyClass;

    public AbstractSingleElementXmlStreamWriter(Class<? extends S> keyClass) {
        super(keyClass);
        this.keyClass = Objects.requireNonNull(keyClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeElement(Object object)
            throws XMLStreamException, EncodingException {
        if (context() == null) {
            throw new IllegalStateException();
        }
        if (!keyClass.isAssignableFrom(object.getClass())) {
            throw unsupported(object);
        }

        write((S) object);
    }

    protected abstract void write(S object)
            throws XMLStreamException;
}
