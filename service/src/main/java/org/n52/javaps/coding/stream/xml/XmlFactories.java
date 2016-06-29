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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.n52.javaps.coding.stream.xml.impl.XMLConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class XmlFactories {

    private static final TransformerFactory TRANSFORMER_FACTORY = createTransformerFactory();
    private static final XMLEventFactory EVENT_FACTORY = createEventFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = createOutputFactory();
    private static final XMLInputFactory INPUT_FACTORY = createInputFactory();

    private static final Charset DOCUMENT_ENCODING = StandardCharsets.UTF_8;
    private static final String XML_VERSION = XMLConstants.XML_VERSION;
    private static final String INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";

    /**
     * @return the event factory
     */
    public static XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the output factory
     */
    public static XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }

    /**
     * @return the input factory
     */
    public static XMLInputFactory inputFactory() {
        return INPUT_FACTORY;
    }

    /**
     * @return the document encoding
     */
    public static Charset documentEncoding() {
        return DOCUMENT_ENCODING;
    }

    /**
     * @return the document version
     */
    public static String documentVersion() {
        return XML_VERSION;
    }

    /**
     * @return the transformer factory
     */
    public static TransformerFactory transformerFactory() {
        return TRANSFORMER_FACTORY;
    }

    private static TransformerFactory createTransformerFactory() {
        TransformerFactory factory = TransformerFactory.newInstance();
        //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "false");
        //factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "false");
        return factory;
    }


    private static XMLInputFactory createInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        return factory;
    }

    private static XMLOutputFactory createOutputFactory() {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();

        factory.setProperty("escapeCharacters", false);

        return factory;
    }

    private static XMLEventFactory createEventFactory() {
        XMLEventFactory factory = XMLEventFactory.newFactory();
        return factory;
    }

    protected static Transformer createIndentingTransformer()
            throws TransformerException {
        Transformer transformer = transformerFactory().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        try {
            transformer.setOutputProperty(INDENT_AMOUNT, "2");
        } catch (IllegalArgumentException e) {
            // ignore
        }

        return transformer;
    }

    protected static XMLStreamException unexpectedTag(StartElement element) {
        String message = String.format("unexpected tag: %s", element.getName());
        return new XMLStreamException(message, element.getLocation());
    }

    protected static XMLStreamException eof() {
        return new XMLStreamException("premature end of file");
    }

}
