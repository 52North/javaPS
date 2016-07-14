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
package org.n52.javaps.io.literal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.codec.binary.Base64InputStream;

import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescription;
import org.n52.iceland.util.XmlFactories;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.InputHandler;

import com.google.common.io.CharStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralInputHandler extends XmlFactories implements InputHandler {

    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(LiteralData.class);
    private static final Set<Format> FORMATS = new HashSet<>(Arrays
            .asList(Format.APPLICATION_XML, Format.TEXT_XML, Format.TEXT_PLAIN, Format.TEXT_PLAIN.withBase64Encoding()));

    private static final QName QN_DATA_TYPE = new QName("dataType");
    private static final QName QN_UOM = new QName("uom");
    private static final QName QN_LITERAL_VALUE = new QName("http://www.opengis.net/wps/2.0", "LiteralValue");

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(FORMATS);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description, InputStream input, Format format)
            throws IOException, DecodingException {
        if (format.isXML()) {
            return parseXML(description.asLiteral(), input, format);
        } else if (format.isBase64()) {
            return parsePlain(description.asLiteral(), new Base64InputStream(input, false), format.withoutEncoding());
        } else {
            return parsePlain(description.asLiteral(), input, format);
        }
    }

    private LiteralData parsePlain(TypedLiteralInputDescription description, InputStream input, Format format)
            throws IOException, DecodingException {
        Charset charset = format.getEncodingAsCharsetOrDefault();
        try (InputStreamReader reader = new InputStreamReader(input, charset)) {
            String string = CharStreams.toString(reader);
            return description.getType().parseToBinding(string);
        }
    }

    private LiteralData parseXML(TypedLiteralInputDescription description, InputStream input, Format format)
            throws IOException, DecodingException {
        Charset charset = format.getEncodingAsCharsetOrDefault();
        try (Reader reader = new InputStreamReader(input, charset)) {
            return parseData(description, reader);
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        }
    }

    private LiteralData parseData(TypedLiteralInputDescription description, Reader reader)
            throws XMLStreamException, DecodingException {
        XMLEventReader xmlReader = inputFactory().createXMLEventReader(reader);
        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(QN_LITERAL_VALUE)) {
                    // TODO check data type?
                    URI dataType = Optional.ofNullable(start.getAttributeByName(QN_DATA_TYPE)).map(Attribute::getValue)
                            .map(URI::create).orElse(null);
                    String uom = Optional.ofNullable(start.getAttributeByName(QN_UOM)).map(Attribute::getValue)
                            .orElse(null);
                    return description.getType().parseToBinding(xmlReader.getElementText(), uom);
                } else {
                    throw unexpectedTag(start);
                }
            }
        }
        throw eof();
    }
}
