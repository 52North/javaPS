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

import org.n52.javaps.coding.stream.xml.impl.XMLConstants;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class XmlFactories {

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newFactory();
    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newFactory();
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newFactory();
    private static final Charset DOCUMENT_ENCODING = StandardCharsets.UTF_8;
    private static final String XML_VERSION = XMLConstants.XML_VERSION;

    static {
        OUTPUT_FACTORY.setProperty("escapeCharacters", false);
    }

    /**
     * @return the event factory
     */
    protected static XMLEventFactory eventFactory() {
        return EVENT_FACTORY;
    }

    /**
     * @return the output factory
     */
    protected static XMLOutputFactory outputFactory() {
        return OUTPUT_FACTORY;
    }

    /**
     * @return the input factory
     */
    protected static XMLInputFactory inputFactory() {
        return INPUT_FACTORY;
    }


    protected static Charset documentEncoding() {
        return DOCUMENT_ENCODING;
    }

    protected static String documentVersion() {
        return XML_VERSION;
    }

    protected static XMLStreamException unexpectedTag(StartElement element) {
        String message = String.format("unexpected tag: %s", element.getName());
        return new XMLStreamException(message, element.getLocation());
    }

    protected static XMLStreamException eof() {
        return new XMLStreamException("premature end of file");
    }

}
