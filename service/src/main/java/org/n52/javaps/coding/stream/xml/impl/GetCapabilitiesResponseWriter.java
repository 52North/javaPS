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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;

public class GetCapabilitiesResponseWriter extends AbstractXmlStreamWriter<GetCapabilitiesResponse>{

    @Override
    public Set<StreamWriterKey> getKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void write(GetCapabilitiesResponse object) throws XMLStreamException {
        // TODO Auto-generated method stub

    }

}
