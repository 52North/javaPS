package org.n52.javaps.coding.xml;

import java.io.OutputStream;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

/**
 * @author Christian Autermann
 */
public interface XmlStreamWriter<T> {

    void write(T object, XMLEventWriter writer)
            throws XMLStreamException;

    void write(T object, OutputStream stream)
            throws XMLStreamException;
}
