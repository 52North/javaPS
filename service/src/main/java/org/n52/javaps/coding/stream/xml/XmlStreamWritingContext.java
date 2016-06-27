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
package org.n52.javaps.coding.stream.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.coding.stream.MissingStreamWriterException;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlStreamWritingContext implements AutoCloseable {

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newFactory();
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newFactory();

    static {
        OUTPUT_FACTORY.setProperty("escapeCharacters", false);
    }

    private final Deque<Map<String, String>> prefixes = new ArrayDeque<>();
    private final XMLEventWriter writer;
    private final Charset documentEncoding = StandardCharsets.UTF_8;
    private final BiFunction<XmlStreamWriterKey, XmlStreamWritingContext, Optional<ElementXmlStreamWriter>> writerProvider;
    private final OutputStream stream;

    public XmlStreamWritingContext(OutputStream stream, BiFunction<XmlStreamWriterKey, XmlStreamWritingContext, Optional<ElementXmlStreamWriter>> writerProvider)
            throws XMLStreamException {
        this.stream = Objects.requireNonNull(stream);
        this.writer = outputFactory().createXMLEventWriter(stream, this.documentEncoding.name());
        this.writerProvider = Objects.requireNonNull(writerProvider);
    }

    public <T> void write(T object)
            throws XMLStreamException {
        if (object != null) {
            XmlStreamWriterKey key = new XmlStreamWriterKey(object.getClass());
            ElementXmlStreamWriter delegate = this.writerProvider
                    .apply(key, this).orElseThrow(() -> new MissingStreamWriterException(key));
            delegate.writeElement(object);
        }
    }
    public <X,Y> Function<X,Y> identity(Function<X,Y> t){
        return t;
    }

    public boolean declareNamespace(String prefix, String namespace)
            throws XMLStreamException {
        if (this.prefixes.isEmpty()) {
            throw new IllegalStateException();
        }
        // check if the prefix is already declared
        String ns = this.prefixes.stream()
                .map(m -> m.get(prefix))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);

        if (ns != null) {
            // if it is declared for another namespace throw an exception
            if (!ns.equals(namespace)) {
                throw new XMLStreamException(String
                        .format("Prefix <%s> is already bound to <%s>", namespace, ns));
            }
            return false;
        }

        this.prefixes.peek().put(prefix, namespace);

        return true;
    }

    public void startDocument() throws XMLStreamException {
        dispatch(eventFactory().createStartDocument(getDocumentEncoding().name(), getDocumentVersion()));
    }

    public void endDocument() throws XMLStreamException {
        dispatch(eventFactory().createEndDocument());
    }

    /**
     * @return the event factory
     */
    public final XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the output factory
     */
    public final XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }

     /**
     * @return the input factory
     */
    public final XMLInputFactory inputFactory() {
        return INPUT_FACTORY;
    }
    private static final Logger LOG = LoggerFactory.getLogger(XmlStreamWritingContext.class);
    public void dispatch(XMLEvent event)
            throws XMLStreamException {
        if (event.isStartElement()) {
            this.prefixes.push(new HashMap<>(0));
        } else if (event.isEndElement()) {
            this.prefixes.pop();
        }
        this.writer.add(event);
    }

    public void write(Reader in) throws XMLStreamException {
        XMLEventReader reader = inputFactory().createXMLEventReader(in);
        try {
            write(reader);
        } finally {
            reader.close();
        }
    }

    public void write(XMLEventReader reader)
            throws XMLStreamException {
        EventFilter filter = (event) ->
                !event.isStartDocument() &&
                !event.isEndDocument() &&
                   !(event.isCharacters() && event.asCharacters().isIgnorableWhiteSpace());
        this.writer.add(inputFactory().createFilteredReader(reader, filter));
    }

    @Override
    public void close()
            throws IOException{
        try {
            this.writer.flush();
            this.writer.close();
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        } finally {
            this.stream.close();
        }
    }

    public void flush()
            throws XMLStreamException {
        this.writer.flush();
    }

    public Charset getDocumentEncoding() {
        return this.documentEncoding;
    }

    public String getDocumentVersion() {
        return XMLConstants.XML_VERSION;
    }
}
