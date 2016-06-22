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

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.description.Format;
import org.n52.javaps.ogc.wps.data.Body;
import org.n52.javaps.ogc.wps.data.ReferenceData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferenceDataWriter extends AbstractXmlElementStreamWriter<ReferenceData> {

    public ReferenceDataWriter() {
        super(ReferenceData.class);
    }

    @Override
    protected void write(ReferenceData reference)
            throws XMLStreamException {

        Format format = reference.getFormat();
        element(WPS.Elem.QN_REFERENCE, reference, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);

            attr(XLink.Attr.QN_HREF, reference.getURI().toString());
            attr(WPS.Attr.AN_MIME_TYPE, format.getMimeType());
            attr(WPS.Attr.AN_ENCODING, format.getEncoding());
            attr(WPS.Attr.AN_SCHEMA, format.getSchema());

            if (reference.getBody().isPresent()) {
                Body body = reference.getBody().get();
                if (body.isInline()) {
                    element(WPS.Elem.QN_BODY, body.asInline(), b -> cdata(b.getBody()));
                } else if (body.isReferenced()) {
                    element(WPS.Elem.QN_BODY_REFERENCE, body.asReferenced(), b
                            -> attr(XLink.Attr.QN_HREF, b.getHref().toString()));
                } else {
                    throw new AssertionError();
                }
            }
        });
    }
}
