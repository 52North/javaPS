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
package org.n52.iceland.coding.stream.xml;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.n52.iceland.util.XmlFactories;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractElementXmlStreamReader extends XmlFactories implements ElementXmlStreamReader {

    protected byte[] asBytes(StartElement start, XMLEventReader reader)
            throws XMLStreamException {
        return asString(start, reader).getBytes(StandardCharsets.UTF_8);
    }

    protected String asString(StartElement start, XMLEventReader reader)
            throws XMLStreamException {
        StringWriter stringWriter = new StringWriter();
        XMLEventWriter writer = outputFactory()
                .createXMLEventWriter(stringWriter);

        // writer.add(eventFactory().createStartDocument());
        copy(reader, writer);
        // writer.add(eventFactory().createEndDocument());

        writer.close();
        return stringWriter.toString();
    }

    /**
     * Copies the content of current node of {@code reader} to {@code writer}.
     * Assumes that the current event is a START_ELEMENT and will proceed until
     * the corresponding END_ELEMENT is consumed.
     *
     * @param reader the reader
     * @param writer the writer
     *
     * @throws XMLStreamException if the copy operation fails
     */
    protected void copy(XMLEventReader reader, XMLEventWriter writer)
            throws XMLStreamException {
        int depth = 0;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement elem = event.asStartElement();
                QName elementName = elem.getName();
                String elementPrefix = elementName.getPrefix();
                String elementNamespace = elementName.getNamespaceURI();

                // check if the current element's namespace is declared
                // this has to be done before the START_ELEMENT event is emitted
                // as this would put the namespace into the writer's context
                boolean writeElementNamespace = !elementPrefix.isEmpty() &&
                                                !elementPrefix.equals("xml") &&
                                                Strings.isNullOrEmpty(writer
                                                        .getNamespaceContext()
                                                        .getNamespaceURI(elementPrefix));

                // emit the element without any attributes or namespaces
                writer.add(eventFactory()
                        .createStartElement(elementName, null, null));

                // iterate over all namespace declaration to check if the element namespace
                // is declared
                @SuppressWarnings("unchecked")
                Iterator<Namespace> namespaces = elem.getNamespaces();
                while (namespaces.hasNext()) {
                    Namespace namespace = namespaces.next();
                    // checks if the namespace declaration matches the current element
                    if (elementPrefix.equals(namespace.getPrefix()) &&
                        elementNamespace.equals(namespace.getNamespaceURI())) {
                        writeElementNamespace = false;
                    }
                    // declare the namespace
                    writer.add(eventFactory()
                            .createNamespace(Strings.nullToEmpty(namespace
                                    .getPrefix()),
                                             namespace.getNamespaceURI()));
                }

                // if the there is no namespace declaration for the current element, create one
                if (writeElementNamespace) {
                    writer.add(eventFactory()
                            .createNamespace(Strings.nullToEmpty(elementPrefix),
                                             elementNamespace));
                }

                // iterate over the attributes and check if there namespace is
                // declared, prior to emitting to ATTRIBUTE event
                @SuppressWarnings("unchecked")
                Iterator<Attribute> attributes = elem.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = attributes.next();
                    String attributePrefix = attribute.getName().getPrefix();
                    String attributeNamespace = attribute.getName()
                            .getNamespaceURI();

                    if (!attributePrefix.isEmpty() && !attributePrefix
                        .equals("xml") &&
                        Strings.isNullOrEmpty(writer.getNamespaceContext()
                                .getNamespaceURI(attributePrefix))) {
                        writer.add(eventFactory()
                                .createNamespace(attributePrefix,
                                                 attributeNamespace));
                    }
                    writer.add(eventFactory()
                            .createAttribute(attribute.getName(),
                                             attribute.getValue()));
                }
                ++depth;
            } else if (event.isEndElement()) {
                --depth;
                if (depth >= 0) {
                    writer.add(event);
                } else {
                    return; // we hit last closing tag
                }
            } else {
                writer.add(event);
            }

        }
        throw eof();
    }

    protected static Optional<String> getAttribute(StartElement event,
                                                   QName name) {
        Attribute attr = event.getAttributeByName(name);
        return Optional.ofNullable(attr).map(Attribute::getValue);
    }

    protected static Optional<String> getAttribute(StartElement event,
                                                   String name) {
        return getAttribute(event, new QName(name));
    }

}
