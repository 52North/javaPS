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

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.response.DescribeProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCapabilitiesResponseWriter extends AbstractXmlStreamWriter<GetCapabilitiesResponse>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesResponseEncoder.class);

    private static final String NS_OWS_20 = "http://www.opengis.net/ows/2.0";

    private static final String NS_WPS_20 = "http://www.opengis.net/wps/2.0";

    private static final String EN_GET_CAPABILITIES = "GetCapabilities";

    private static final String EN_PROCESS_OFFERING = "ProcessOffering";

    private static final String NS_OWS_PREFIX = "ows";

    private static final String NS_WPS_PREFIX = "wps";

    private static final QName QN_GET_CAPABILITIES = new QName(NS_WPS_20, EN_GET_CAPABILITIES, NS_WPS_PREFIX);

    public static final StreamWriterKey KEY = new XmlStreamWriterKey(GetCapabilitiesResponse.class);

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected void write(GetCapabilitiesResponse object) throws XMLStreamException {

        start(QN_GET_CAPABILITIES);
        namespace(NS_OWS_PREFIX, NS_OWS_20);
        namespace(NS_WPS_PREFIX, NS_WPS_20);
        end(QN_GET_CAPABILITIES);
    }

}
