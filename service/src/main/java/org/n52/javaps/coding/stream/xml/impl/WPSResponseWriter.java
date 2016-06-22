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

import java.io.StringReader;

import javax.xml.stream.XMLStreamException;

import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.javaps.coding.stream.xml.AbstractMultiElementXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.response.DescribeProcessResponse;
import org.n52.javaps.response.DismissResponse;
import org.n52.javaps.response.ExecuteResponse;
import org.n52.javaps.response.GetResultResponse;
import org.n52.javaps.response.GetStatusResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSResponseWriter extends AbstractMultiElementXmlStreamWriter {

    public WPSResponseWriter() {
        super(DescribeProcessResponse.class,
              DismissResponse.class,
              ExecuteResponse.class,
              GetResultResponse.class,
              GetStatusResponse.class,
              GetCapabilitiesResponse.class);
    }

    @Override
    public void writeElement(Object object)
            throws XMLStreamException {
        if (object instanceof DescribeProcessResponse) {
            writeDescribeProcessResponse((DescribeProcessResponse) object);
        } else if (object instanceof DismissResponse) {
            writeDismissResponse((DismissResponse) object);
        } else if (object instanceof ExecuteResponse) {
            writeExecuteResponse((ExecuteResponse) object);
        } else if (object instanceof GetResultResponse) {
            writeGetResultResponse((GetResultResponse) object);
        } else if (object instanceof GetStatusResponse) {
            writeGetStatusResponse((GetStatusResponse) object);
        } else if (object instanceof GetCapabilitiesResponse) {
            writeGetCapabilitiesResponse((GetCapabilitiesResponse) object);
        } else {
            throw unsupported(object);
        }
    }

    private void writeDescribeProcessResponse(DescribeProcessResponse response)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS_OFFERINGS, () -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);
            for (ProcessOffering description : response.getProcessOffering()) {
                delegate(description);
            }
        });
    }

    private void writeDismissResponse(DismissResponse response)
            throws XMLStreamException {
        delegate(response.getStatus());
    }

    private void writeExecuteResponse(ExecuteResponse response)
            throws XMLStreamException {
        if (response.getResult().isPresent()) {
            delegate(response.getResult().get());
        } else if (response.getStatus().isPresent()) {
            delegate(response.getStatus().get());
        } else {
            throw new AssertionError();
        }
    }

    private void writeGetResultResponse(GetResultResponse response)
            throws XMLStreamException {
        delegate(response.getResult());
    }

    private void writeGetStatusResponse(GetStatusResponse response)
            throws XMLStreamException {
        delegate(response.getStatus());
    }

    private void writeGetCapabilitiesResponse(GetCapabilitiesResponse response)
            throws XMLStreamException {
        if (response.getXmlString() != null) {
            String xml = response.getXmlString();
            try (StringReader reader = new StringReader(xml)) {
                write(reader);
            }
        } else {
            delegate(response.getCapabilities());
        }
    }

}
