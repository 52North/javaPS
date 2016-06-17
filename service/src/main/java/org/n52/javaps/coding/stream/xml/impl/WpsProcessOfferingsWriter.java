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

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Namespaces;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.QNames;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.response.DescribeProcessResponse;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WpsProcessOfferingsWriter extends AbstractXmlElementStreamWriter<DescribeProcessResponse> {

    private static final XmlStreamWriterKey KEY = new XmlStreamWriterKey(DescribeProcessResponse.class);

    @Override
    public void write(DescribeProcessResponse object) throws XMLStreamException {
        start(QNames.WPS_PROCESS_OFFERINGS);
        namespace(Namespaces.WPS_PREFIX, Namespaces.WPS_20);
        namespace(Namespaces.OWS_PREFIX, Namespaces.OWS_20);

        for (ProcessOffering description : object.getProcessOffering()) {
            delegate(description);
        }

        end(QNames.WPS_PROCESS_OFFERINGS);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }
}
