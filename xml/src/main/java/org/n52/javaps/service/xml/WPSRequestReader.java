/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.ExecutionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;
import org.n52.shetland.ogc.wps.request.DescribeProcessRequest;
import org.n52.shetland.ogc.wps.request.DismissRequest;
import org.n52.shetland.ogc.wps.request.ExecuteRequest;
import org.n52.shetland.ogc.wps.request.GetResultRequest;
import org.n52.shetland.ogc.wps.request.GetStatusRequest;
import org.n52.svalbard.decode.stream.StreamReaderKey;
import org.n52.svalbard.decode.stream.xml.AbstractElementXmlStreamReader;
import org.n52.svalbard.decode.stream.xml.XmlStreamReaderKey;
import org.n52.svalbard.stream.XLinkConstants;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSRequestReader extends AbstractElementXmlStreamReader {

    private static final HashSet<XmlStreamReaderKey> KEYS = new HashSet<>(Arrays.asList(new XmlStreamReaderKey(
            WPSConstants.Elem.QN_GET_STATUS), new XmlStreamReaderKey(WPSConstants.Elem.QN_DISMISS),
            new XmlStreamReaderKey(WPSConstants.Elem.QN_DESCRIBE_PROCESS), new XmlStreamReaderKey(
                    WPSConstants.Elem.QN_EXECUTE), new XmlStreamReaderKey(WPSConstants.Elem.QN_GET_CAPABILITIES),
            new XmlStreamReaderKey(WPSConstants.Elem.QN_GET_RESULT)));

    @Override
    public Set<StreamReaderKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public OwsServiceRequest readElement(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_GET_STATUS)) {
                    return readGetStatusRequest(start, reader);
                } else if (start.getName().equals(WPSConstants.Elem.QN_DISMISS)) {
                    return readDismissRequest(start, reader);
                } else if (start.getName().equals(WPSConstants.Elem.QN_DESCRIBE_PROCESS)) {
                    return readDescribeProcessRequest(start, reader);
                } else if (start.getName().equals(WPSConstants.Elem.QN_EXECUTE)) {
                    return readExecuteRequest(start, reader);
                } else if (start.getName().equals(WPSConstants.Elem.QN_GET_CAPABILITIES)) {
                    return readGetCapabilitiesRequest(start, reader);
                } else if (start.getName().equals(WPSConstants.Elem.QN_GET_RESULT)) {
                    return readGetResultRequest(start, reader);
                } else {
                    throw unexpectedTag(start);
                }
            }
        }
        throw eof();
    }

    private GetStatusRequest readGetStatusRequest(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        GetStatusRequest request = new GetStatusRequest();

        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_JOB_ID)) {
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

    private JobId readJobId(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        return new JobId(reader.getElementText());
    }

    private void readServiceAndVersion(StartElement elem,
            OwsServiceRequest request) {
        getAttribute(elem, WPSConstants.Attr.AN_SERVICE).ifPresent(request::setService);
        getAttribute(elem, WPSConstants.Attr.AN_VERSION).ifPresent(request::setVersion);
    }

    private DismissRequest readDismissRequest(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        DismissRequest request = new DismissRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_JOB_ID)) {
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
            XMLEventReader reader) throws XMLStreamException {
        DescribeProcessRequest request = new DescribeProcessRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_IDENTIFIER)) {
                    OwsCode identifier = null;
                    try {
                        identifier = readIdentifier(start, reader);
                    } catch (Exception e) {
                        identifier = null;
                    }
                    if (identifier != null) {
                        request.addProcessIdentifier(identifier);
                    }
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
            XMLEventReader reader) throws XMLStreamException {
        GetResultRequest request = new GetResultRequest();
        readServiceAndVersion(elem, request);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_JOB_ID)) {
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
            XMLEventReader reader) throws XMLStreamException {
        ExecuteRequest request = new ExecuteRequest();
        readServiceAndVersion(elem, request);

        getAttribute(elem, WPSConstants.Attr.AN_MODE).flatMap(ExecutionMode::fromString).ifPresent(
                request::setExecutionMode);
        getAttribute(elem, WPSConstants.Attr.AN_RESPONSE).flatMap(ResponseMode::fromString).ifPresent(
                request::setResponseMode);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_IDENTIFIER)) {
                    request.setId(readIdentifier(start, reader));
                } else if (start.getName().equals(WPSConstants.Elem.QN_INPUT)) {
                    request.addInput(readInput(start, reader));
                } else if (start.getName().equals(WPSConstants.Elem.QN_OUTPUT)) {
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
            XMLEventReader reader) throws XMLStreamException {
        String service = getAttribute(elem, WPSConstants.Attr.AN_SERVICE).orElse(null);
        GetCapabilitiesRequest request = new GetCapabilitiesRequest(service);
        getAttribute(elem, OWSConstants.Attr.AN_UPDATE_SEQUENCE).ifPresent(request::setUpdateSequence);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_ACCEPT_VERSIONS)) {
                    request.setAcceptVersions(readAcceptVersions(start, reader));
                } else if (start.getName().equals(OWSConstants.Elem.QN_SECTIONS)) {
                    request.setSections(readSections(start, reader));
                } else if (start.getName().equals(OWSConstants.Elem.QN_ACCEPT_FORMATS)) {
                    request.setAcceptFormats(readAcceptFormats(start, reader));
                } else if (start.getName().equals(OWSConstants.Elem.QN_ACCEPT_LANGUAGES)) {
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

    private OwsCode readIdentifier(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        return readOwsCode(elem, reader);
    }

    private OwsCode readOwsCode(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        URI codeSpace = getAttribute(elem, OWSConstants.Attr.AN_CODE_SPACE).map(URI::create).orElse(null);
        return new OwsCode(reader.getElementText(), codeSpace);
    }

    private List<String> readAcceptVersions(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_VERSION)) {
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

    private List<String> readSections(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_SECTION)) {
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
            XMLEventReader reader) throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_OUTPUT_FORMAT)) {
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
            XMLEventReader reader) throws XMLStreamException {
        List<String> list = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(OWSConstants.Elem.QN_LANGUAGE)) {
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

    private OutputDefinition readOutput(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        OutputDefinition outputDefinition = new OutputDefinition();
        Optional<String> attribute = getAttribute(elem, WPSConstants.Attr.AN_TRANSMISSION);
        Optional<DataTransmissionMode> flatMap = attribute.flatMap(DataTransmissionMode::fromString);
        flatMap.ifPresent(outputDefinition::setDataTransmissionMode);
        getAttribute(elem, WPSConstants.Attr.AN_ID).map(OwsCode::new).ifPresent(outputDefinition::setId);
        outputDefinition.setFormat(readFormat(elem));
        List<OutputDefinition> outputs = new LinkedList<>();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_OUTPUT)) {
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
        String mimeType = getAttribute(elem, WPSConstants.Attr.AN_MIME_TYPE).orElse(null);
        String encoding = getAttribute(elem, WPSConstants.Attr.AN_ENCODING).orElse(null);
        String schema = getAttribute(elem, WPSConstants.Attr.AN_SCHEMA).orElse(null);
        return new Format(mimeType, encoding, schema);
    }

    private ProcessData readInput(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {

        OwsCode id = getAttribute(elem, WPSConstants.Attr.AN_ID).map(OwsCode::new).orElse(null);
        List<ProcessData> inputs = new LinkedList<>();
        ProcessData data = null;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_DATA)) {
                    data = readData(start, reader, id);
                } else if (start.getName().equals(WPSConstants.Elem.QN_REFERENCE)) {
                    data = readReference(start, reader, id);
                } else if (start.getName().equals(WPSConstants.Elem.QN_INPUT)) {
                    inputs.add(readInput(start, reader));
                } else {
                    throw unexpectedTag(start);
                }
            } else if (event.isEndElement()) {
                if (data == null) {
                    if (inputs.isEmpty()) {
                        throw new IllegalStateException();
                    }
                    data = new GroupProcessData(id, inputs);
                }
                return data;

            }
        }
        throw eof();
    }

    private ReferenceProcessData readReference(StartElement elem,
            XMLEventReader reader,
            OwsCode id) throws XMLStreamException {
        ReferenceProcessData data = new ReferenceProcessData(id);
        data.setFormat(readFormat(elem));
        data.setURI(getAttribute(elem, XLinkConstants.Attr.QN_HREF).map(URI::create).orElse(null));

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (start.getName().equals(WPSConstants.Elem.QN_BODY)) {
                    data.setBody(parseBody(start, reader));
                } else if (start.getName().equals(WPSConstants.Elem.QN_BODY_REFERENCE)) {
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

    private Body parseBodyReference(StartElement elem,
            XMLEventReader reader) throws XMLStreamException {
        URI href = getAttribute(elem, XLinkConstants.Attr.QN_HREF).map(URI::create).orElse(null);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                throw unexpectedTag(event.asStartElement());
            } else if (event.isEndElement()) {
                return Body.reference(href);
            }
        }

        throw eof();
    }

    private Body parseBody(StartElement start,
            XMLEventReader reader) throws XMLStreamException {
        return Body.inline(asString(start, reader));
    }

    private ValueProcessData readData(StartElement start,
            XMLEventReader reader,
            OwsCode id) throws XMLStreamException {
        Format format = readFormat(start);
        // TODO persist the inputs to disk?
        String string = asString(start, reader);
        return new StringValueProcessData(id, format, string);
    }
}
