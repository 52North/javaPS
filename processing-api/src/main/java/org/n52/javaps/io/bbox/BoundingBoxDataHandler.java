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
package org.n52.javaps.io.bbox;

import static java.util.stream.Collectors.joining;

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

import org.n52.iceland.ogc.ows.OwsBoundingBox;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.complex.IGenerator;
import org.n52.javaps.io.complex.IParser;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxDataHandler implements IGenerator, IParser {

    private static final HashSet<Format> FORMATS = new HashSet<>(Arrays.asList(new Format("text/xml"),
                                                                               new Format("application/xml")));
    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);

    private static final String NS_URI = "http://www.opengis.net/ows/2.0";
    private static final String NS_PREFIX = "ows";
    private static final String EN_UPPER_CORNER = "UpperCorner";
    private static final String EN_LOWER_CORNER = "LowerCorner";
    private static final String EN_BOUNDING_BOX = "BoundingBox";
    private static final String AN_DIMENSION = "dimension";
    private static final String AN_CRS = "crs";

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
    private final XMLInputFactory inputFactory = XMLInputFactory.newFactory();

    @Override
    public InputStream encode(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException {
        try {
            OwsBoundingBox bbox = (OwsBoundingBox) data.getPayload();
            StringWriter writer = new StringWriter();
            writeBoundingBox(writer, bbox);
            String string = writer.toString();
            Charset charset = format.getEncodingAsCharsetOrDefault();
            byte[] bytes = string.getBytes(charset);
            return new ByteArrayInputStream(bytes);
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Data<?> decode(TypedProcessInputDescription<?> description, InputStream input, Format format)
            throws IOException {
        Charset charset = format.getEncodingAsCharsetOrDefault();
        try (Reader reader = new InputStreamReader(input, charset)) {
            return parseBoundingBoxData(reader);
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(FORMATS);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    private void writeBoundingBox(Writer writer, OwsBoundingBox bbox) throws XMLStreamException {
        XMLStreamWriter xmlWriter = this.outputFactory.createXMLStreamWriter(writer);
        try {
            writeBoundingBox(xmlWriter, bbox);
        } finally {
            xmlWriter.close();
        }
    }

    private void writeBoundingBox(XMLStreamWriter writer, OwsBoundingBox bbox) throws XMLStreamException {
        writer.writeStartElement(NS_PREFIX, EN_BOUNDING_BOX, NS_URI);
        writer.writeNamespace(NS_PREFIX, NS_URI);
        if (bbox.getCRS().isPresent()) {
            writer.writeAttribute(AN_CRS, bbox.getCRS().get().toString());
        }
        writer.writeAttribute(AN_DIMENSION, String.valueOf(bbox.getDimension()));
        writeLowerCorner(writer, bbox.getLowerCorner());
        writeUpperCorner(writer, bbox.getUpperCorner());
        writer.writeEndElement();
    }

    private void writeLowerCorner(XMLStreamWriter writer, double[] lowerCorner) throws XMLStreamException {
        writer.writeStartElement(NS_PREFIX, EN_LOWER_CORNER, NS_URI);
        writerPositionType(writer, lowerCorner);
        writer.writeEndElement();
    }

    private void writeUpperCorner(XMLStreamWriter writer, double[] upperCorner) throws XMLStreamException {
        writer.writeStartElement(NS_PREFIX, EN_UPPER_CORNER, NS_URI);
        writerPositionType(writer, upperCorner);
        writer.writeEndElement();
    }

    private void writerPositionType(XMLStreamWriter writer, double[] coordinates) throws XMLStreamException {
        writer.writeCharacters(Arrays.stream(coordinates).mapToObj(String::valueOf).collect(joining(" ")));
    }

    private OwsBoundingBox parseBoundingBox(StartElement elem, XMLEventReader xmlReader) throws XMLStreamException {

        URI crs = Optional.ofNullable(elem.getAttributeByName(new QName(AN_CRS)))
                .map(Attribute::getValue)
                .map(URI::create)
                .orElse(null);
        Integer dimension = Optional.ofNullable(elem.getAttributeByName(new QName(AN_DIMENSION)))
                .map(Attribute::getValue)
                .filter(s -> !s.isEmpty())
                .map(Integer::valueOf)
                .filter(x -> x > 0)
                .orElse(null);

        double[] lowerCorner = null;
        double[] upperCorner = null;

        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(new QName(NS_URI, EN_LOWER_CORNER))) {
                    lowerCorner = Arrays.stream(xmlReader.getElementText().split(" "))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                } else if (start.getName().equals(new QName(NS_URI, EN_UPPER_CORNER))) {
                    upperCorner = Arrays.stream(xmlReader.getElementText().split(" "))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                if (dimension == null) {
                    return new OwsBoundingBox(lowerCorner, upperCorner, crs);
                } else {
                    return new OwsBoundingBox(lowerCorner, upperCorner, dimension, crs);
                }
            }
        }
        throw eof();
    }

    private Data<?> parseBoundingBoxData(Reader reader) throws XMLStreamException {
        OwsBoundingBox bbox = null;
        XMLEventReader xmlReader = inputFactory.createXMLEventReader(reader);

        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(new QName(NS_URI, EN_BOUNDING_BOX))) {
                    bbox = parseBoundingBox(start, xmlReader);
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return new BoundingBoxData(bbox);
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

}
