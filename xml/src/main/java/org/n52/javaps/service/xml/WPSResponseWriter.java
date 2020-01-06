/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.xml;

import javax.xml.stream.XMLStreamException;

import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.shetland.ogc.wps.response.DescribeProcessResponse;
import org.n52.shetland.ogc.wps.response.DismissResponse;
import org.n52.shetland.ogc.wps.response.ExecuteResponse;
import org.n52.shetland.ogc.wps.response.GetResultResponse;
import org.n52.shetland.ogc.wps.response.GetStatusResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.stream.xml.AbstractMultiElementXmlStreamWriter;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSResponseWriter extends AbstractMultiElementXmlStreamWriter {

    public WPSResponseWriter() {
        super(DescribeProcessResponse.class, DismissResponse.class, ExecuteResponse.class, GetResultResponse.class,
                GetStatusResponse.class, GetCapabilitiesResponse.class);
    }

    @Override
    public void writeElement(Object object) throws XMLStreamException, EncodingException {
        if (object instanceof DescribeProcessResponse) {
            DescribeProcessResponse response = (DescribeProcessResponse) object;
            delegate(response.getProcessOfferings());
        } else if (object instanceof DismissResponse) {
            DismissResponse response = (DismissResponse) object;
            delegate(response.getStatus());
        } else if (object instanceof ExecuteResponse) {
            ExecuteResponse response = (ExecuteResponse) object;
            if (response.getResult().isPresent()) {
                delegate(response.getResult().get());
            } else if (response.getStatus().isPresent()) {
                delegate(response.getStatus().get());
            } else {
                throw new AssertionError();
            }
        } else if (object instanceof GetResultResponse) {
            GetResultResponse response = (GetResultResponse) object;
            delegate(response.getResult());
        } else if (object instanceof GetStatusResponse) {
            GetStatusResponse response = (GetStatusResponse) object;
            delegate(response.getStatus());
        } else if (object instanceof GetCapabilitiesResponse) {
            GetCapabilitiesResponse response = (GetCapabilitiesResponse) object;
            if (response.getXmlString() == null) {
                delegate(response.getCapabilities());
            } else {
                writeXML(response.getXmlString());
            }
        } else {
            throw unsupported(object);
        }
    }
}
