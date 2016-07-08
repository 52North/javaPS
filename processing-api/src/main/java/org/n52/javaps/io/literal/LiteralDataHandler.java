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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.complex.IGenerator;
import org.n52.javaps.io.complex.IParser;

import com.google.common.io.CharStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataHandler implements IParser, IGenerator {

    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(LiteralData.class);
    private static final Set<Format> FORMATS = new HashSet<>(Arrays.asList(new Format("application/xml"),
                                                                           new Format("text/xml"),
                                                                           new Format("text/plain")));
    private static final String NS_WPS = "wps";
    private static final String NS_WPS_URI = "http://www.opengis.net/wps/2.0";
    private static final String EN_LITERAL_VALUE = "LiteralValue";
    private static final String AN_UOM = "uom";
    private static final String AN_DATA_TYPE = "dataType";
    private static final QName QN_DATA_TYPE = new QName(AN_DATA_TYPE);
    private static final QName QN_UOM = new QName(AN_UOM);
    private static final QName QN_LITERAL_VALUE = new QName(NS_WPS_URI, EN_LITERAL_VALUE);

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(FORMATS);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    @Override
    public Data<?> decode(TypedProcessInputDescription<?> description, InputStream input, Format format)
            throws IOException, DecodingException {
        if (format.isXML()) {
            return parseXML(description.asLiteral(), input, format);
        } else {
            return parsePlain(description.asLiteral(), input, format);
        }
    }

    @Override
    public InputStream encode(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException {
        if (format.isXML()) {
            return generateXML(description.asLiteral(), (LiteralData) data, format);
        } else {
            return generatePlain(description.asLiteral(), (LiteralData) data, format);
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

    private InputStream generatePlain(TypedLiteralOutputDescription description, LiteralData data, Format format)
            throws EncodingException {
        return toStream(toString(description, data), format);
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

    private InputStream generateXML(TypedLiteralOutputDescription description, LiteralData data, Format format)
            throws IOException, EncodingException {
        try {
            StringWriter writer = new StringWriter();
            writeData(description, writer, data);
            return toStream(writer.toString(), format);
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        }
    }

    private String toString(TypedLiteralOutputDescription description, LiteralData data)
            throws EncodingException {
        @SuppressWarnings("unchecked")
        LiteralType<Object> type = (LiteralType<Object>) description.getType();
        String value = type.generate(data.getPayload());
        return value;
    }

    private void writeData(TypedLiteralOutputDescription description, Writer writer, LiteralData data)
            throws XMLStreamException, EncodingException {
        XMLStreamWriter xmlWriter = this.outputFactory.createXMLStreamWriter(writer);
        try {
            writeData(description, xmlWriter, data);
        } finally {
            xmlWriter.close();
        }
    }

    private void writeData(TypedLiteralOutputDescription description, XMLStreamWriter writer, LiteralData data)
            throws XMLStreamException, EncodingException {
        writer.writeStartElement(NS_WPS, EN_LITERAL_VALUE, NS_WPS_URI);
        writer.writeNamespace(NS_WPS, NS_WPS_URI);
        writer.writeAttribute(AN_DATA_TYPE, description.getType().getURI().toString());
        if (data.getUnitOfMeasurement().isPresent()) {
            writer.writeAttribute(AN_UOM, data.getUnitOfMeasurement().get());
        }
        writer.writeCharacters(toString(description, data));
        writer.writeEndElement();
    }

    private LiteralData parseData(TypedLiteralInputDescription description, Reader reader)
            throws XMLStreamException, DecodingException {
        LiteralData literalData = null;
        XMLEventReader xmlReader = inputFactory.createXMLEventReader(reader);
        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(QN_LITERAL_VALUE)) {
                    // TODO check data type?
                    URI dataType = Optional.ofNullable(start.getAttributeByName(QN_DATA_TYPE)).map(Attribute::getValue).map(URI::create).orElse(null);
                    String uom = Optional.ofNullable(start.getAttributeByName(QN_UOM)).map(Attribute::getValue).orElse(null);
                    literalData = description.getType().parseToBinding(xmlReader.getElementText(), uom);
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return literalData;
            }
         }
        throw eof();
    }


    private static XMLStreamException unexpectedTag(StartElement element) {
        String message = String.format("unexpected tag: %s", element.getName());
        return new XMLStreamException(message, element.getLocation());
    }

    private static XMLStreamException eof() {
        return new XMLStreamException("premature end of file");
    }

    private static InputStream toStream(String value, Format format) {
        Charset charset = format.getEncodingAsCharsetOrDefault();
        return new ByteArrayInputStream(value.getBytes(charset));
    }

}
