package org.n52.javaps.coding.stream.xml;

import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.n52.javaps.coding.stream.MissingStreamWriterException;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants;

import javanet.staxutils.IndentingXMLEventWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XmlStreamWritingContext {

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newFactory();

    static {
        OUTPUT_FACTORY.setProperty("escapeCharacters", false);
    }

    private final Deque<Map<String, String>> prefixes = new ArrayDeque<>();
    private final XMLEventWriter writer;
    private final BiFunction<XmlStreamWriterKey, XmlStreamWritingContext, Optional<XmlElementStreamWriter>> writerProvider;

    public XmlStreamWritingContext(OutputStream stream,
                                   BiFunction<XmlStreamWriterKey, XmlStreamWritingContext, Optional<XmlElementStreamWriter>> writerProvider)
            throws XMLStreamException {
        this.writer = new IndentingXMLEventWriter(outputFactory()
                .createXMLEventWriter(stream, XMLConstants.XML_ENCODING));
        this.writerProvider = Objects.requireNonNull(writerProvider);
    }

    public <T> void write(T object)
            throws XMLStreamException {
        if (object != null) {
            XmlStreamWriterKey key = new XmlStreamWriterKey(object.getClass());
            XmlElementStreamWriter delegate = this.writerProvider.apply(key, this)
                    .orElseThrow(() -> new MissingStreamWriterException(key));
            delegate.writeElement(object);
        }
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

    /**
     * @return the eventFactory
     */
    public final XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the outputFactory
     */
    public final XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }

    public void dispatch(XMLEvent event)
            throws XMLStreamException {
        if (event.isStartElement()) {
            this.prefixes.push(new HashMap<>(0));
        } else if (event.isEndElement()) {
            this.prefixes.pop();
        }
        this.writer.add(event);
    }

    public void finish()
            throws XMLStreamException {
        this.writer.flush();
        this.writer.close();
    }

    public void flush()
            throws XMLStreamException {
        this.writer.flush();
    }
}
