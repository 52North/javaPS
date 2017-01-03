/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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


import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.n52.iceland.util.XmlFactories;
import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.decode.stream.MissingStreamReaderException;
import org.n52.svalbard.decode.stream.StreamReader;
import org.n52.svalbard.decode.stream.StreamReaderKey;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DocumentXmlStreamReader extends XmlFactories implements StreamReader<Object>, Component<StreamReaderKey> {

    private final ElementXmlStreamReaderRepository repository;

    @Inject
    public DocumentXmlStreamReader(ElementXmlStreamReaderRepository delegate) {
        this.repository = delegate;
    }

    private Object read(XMLEventReader reader)
            throws XMLStreamException {
        Object object = null;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartDocument()) {
                object = readDocumentElement(reader);
            } else if (event.isEndDocument()) {
                return object;
            }
        }
        throw eof();
    }

    private Object readDocumentElement(XMLEventReader reader)
            throws XMLStreamException {
        while (reader.hasNext()) {
            if (reader.peek().isStartElement()) {
                StartElement elem = reader.peek().asStartElement();
                XmlStreamReaderKey key = new XmlStreamReaderKey(elem.getName());
                return this.repository.get(key).orElseThrow(() -> new MissingStreamReaderException(key))
                        .readElement(reader);
            } else {
                reader.next();
            }
        }
        throw eof();
    }

    @Override
    public Set<StreamReaderKey> getKeys() {
        return repository.keys();
    }

    @Override
    public Object read(InputStream stream)
            throws OwsExceptionReport {
        try {
            return read(inputFactory().createXMLEventReader(stream));
        } catch (XMLStreamException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }
    }
}
