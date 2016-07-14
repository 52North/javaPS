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

import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.iceland.util.XmlFactories;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.OutputHandler;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralOutputHandler extends XmlFactories implements OutputHandler {

    private static final Set<Class<? extends Data<?>>> BINDINGS = Collections.singleton(LiteralData.class);
    private static final Set<Format> FORMATS = new HashSet<>(Arrays.asList(
            Format.APPLICATION_XML, Format.TEXT_XML,
            Format.TEXT_PLAIN, Format.TEXT_PLAIN.withBase64Encoding()));
    private static final String NS_WPS = "wps";
    private static final String NS_WPS_URI = "http://www.opengis.net/wps/2.0";
    private static final String EN_LITERAL_VALUE = "LiteralValue";
    private static final String AN_UOM = "uom";
    private static final String AN_DATA_TYPE = "dataType";

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(FORMATS);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return BINDINGS;
    }

    @Override
    public InputStream generate(TypedProcessOutputDescription<?> description, Data<?> data, Format format)
            throws IOException, EncodingException {
        LiteralData literalData = (LiteralData) data;
        TypedLiteralOutputDescription literalDescription = description.asLiteral();

        if (format.isXML()) {
            return generateXML(literalDescription, literalData, format);
        } else if (format.isBase64()) {
            InputStream stream = generatePlain(literalDescription, literalData, format.withoutEncoding());
            return new Base64InputStream(stream, true);
        } else {
            return generatePlain(literalDescription, literalData, format);
        }
    }

    private InputStream generatePlain(TypedLiteralOutputDescription description, LiteralData data, Format format)
            throws EncodingException {
        return toStream(toString(description, data), format);
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
        XMLStreamWriter xmlWriter = outputFactory().createXMLStreamWriter(writer);
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

    private static InputStream toStream(String value, Format format) {
        Charset charset = format.getEncodingAsCharsetOrDefault();
        return new ByteArrayInputStream(value.getBytes(charset));
    }

}
