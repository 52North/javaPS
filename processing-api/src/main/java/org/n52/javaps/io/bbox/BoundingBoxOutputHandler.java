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
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.codec.binary.Base64InputStream;

import org.n52.iceland.ogc.ows.OwsBoundingBox;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.iceland.util.XmlFactories;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.OutputHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxOutputHandler extends XmlFactories implements OutputHandler {

    private static final HashSet<Format> FORMATS = new HashSet<>(Arrays.asList(Format.TEXT_XML,
                                                                               Format.APPLICATION_XML));
    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);

    private static final String NS_URI = "http://www.opengis.net/ows/2.0";
    private static final String NS_PREFIX = "ows";
    private static final String EN_UPPER_CORNER = "UpperCorner";
    private static final String EN_LOWER_CORNER = "LowerCorner";
    private static final String EN_BOUNDING_BOX = "BoundingBox";
    private static final String AN_DIMENSION = "dimension";
    private static final String AN_CRS = "crs";

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException {
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

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(FORMATS);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    private void writeBoundingBox(Writer writer, OwsBoundingBox bbox) throws XMLStreamException {
        XMLStreamWriter xmlWriter = outputFactory().createXMLStreamWriter(writer);
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

}