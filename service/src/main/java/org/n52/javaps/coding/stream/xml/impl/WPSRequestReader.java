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

import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.javaps.coding.stream.xml.XMLFactories;
import org.n52.javaps.coding.stream.xml.XmlElementStreamReader;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.ogc.wps.DataTransmissionMode;
import org.n52.javaps.ogc.wps.ExecutionMode;
import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.ogc.wps.OutputDefinition;
import org.n52.javaps.ogc.wps.ResponseMode;
import org.n52.javaps.ogc.wps.data.Body;
import org.n52.javaps.ogc.wps.data.Data;
import org.n52.javaps.ogc.wps.data.GroupData;
import org.n52.javaps.ogc.wps.data.ReferenceData;
import org.n52.javaps.ogc.wps.data.StringValueData;
import org.n52.javaps.ogc.wps.data.ValueData;
import org.n52.javaps.request.DescribeProcessRequest;
import org.n52.javaps.request.DismissRequest;
import org.n52.javaps.request.ExecuteRequest;
import org.n52.javaps.request.GetResultRequest;
import org.n52.javaps.request.GetStatusRequest;

import com.google.common.base.Strings;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSRequestReader extends XMLFactories implements XmlElementStreamReader {
    private static final Logger LOG = LoggerFactory.getLogger(WPSRequestReader.class);

    @Override
    public Object read(XMLEventReader reader)
            throws XMLStreamException {

        AbstractServiceRequest<?> request = null;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartDocument()) {
                request = readDocumentElement(reader);
            } else if (event.isEndDocument()) {
                return request;
            }
        }
        throw eof();
    }

    private AbstractServiceRequest<?> readDocumentElement(XMLEventReader reader)
            throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_GET_STATUS)) {
                    return readGetStatusRequest(start, reader);
                } else if (start.getName().equals(WPS.Elem.QN_DISMISS)) {
                    return readDismissRequest(start, reader);
                } else if (start.getName().equals(WPS.Elem.QN_DESCRIBE_PROCESS)) {
                    return readDescribeProcessRequest(start, reader);
                } else if (start.getName().equals(WPS.Elem.QN_EXECUTE)) {
                    return readExecuteRequest(start, reader);
                } else if (start.getName().equals(WPS.Elem.QN_GET_CAPABILITIES)) {
                    return readGetCapabilitiesRequest(start, reader);
                } else if (start.getName().equals(WPS.Elem.QN_GET_RESULT)) {
                    return readGetResultRequest(start, reader);
                } else {
                    throw unexpectedTag(start);
                }
            }
        }
        throw eof();
    }

    private GetStatusRequest readGetStatusRequest(StartElement elem,
                                                  XMLEventReader reader)
            throws XMLStreamException {
        GetStatusRequest request = new GetStatusRequest();

        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_JOB_ID)) {
                    request.setJobId(readJobId(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return request;
            }
        }
        throw eof();
    }

    private JobId readJobId(StartElement elem, XMLEventReader reader)
            throws XMLStreamException {
        LOG.info("readJobId()");
        return new JobId(reader.getElementText());
    }

    private void readServiceAndVersion(StartElement elem,
                                       AbstractServiceRequest<?> request) {
        getAttribute(elem, WPS.Attr.AN_SERVICE).ifPresent(request::setService);
        getAttribute(elem, WPS.Attr.AN_VERSION).ifPresent(request::setVersion);
    }

    private DismissRequest readDismissRequest(StartElement elem,
                                              XMLEventReader reader)
            throws XMLStreamException {
        DismissRequest request = new DismissRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_JOB_ID)) {
                    request.setJobId(readJobId(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return request;
            }
        }
        throw eof();
    }

    private DescribeProcessRequest readDescribeProcessRequest(StartElement elem,
                                                              XMLEventReader reader)
            throws XMLStreamException {
        DescribeProcessRequest request = new DescribeProcessRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_IDENTIFIER)) {
                    request.addProcessIdentifier(readIdentifier(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return request;
            }
        }
        throw eof();
    }

    private GetResultRequest readGetResultRequest(StartElement elem,
                                                  XMLEventReader reader)
            throws XMLStreamException {
        GetResultRequest request = new GetResultRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_JOB_ID)) {
                    request.setJobId(readJobId(start, reader));
                } else {
                    throw unexpectedTag(start);
                }

            } else if (event.isEndElement()) {
                return request;
            }
        }
        throw eof();
    }

    private ExecuteRequest readExecuteRequest(StartElement elem,
                                              XMLEventReader reader)
            throws XMLStreamException {
        ExecuteRequest request = new ExecuteRequest();
        readServiceAndVersion(elem, request);

        getAttribute(elem, WPS.Attr.AN_MODE).flatMap(ExecutionMode::fromString)
                .ifPresent(request::setExecutionMode);
        getAttribute(elem, WPS.Attr.AN_RESPONSE)
                .flatMap(ResponseMode::fromString)
                .ifPresent(request::setResponseMode);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_IDENTIFIER)) {
                    request.setId(readIdentifier(start, reader));
                } else if (start.getName().equals(WPS.Elem.QN_INPUT)) {
                    request.addInput(readInput(start, reader));
                } else if (start.getName().equals(WPS.Elem.QN_OUTPUT)) {
                    request.addOutput(readOutput(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return request;
            }
        }
        throw eof();
    }

    private GetCapabilitiesRequest readGetCapabilitiesRequest(StartElement elem,
                                                              XMLEventReader reader)
            throws XMLStreamException {
        String service = getAttribute(elem, WPS.Attr.AN_SERVICE).orElse(null);
        GetCapabilitiesRequest request = new GetCapabilitiesRequest(service);
        getAttribute(elem, OWS.Attr.AN_UPDATE_SEQUENCE)
                .ifPresent(request::setUpdateSequence);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_ACCEPT_VERSIONS)) {
                    request.setAcceptVersions(readAcceptVersions(start, reader));
                } else if (start.getName().equals(OWS.Elem.QN_SECTIONS)) {
                    request.setSections(readSections(start, reader));
                } else if (start.getName().equals(OWS.Elem.QN_ACCEPT_FORMATS)) {
                    request.setAcceptFormats(readAcceptFormats(start, reader));
                } else if (start.getName().equals(OWS.Elem.QN_ACCEPT_LANGUAGES)) {
                    request.setAcceptLanguages(readAcceptLanguages(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return request;
            }
        }

        throw eof();
    }

    private OwsCode readIdentifier(StartElement elem, XMLEventReader reader)
            throws XMLStreamException {
        return readOwsCode(elem, reader);
    }

    private OwsCode readOwsCode(StartElement elem, XMLEventReader reader)
            throws XMLStreamException {
        URI codeSpace = getAttribute(elem, OWS.Attr.AN_CODE_SPACE)
                .map(URI::create).orElse(null);
        return new OwsCode(reader.getElementText(), codeSpace);
    }

    private List<String> readAcceptVersions(StartElement elem,
                                            XMLEventReader reader)
            throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_VERSION)) {
                    list.add(reader.getElementText());
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return list;
            }
        }
        throw eof();
    }

    private List<String> readSections(StartElement elem, XMLEventReader reader)
            throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_SECTION)) {
                    list.add(reader.getElementText());
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return list;
            }
        }
        throw eof();
    }

    private List<String> readAcceptFormats(StartElement elem,
                                           XMLEventReader reader)
            throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_OUTPUT_FORMAT)) {
                    list.add(reader.getElementText());
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return list;
            }
        }
        throw eof();
    }

    private List<String> readAcceptLanguages(StartElement elem,
                                             XMLEventReader reader)
            throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWS.Elem.QN_LANGUAGE)) {
                    list.add(reader.getElementText());
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return list;
            }
        }
        throw eof();
    }

    private OutputDefinition readOutput(StartElement elem, XMLEventReader reader)
            throws XMLStreamException {
        OutputDefinition outputDefinition = new OutputDefinition();
        Optional<String> attribute
                = getAttribute(elem, WPS.Attr.AN_TRANSMISSION);
        Optional<DataTransmissionMode> flatMap
                = attribute
                        .flatMap(DataTransmissionMode::fromString);
        flatMap
                .ifPresent(outputDefinition::setDataTransmissionMode);
        getAttribute(elem, WPS.Attr.AN_ID).map(OwsCode::new)
                .ifPresent(outputDefinition::setId);
        outputDefinition.setFormat(readFormat(elem));
        List<OutputDefinition> outputs = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_OUTPUT)) {
                    outputs.add(readOutput(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                outputDefinition.setOutputs(outputs);
                return outputDefinition;
            }
        }
        throw eof();
    }

    private Format readFormat(StartElement elem) {
        String mimeType = getAttribute(elem, WPS.Attr.AN_MIME_TYPE).orElse(null);
        String encoding = getAttribute(elem, WPS.Attr.AN_ENCODING).orElse(null);
        String schema = getAttribute(elem, WPS.Attr.AN_SCHEMA).orElse(null);
        return new Format(mimeType, encoding, schema);
    }

    private Data readInput(StartElement elem, XMLEventReader reader) throws XMLStreamException {

        OwsCode id = getAttribute(elem, WPS.Attr.AN_ID).map(OwsCode::new).orElse(null);
        List<Data> inputs = new LinkedList<>();
        Data data = null;
        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_DATA)) {
                    data = readData(start, reader, id);
                } else if (start.getName().equals(WPS.Elem.QN_REFERENCE)) {
                    data = readReference(start, reader, id);
                } else if (start.getName().equals(WPS.Elem.QN_INPUT)) {
                    inputs.add(readInput(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                if (data == null) {
                    if (inputs.isEmpty()) {
                        throw new IllegalStateException();
                    }
                    data = new GroupData(id, inputs);
                }
                return data;

            }
        }
        throw eof();
    }

    private ValueData readData(StartElement start, XMLEventReader reader,
                               OwsCode id) throws XMLStreamException {
        Format format = readFormat(start);
        String string = asString(start, reader);
        return new StringValueData(id, format, string);
    }

    private ReferenceData readReference(StartElement elem, XMLEventReader reader,
                               OwsCode id) throws XMLStreamException {
        ReferenceData data = new ReferenceData(id);
        data.setFormat(readFormat(elem));
        data.setURI(getAttribute(elem, XLink.Attr.QN_HREF).map(URI::create).orElse(null));

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPS.Elem.QN_BODY)) {
                    data.setBody(parseBody(start, reader));
                } else if (start.getName().equals(WPS.Elem.QN_BODY_REFERENCE)) {
                    data.setBody(parseBodyReference(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                return data;
            }
        }

        throw eof();
    }

    private Body parseBodyReference(StartElement elem, XMLEventReader reader) throws XMLStreamException {
        URI href = getAttribute(elem, XLink.Attr.QN_HREF).map(URI::create).orElse(null);

        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                throw unexpectedTag(event.asStartElement());
            } else if (event.isEndElement()) {
                return Body.reference(href);
            }
        }

        throw eof();
    }

    private Body parseBody(StartElement start, XMLEventReader reader) throws XMLStreamException {
        return Body.inline(asString(start, reader));
    }

    private byte[] asBytes(StartElement start, XMLEventReader reader) throws XMLStreamException {
        return asString(start, reader).getBytes(StandardCharsets.UTF_8);
    }

    private String asString(StartElement start, XMLEventReader reader) throws XMLStreamException {
        StringWriter stringWriter = new StringWriter();
        XMLEventWriter writer = outputFactory().createXMLEventWriter(stringWriter);

        // writer.add(eventFactory().createStartDocument());
        copy(reader, writer);
        // writer.add(eventFactory().createEndDocument());

        writer.close();
        return stringWriter.toString();
    }


    /**
     * Copies the content of current node of {@code reader} to {@code writer}.
     * Assumes that the current event is a START_ELEMENT and will proceed until
     * the corresponding END_ELEMENT is consumed.
     *
     * @param reader the reader
     * @param writer the writer
     *
     * @throws XMLStreamException
     */
    private void copy(XMLEventReader reader, XMLEventWriter writer)
            throws XMLStreamException {
        int depth = 0;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement elem = event.asStartElement();
                QName elementName = elem.getName();
                String elementPrefix = elementName.getPrefix();
                String elementNamespace = elementName.getNamespaceURI();

                // check if the current element's namespace is declared
                // this has to be done before the START_ELEMENT event is emitted
                // as this would put the namespace into the writer's context
                boolean writeElementNamespace =
                        !elementPrefix.isEmpty() && !elementPrefix.equals("xml") &&
                        Strings.isNullOrEmpty(writer.getNamespaceContext().getNamespaceURI(elementPrefix));

                // emit the element without any attributes or namespaces
                writer.add(eventFactory().createStartElement(elementName, null, null));

                // iterate over all namespace declaration to check if the element namespace
                // is declared
                @SuppressWarnings("unchecked")
                Iterator<Namespace> namespaces = elem.getNamespaces();
                while (namespaces.hasNext()) {
                    Namespace namespace = namespaces.next();
                    // checks if the namespace declaration matches the current element
                    if (elementPrefix.equals(namespace.getPrefix()) &&
                        elementNamespace.equals(namespace.getNamespaceURI())) {
                        writeElementNamespace = false;
                    }
                    // declare the namespace
                    writer.add(eventFactory()
                            .createNamespace(Strings.nullToEmpty(namespace.getPrefix()),
                                             namespace.getNamespaceURI()));
                }

                // if the there is no namespace declaration for the current element, create one
                if (writeElementNamespace) {
                    writer.add(eventFactory()
                            .createNamespace(Strings.nullToEmpty(elementPrefix),
                                             elementNamespace));
                }

                // iterate over the attributes and check if there namespace is
                // declared, prior to emitting to ATTRIBUTE event
                @SuppressWarnings("unchecked")
                Iterator<Attribute> attributes = elem.getAttributes();
                while (attributes.hasNext()) {
                    Attribute attribute = attributes.next();
                    String attributePrefix = attribute.getName().getPrefix();
                    String attributeNamespace = attribute.getName().getNamespaceURI();

                    if (!attributePrefix.isEmpty() && !attributePrefix.equals("xml") &&
                        Strings.isNullOrEmpty(writer.getNamespaceContext().getNamespaceURI(attributePrefix))) {
                        writer.add(eventFactory()
                                .createNamespace(attributePrefix,
                                                 attributeNamespace));
                    }
                    writer.add(eventFactory()
                            .createAttribute(attribute.getName(),
                                             attribute.getValue()));
                }
                ++depth;
            } else if (event.isEndElement()) {
                --depth;
                if (depth >= 0) {
                    writer.add(event);
                } else {
                    return; // we hit last closing tag
                }
            } else {
                writer.add(event);
            }

        }
        throw eof();
    }

    protected static Optional<String> getAttribute(StartElement event, QName name) {
        Attribute attr = event.getAttributeByName(name);
        return Optional.ofNullable(attr).map(Attribute::getValue);
    }

    protected static Optional<String> getAttribute(StartElement event, String name) {
        return getAttribute(event, new QName(name));
    }

    protected static XMLStreamException unexpectedTag(StartElement element) {
        String message = String.format("unexpected tag: %s", element.getName());
        return new XMLStreamException(message, element.getLocation());
    }

    protected static XMLStreamException eof() {
        return new XMLStreamException("premature end of file");
    }
}
