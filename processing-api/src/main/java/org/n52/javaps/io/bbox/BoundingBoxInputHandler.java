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

import org.n52.iceland.ogc.ows.OwsBoundingBox;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessInputDescription;
import org.n52.iceland.util.XmlFactories;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.InputHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxInputHandler extends XmlFactories implements InputHandler {

    private static final HashSet<Format> FORMATS = new HashSet<>(Arrays.asList(Format.TEXT_XML,
                                                                               Format.APPLICATION_XML));
    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(BoundingBoxData.class);

    private static final String NS_URI = "http://www.opengis.net/ows/2.0";
    private static final QName QN_BOUNDING_BOX = new QName(NS_URI, "BoundingBox");
    private static final QName QN_CRS = new QName("crs");
    private static final QName QN_DIMENSION = new QName("dimension");
    private static final QName QN_LOWER_CORNER = new QName(NS_URI, "LowerCorner");
    private static final QName QN_UPPER_CORNER = new QName(NS_URI, "UpperCorner");

    @Override
    public Data<?> parse(TypedProcessInputDescription<?> description, InputStream input, Format format)
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

    private OwsBoundingBox parseBoundingBox(StartElement elem, XMLEventReader xmlReader) throws XMLStreamException {

        URI crs = Optional.ofNullable(elem.getAttributeByName(QN_CRS))
                .map(Attribute::getValue)
                .map(URI::create)
                .orElse(null);
        Integer dimension = Optional.ofNullable(elem.getAttributeByName(QN_DIMENSION))
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
                QName name = start.getName();
                if (name.equals(QN_LOWER_CORNER)) {
                    lowerCorner = Arrays.stream(xmlReader.getElementText().split(" "))
                            .mapToDouble(Double::parseDouble).toArray();
                } else if (name.equals(QN_UPPER_CORNER)) {
                    upperCorner = Arrays.stream(xmlReader.getElementText().split(" "))
                            .mapToDouble(Double::parseDouble).toArray();
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

}
