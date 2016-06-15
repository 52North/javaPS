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

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.gml.time.TimeInstant;
import org.n52.iceland.ogc.gml.time.TimePosition;
import org.n52.iceland.util.DateTimeHelper;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.w3c.SchemaLocation;
import org.n52.iceland.w3c.W3CConstants;
import org.n52.javaps.coding.stream.MissingStreamWriterException;
import org.n52.javaps.coding.stream.StreamWriter;
import org.n52.javaps.coding.stream.StreamWriterRepository;

import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

public abstract class AbstractXmlStreamWriter<S> implements XmlStreamWriter<S> {
    private static final String VERSION = "1.0";
    private static final String ENCODING = "UTF-8";
    private static final Escaper ESCAPER = XmlEscapers.xmlContentEscaper();
    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newFactory();
    private final Map<String, String> prefixes = new HashMap<>();
    private XMLEventWriter writer;

    private StreamWriterRepository streamWriterRepository;

    @Inject
    public void setStreamWriterRepository(StreamWriterRepository streamWriterRepository) {
        this.streamWriterRepository = streamWriterRepository;
    }

    @Override
    public void write(S object, OutputStream stream) throws OwsExceptionReport {
        try {
            prefixes.clear();
            XMLEventWriter w = outputFactory().createXMLEventWriter(stream, ENCODING);
            w.add(eventFactory().createStartDocument(ENCODING, VERSION));
            write(object, w);
            w.add(eventFactory().createEndDocument());
        } catch (XMLStreamException ex) {
            throw new NoApplicableCodeException().causedBy(ex);
        }
    }

    @Override
    public synchronized void write(S object, XMLEventWriter writer)
            throws XMLStreamException {
        this.writer = Objects.requireNonNull(writer);
        write(object);
    }

    protected XMLEventWriter writer() {
        return writer;
    }

    protected abstract void write(S object) throws XMLStreamException ;

    protected void attr(QName name, String value) throws XMLStreamException {
        dispatch(eventFactory().createAttribute(name, value));
    }

    protected void attr(String name, String value) throws XMLStreamException {
        attr(new QName(name), value);
    }

    protected void attr(String namespace, String localName, String value) throws XMLStreamException {
        attr(new QName(namespace, localName), value);
    }

    protected void namespace(String prefix, String namespace)
            throws XMLStreamException {
        String ns = prefixes.get(prefix);
        if (ns != null && !ns.equals(namespace)) {
            throw new XMLStreamException(String.format("Prefix <%s> is already bound to <%s>", namespace, ns));
        }
        dispatch(eventFactory().createNamespace(prefix, namespace));
        prefixes.put(prefix, namespace);
    }

    protected void start(String namespace, String localName) throws XMLStreamException {
        start(new QName(namespace, localName));
    }

    protected void start(String namespace, String localName, String prefix) throws XMLStreamException {
        start(new QName(namespace, localName, prefix));
    }

    protected void start(QName name) throws XMLStreamException {
        StartElement event = eventFactory().createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart());
        dispatch(event);
    }

    protected void empty(QName name) throws XMLStreamException {
        start(name);
        end(name);
    }

    protected void chars(String chars) throws XMLStreamException {
        dispatch(eventFactory().createCharacters(chars));
    }

    protected void chars(String chars, boolean escape) throws XMLStreamException {
        dispatch(eventFactory().createCharacters(escape ? escaper().escape(chars) : chars));
    }

    protected void end(QName name) throws XMLStreamException {
        dispatch(eventFactory().createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void endInline(QName name) throws XMLStreamException {
        dispatch(eventFactory().createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void finish() throws XMLStreamException {
        flush();
        writer().close();
    }

    protected void flush() throws XMLStreamException {
        writer().flush();
    }

    protected XMLEventFactory getXmlEventFactory() {
        return eventFactory();
    }

    /**
     * Write {@link TimeInstant} to stream
     *
     * @param time
     *             {@link TimeInstant} to write to stream
     *
     * @throws XMLStreamException
     *                            If an error occurs when writing to {@link OutputStream}
     */
    protected void time(TimeInstant time) throws XMLStreamException {
        time(time.getTimePosition());
    }

    /**
     * Write {@link TimePosition} as ISO 8601 to stream
     *
     * @param time {@link TimePosition} to write as ISO 8601 to stream
     *
     * @throws XMLStreamException If an error occurs when writing to
     *                            {@link OutputStream}
     */
    protected void time(TimePosition time) throws XMLStreamException {
        chars(DateTimeHelper.formatDateTime2IsoString(time.getTime()));
    }

    /**
     * Write {@link SchemaLocation}s as xsi:schemaLocations attribute to stream
     *
     * @param schemaLocations
     *                        {@link SchemaLocation}s to write
     *
     * @throws XMLStreamException
     *                            If an error occurs when writing to {@link OutputStream}
     */
    protected void schemaLocation(Set<SchemaLocation> schemaLocations)
            throws XMLStreamException {
        String merged = mergeSchemaLocationsToString(schemaLocations);
        if (StringHelper.isNotEmpty(merged)) {
            namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
            attr(W3CConstants.NS_XSI, W3CConstants.SCHEMA_LOCATION, merged);
        }
    }

    /**
     * Get the {@link XMLOutputFactory}
     *
     * @return the {@link XMLOutputFactory}
     */
    protected XMLOutputFactory getXmlOutputFactory() {
        outputFactory().setProperty("escapeCharacters", false);
        return outputFactory();
    }

    protected void xlinkHrefAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, value);
    }

    protected void xlinkTitleAttr(String value) throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_TITLE, value);
    }

    private void dispatch(XMLEvent event) throws XMLStreamException {
        writer().add(event);
    }

    private static String mergeSchemaLocationsToString(
            Iterable<SchemaLocation> schemaLocations) {
        if (schemaLocations == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator<SchemaLocation> it = schemaLocations.iterator();
        if (it.hasNext()) {
            builder.append(it.next().getSchemaLocationString());
            while (it.hasNext()) {
                builder.append(" ").append(it.next().getSchemaLocationString());
            }
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    protected <T> void delegate(T object) throws XMLStreamException {
        if (object != null) {
            XmlStreamWriterKey key = new XmlStreamWriterKey(object.getClass());
            Optional<StreamWriter<Object>> writer = this.streamWriterRepository.getWriter(key);
            if (!writer.isPresent() || !(writer.get() instanceof XmlStreamWriter)) {
                throw new MissingStreamWriterException(key);
            }
            ((XmlStreamWriter<T>)writer.get()).write(object, writer());
        }
    }

    /**
     * @return the xmlContentEscaper
     */
    protected static Escaper escaper() {
        return ESCAPER;
    }

    /**
     * @return the eventFactory
     */
    protected static XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the outputFactory
     */
    protected static XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }
}
