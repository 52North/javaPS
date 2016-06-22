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
package org.n52.javaps.coding.stream.xml.impl.response;

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.response.DescribeProcessResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessResponseWriter extends AbstractXmlElementStreamWriter<DescribeProcessResponse> {

    public DescribeProcessResponseWriter() {
        super(DescribeProcessResponse.class);
    }

    @Override
    public void write(DescribeProcessResponse object)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS_OFFERINGS, object, x -> {
            namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
            namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
            namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);
            for (ProcessOffering description : x.getProcessOffering()) {
                delegate(description);
            }
        });
    }
}
