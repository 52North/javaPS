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
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.ogc.gml.time.TimeInstant;
import org.n52.iceland.ogc.gml.time.TimePosition;
import org.n52.iceland.util.DateTimeHelper;
import org.n52.iceland.w3c.SchemaLocation;
import org.n52.iceland.w3c.W3CConstants;

import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

public abstract class AbstractXmlElementStreamWriter<S>
        implements XmlElementStreamWriter {

    private static final Escaper ESCAPER = XmlEscapers.xmlContentEscaper();

    private XmlStreamWritingContext context;
    private XMLEventFactory eventFactory;
    private XMLOutputFactory outputFactory;

    @Override
    public void setContext(XmlStreamWritingContext context) {
        this.context = Objects.requireNonNull(context);
        this.eventFactory = context.eventFactory();
        this.outputFactory = context.outputFactory();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeElement(Object object)
            throws XMLStreamException {
        if (this.context == null) {
            throw new IllegalStateException();
        }
        write((S) object);
    }

    protected abstract void write(S object)
            throws XMLStreamException;

    protected void attr(QName name, String value)
            throws XMLStreamException {
        this.context.dispatch(this.eventFactory.createAttribute(name, value));
    }

    protected void attr(String name, String value)
            throws XMLStreamException {
        attr(new QName(name), value);
    }

    protected void attr(String namespace, String localName, String value)
            throws XMLStreamException {
        attr(new QName(namespace, localName), value);
    }

    protected void namespace(String prefix, String namespace)
            throws XMLStreamException {
        if (this.context.declareNamespace(prefix, namespace)) {
            this.context.dispatch(this.eventFactory.createNamespace(prefix, namespace));
        }
    }

    protected void start(String namespace, String localName)
            throws XMLStreamException {
        start(new QName(namespace, localName));
    }

    protected void start(String namespace, String localName, String prefix)
            throws XMLStreamException {
        start(new QName(namespace, localName, prefix));
    }

    protected void start(QName name)
            throws XMLStreamException {
        this.context.dispatch(this.eventFactory.createStartElement(
                name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void empty(QName name)
            throws XMLStreamException {
        start(name);
        end(name);
    }

    protected void chars(String chars)
            throws XMLStreamException {
        this.context.dispatch(this.eventFactory.createCharacters(chars));
    }

    protected void chars(String chars, boolean escape)
            throws XMLStreamException {
        this.context.dispatch(this.eventFactory.createCharacters(escape ? escaper()
                .escape(chars) : chars));
    }

    protected void end(QName name)
            throws XMLStreamException {
        this.context.dispatch(this.eventFactory.createEndElement(
                name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void flush()
            throws XMLStreamException {
        this.context.flush();
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
    protected void time(TimeInstant time)
            throws XMLStreamException {
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
    protected void time(TimePosition time)
            throws XMLStreamException {
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
        if (!Strings.isNullOrEmpty(merged)) {
            namespace(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
            attr(W3CConstants.NS_XSI, W3CConstants.SCHEMA_LOCATION, merged);
        }
    }

    /**
     * Get the {@link XMLOutputFactory}
     *
     * @return the {@link XMLOutputFactory}
     */
    protected XMLOutputFactory outputFactory() {
        return outputFactory;
    }

    protected void xlinkHrefAttr(String value)
            throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_HREF, value);
    }

    protected void xlinkTitleAttr(String value)
            throws XMLStreamException {
        attr(W3CConstants.QN_XLINK_TITLE, value);
    }

    protected void element(QName name, String value)
            throws XMLStreamException {
        start(name);
        chars(value);
        end(name);
    }

    protected <T> void delegate(T object)
            throws XMLStreamException {
        this.context.write(object);
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

    /**
     * @return the xmlContentEscaper
     */
    protected static Escaper escaper() {
        return ESCAPER;
    }

}
