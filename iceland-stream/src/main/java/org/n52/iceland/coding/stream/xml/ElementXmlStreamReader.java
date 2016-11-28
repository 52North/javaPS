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
package org.n52.iceland.coding.stream.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.coding.stream.StreamReaderKey;
import org.n52.janmayen.component.Component;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ElementXmlStreamReader extends Component<StreamReaderKey> {

    Object readElement(XMLEventReader reader)
            throws XMLStreamException;
}
