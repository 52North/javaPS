package org.n52.javaps.coding.xml;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.ogc.gml.time.TimeInstant;
import org.n52.iceland.ogc.gml.time.TimePosition;
import org.n52.iceland.util.DateTimeHelper;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.w3c.SchemaLocation;
import org.n52.iceland.w3c.W3CConstants;

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

    @Override
    public void write(S object, OutputStream stream) throws XMLStreamException {
        prefixes.clear();
        XMLEventWriter w = outputFactory().createXMLEventWriter(stream, ENCODING);
        w.add(eventFactory().createStartDocument(ENCODING, VERSION));
        write(object, w);
        w.add(eventFactory().createEndDocument());
    }

    @Override
    public synchronized void write(S object, XMLEventWriter writer)
            throws XMLStreamException {
        this.writer = Objects.requireNonNull(writer);
        write(object);
    }

    /**
     * @return the writer
     */
    private XMLEventWriter writer() {
        return writer;
    }

    protected abstract void write(S object) throws XMLStreamException ;

    protected void attr(QName name, String value) throws XMLStreamException {
        writer().add(eventFactory().createAttribute(name, value));
    }

    protected void attr(String name, String value) throws XMLStreamException {
        writer().add(eventFactory().createAttribute(name, value));
    }

    protected void attr(String namespace, String localName, String value) throws XMLStreamException {
        writer().add(eventFactory().createAttribute(null, namespace, localName, value));
    }

    protected void namespace(String prefix, String namespace)
            throws XMLStreamException {
        String ns = prefixes.get(prefix);
        if (ns != null && !ns.equals(namespace)) {
            throw new XMLStreamException(String.format("Prefix <%s> is already bound to <%s>", namespace, ns));
        }
        writer().add(eventFactory().createNamespace(prefix, namespace));
        prefixes.put(prefix, namespace);
    }

    protected void start(QName name) throws XMLStreamException {
        writer().add(eventFactory().createStartElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void empty(QName name) throws XMLStreamException {
        start(name);
        end(name);
    }

    protected void chars(String chars) throws XMLStreamException {
        writer().add(eventFactory().createCharacters(chars));
    }

    protected void chars(String chars, boolean escape) throws XMLStreamException {
        writer().add(eventFactory().createCharacters(escape ? escaper().escape(chars) : chars));
    }

    protected void end(QName name) throws XMLStreamException {
        writer().add(eventFactory().createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
    }

    protected void endInline(QName name) throws XMLStreamException {
        writer().add(eventFactory().createEndElement(name.getPrefix(), name.getNamespaceURI(), name.getLocalPart()));
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
