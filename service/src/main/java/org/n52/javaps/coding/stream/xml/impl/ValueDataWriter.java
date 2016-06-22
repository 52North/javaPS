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
package org.n52.javaps.coding.stream.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.description.Format;
import org.n52.javaps.ogc.wps.data.ValueData;

import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ValueDataWriter extends AbstractXmlElementStreamWriter<ValueData> {

    public ValueDataWriter() {
        super(ValueData.class);
    }

    @Override
    protected void write(ValueData value)
            throws XMLStreamException {
        element(WPS.Elem.QN_DATA, value, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);

            Format format = x.getFormat();
            try (InputStream data = x.getData()) {

                if (!x.getFormat().hasEncoding() || x.getFormat()
                    .isCharacterEncoding()) {

                    writeDataEncodingAttributes(format
                            .withEncoding(getDocumentEncoding()));

                    String encoding = format.getEncoding()
                            .orElse(Format.DEFAULT_ENCODING);
                    Charset charset = Charset.forName(encoding);

                    try (InputStreamReader reader
                            = new InputStreamReader(data, charset)) {
                        if (format.isXML()) {
                            write(reader);
                        } else {
                            cdata(CharStreams.toString(reader));
                        }
                    }

                } else {
                    // format describes some other thing, e.g. binary
                    BaseEncoding base64 = base64();
                    writeDataEncodingAttributes(format.withBase64Encoding());

                    if (format.isBase64()) {
                        // it is already base64 encoded
                        try (Reader reader
                                = new InputStreamReader(data, StandardCharsets.US_ASCII);
                             InputStream in = base64.decodingStream(reader)) {
                            writeBase64(in);
                        }
                    } else {
                        writeBase64(data);
                    }
                }
            } catch (IOException ex) {
                throw new XMLStreamException(ex);
            }
        });
    }

    private void writeDataEncodingAttributes(Format format)
            throws XMLStreamException {
        attr(WPS.Attr.AN_MIME_TYPE, format.getMimeType());
        attr(WPS.Attr.AN_ENCODING, format.getEncoding());
        attr(WPS.Attr.AN_SCHEMA, format.getSchema());
    }

}
