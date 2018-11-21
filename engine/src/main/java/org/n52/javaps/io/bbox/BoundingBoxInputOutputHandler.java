/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.codec.binary.Base64InputStream;

import org.n52.shetland.ogc.ows.OwsBoundingBox;
import org.n52.shetland.ogc.wps.Format;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.iceland.util.XmlFactories;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.InputHandler;
import org.n52.javaps.io.OutputHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxInputOutputHandler extends XmlFactories implements InputHandler, OutputHandler {

    public static final Set<Format> FORMATS =
            Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(Format.APPLICATION_XML, Format.TEXT_XML)));

    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);

    private static final String NS_URI = "http://www.opengis.net/ows/2.0";

    private static final String NS_PREFIX = "ows";

    private static final String EN_UPPER_CORNER = "UpperCorner";

    private static final String EN_LOWER_CORNER = "LowerCorner";

    private static final String EN_BOUNDING_BOX = "BoundingBox";

    private static final String AN_DIMENSION = "dimension";

    private static final String AN_CRS = "crs";

    private static final QName QN_BOUNDING_BOX = new QName(NS_URI, EN_BOUNDING_BOX);

    private static final QName QN_CRS = new QName(AN_CRS);

    private static final QName QN_DIMENSION = new QName(AN_DIMENSION);

    private static final QName QN_LOWER_CORNER = new QName(NS_URI, EN_LOWER_CORNER);

    private static final QName QN_UPPER_CORNER = new QName(NS_URI, EN_UPPER_CORNER);

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description,
            InputStream input,
            Format format) throws IOException {
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

    private Data<?> parseBoundingBoxData(Reader reader) throws XMLStreamException {
        OwsBoundingBox bbox = null;
        XMLEventReader xmlReader = inputFactory().createXMLEventReader(reader);

        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(QN_BOUNDING_BOX)) {
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

    private OwsBoundingBox parseBoundingBox(StartElement elem,
            XMLEventReader xmlReader) throws XMLStreamException {

        URI crs = Optional.ofNullable(elem.getAttributeByName(QN_CRS)).map(Attribute::getValue).map(URI::create)
                .orElse(null);
        Integer dimension = Optional.ofNullable(elem.getAttributeByName(QN_DIMENSION)).map(Attribute::getValue)
                .filter(s -> !s.isEmpty()).map(Integer::valueOf).filter(x -> x > 0).orElse(null);

        double[] lowerCorner = null;
        double[] upperCorner = null;

        while (xmlReader.hasNext()) {
            XMLEvent event = xmlReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                QName name = start.getName();
                if (name.equals(QN_LOWER_CORNER)) {
                    lowerCorner = Arrays.stream(xmlReader.getElementText().split(" ")).mapToDouble(Double::parseDouble)
                            .toArray();
                } else if (name.equals(QN_UPPER_CORNER)) {
                    upperCorner = Arrays.stream(xmlReader.getElementText().split(" ")).mapToDouble(Double::parseDouble)
                            .toArray();
                } else {
                    throw unexpectedTag(start);
                }
                if (upperCorner != null && lowerCorner != null) {
                    if (dimension == null) {
                        return new OwsBoundingBox(lowerCorner, upperCorner, crs);
                    } else {
                        return new OwsBoundingBox(lowerCorner, upperCorner, dimension, crs);
                    }
                }
            }
        }
        throw eof();
    }

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description,
            Data<?> data,
            Format format) throws IOException {
        try {
            Charset charset = format.getEncodingAsCharsetOrDefault();

            byte[] bytes = encode(data).getBytes(charset);
            InputStream stream = new ByteArrayInputStream(bytes);

            if (format.isBase64()) {
                stream = new Base64InputStream(stream, true);
            }

            return stream;
        } catch (XMLStreamException ex) {
            throw new IOException(ex);
        }
    }

    private String encode(Data<?> data) throws XMLStreamException {
        OwsBoundingBox bbox = (OwsBoundingBox) data.getPayload();
        StringWriter writer = new StringWriter();
        writeBoundingBox(writer, bbox);
        return writer.toString();
    }

    private void writeBoundingBox(Writer writer,
            OwsBoundingBox bbox) throws XMLStreamException {
        XMLStreamWriter xmlWriter = outputFactory().createXMLStreamWriter(writer);
        try {
            writeBoundingBox(xmlWriter, bbox);
        } finally {
            xmlWriter.close();
        }
    }

    private void writeBoundingBox(XMLStreamWriter writer,
            OwsBoundingBox bbox) throws XMLStreamException {
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

    private void writeLowerCorner(XMLStreamWriter writer,
            double[] lowerCorner) throws XMLStreamException {
        writer.writeStartElement(NS_PREFIX, EN_LOWER_CORNER, NS_URI);
        writerPositionType(writer, lowerCorner);
        writer.writeEndElement();
    }

    private void writeUpperCorner(XMLStreamWriter writer,
            double[] upperCorner) throws XMLStreamException {
        writer.writeStartElement(NS_PREFIX, EN_UPPER_CORNER, NS_URI);
        writerPositionType(writer, upperCorner);
        writer.writeEndElement();
    }

    private void writerPositionType(XMLStreamWriter writer,
            double[] coordinates) throws XMLStreamException {
        writer.writeCharacters(Arrays.stream(coordinates).mapToObj(String::valueOf).collect(joining(" ")));
    }

}
