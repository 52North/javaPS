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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.n52.iceland.ogc.ows.OwsCRS;
import org.n52.iceland.ogc.ows.OwsPossibleValues;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.iceland.ogc.wps.DataTransmissionMode;
import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.JobControlOption;
import org.n52.iceland.ogc.wps.JobId;
import org.n52.iceland.ogc.wps.ProcessOffering;
import org.n52.iceland.ogc.wps.ProcessOfferings;
import org.n52.iceland.ogc.wps.Result;
import org.n52.iceland.ogc.wps.StatusInfo;
import org.n52.iceland.ogc.wps.WPSCapabilities;
import org.n52.iceland.ogc.wps.data.Body;
import org.n52.iceland.ogc.wps.data.GroupProcessData;
import org.n52.iceland.ogc.wps.data.ProcessData;
import org.n52.iceland.ogc.wps.data.ReferenceProcessData;
import org.n52.iceland.ogc.wps.data.ValueProcessData;
import org.n52.iceland.ogc.wps.description.BoundingBoxDescription;
import org.n52.iceland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.iceland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.iceland.ogc.wps.description.ComplexDescription;
import org.n52.iceland.ogc.wps.description.ComplexInputDescription;
import org.n52.iceland.ogc.wps.description.ComplexOutputDescription;
import org.n52.iceland.ogc.wps.description.Description;
import org.n52.iceland.ogc.wps.description.GroupInputDescription;
import org.n52.iceland.ogc.wps.description.GroupOutputDescription;
import org.n52.iceland.ogc.wps.description.LiteralDataDomain;
import org.n52.iceland.ogc.wps.description.LiteralDescription;
import org.n52.iceland.ogc.wps.description.LiteralInputDescription;
import org.n52.iceland.ogc.wps.description.LiteralOutputDescription;
import org.n52.iceland.ogc.wps.description.ProcessDescription;
import org.n52.iceland.ogc.wps.description.ProcessInputDescription;
import org.n52.iceland.ogc.wps.description.ProcessInputDescriptionContainer;
import org.n52.iceland.ogc.wps.description.ProcessOutputDescription;
import org.n52.iceland.ogc.wps.description.ProcessOutputDescriptionContainer;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.WPS;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XLink;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.XMLSchema;
import org.n52.javaps.io.EncodingException;

import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSWriter extends AbstractOWSWriter {

    private static final String UNBOUNDED = "unbounded";
    private final ConcreteOutputWriter concreteOutputWriter
            = new ConcreteOutputWriter();
    private final ConcreteInputWriter concreteInputWriter
            = new ConcreteInputWriter();

    public WPSWriter() {
        super(ProcessOfferings.class,
              Result.class,
              StatusInfo.class,
              ProcessDescription.class,
              WPSCapabilities.class,
              ProcessData.class);
    }

    @Override
    public void writeElement(Object object)
            throws XMLStreamException {
        if (object instanceof ProcessOffering) {
            writeProcessOffering((ProcessOffering) object);
        } else if (object instanceof Result) {
            writeResult((Result) object);
        } else if (object instanceof ValueProcessData) {
            writeValueData((ValueProcessData) object);
        } else if (object instanceof StatusInfo) {
            writeStatusInfo((StatusInfo) object);
        } else if (object instanceof ReferenceProcessData) {
            writeReferenceData((ReferenceProcessData) object);
        } else if (object instanceof ProcessDescription) {
            writeProcessDescription((ProcessDescription) object);
        } else if (object instanceof WPSCapabilities) {
            writeWPSCapabilities((WPSCapabilities) object);
        } else if (object instanceof ProcessOfferings) {
            writeProcessOfferings((ProcessOfferings) object);
        } else {
            throw unsupported(object);
        }
    }

    private void writeWPSCapabilities(WPSCapabilities object)
            throws XMLStreamException {
        element(WPS.Elem.QN_CAPABILITIES, object, (WPSCapabilities capabilities) -> {
            writeNamespaces();

            attr(OWS.Attr.AN_VERSION, capabilities.getVersion());
            attr(OWS.Attr.AN_UPDATE_SEQUENCE, capabilities.getUpdateSequence());

            writeServiceIdentification(capabilities);
            writeServiceProvider(capabilities);
            writeOperationsMetadata(capabilities);
            writeLanguages(capabilities);
            writeContents(capabilities);
        });
    }

    private void writeContents(WPSCapabilities capabilities)
            throws XMLStreamException {
        element(WPS.Elem.QN_CONTENTS, capabilities.getProcessOfferings(), (ProcessOfferings offerings) -> {
            forEach(WPS.Elem.QN_PROCESS_SUMMARY, offerings, offering -> {
                attr(WPS.Attr.AN_JOB_CONTROL_OPTIONS, offering
                     .getJobControlOptions(), JobControlOption::getValue);
                attr(WPS.Attr.AN_OUTPUT_TRANSMISSION, offering
                     .getOutputTransmissionModes(), DataTransmissionMode::getValue);
                attr(WPS.Attr.AN_PROCESS_VERSION, offering.getProcessVersion());
                attr(WPS.Attr.AN_PROCESS_MODEL, offering.getProcessModel());

                writeDescriptionElements(offering.getProcessDescription());
            });
        });
    }

    private void writeProcessInputDescription(ProcessInputDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_INPUT, input, x -> {
            attr(XMLSchema.Attr.AN_MIN_OCCURS, x.getOccurence().getMin()
                 .toString());
            attr(XMLSchema.Attr.AN_MAX_OCCURS, x.getOccurence().getMax()
                 .map(Object::toString).orElse(UNBOUNDED));
            writeDescriptionElements(x);
            x.visit(this.concreteInputWriter);
        });
    }

    private void writeProcessOutputDescription(ProcessOutputDescription output)
            throws XMLStreamException {
        element(WPS.Elem.QN_OUTPUT, () -> {
            writeDescriptionElements(output);
            output.visit(this.concreteOutputWriter);
        });
    }

    private void writeDescriptionElements(Description description)
            throws XMLStreamException {
        writeLanguageString(OWS.Elem.QN_TITLE, description.getTitle());
        writeLanguageString(OWS.Elem.QN_ABSTRACT, description.getAbstract());
        writeKeywords(description.getKeywords());
        writeCode(OWS.Elem.QN_IDENTIFIER, description.getId());
        writeMetadata(description.getMetadata());
    }

    private void writeFormat(Format format,
                             Optional<BigInteger> maximumMegabytes,
                             boolean isDefaultFormat)
            throws XMLStreamException {
        element(WPS.Elem.QN_FORMAT, () -> {
            writeDataEncodingAttributes(format);
            attr(WPS.Attr.AN_MAXIMUM_MEGABYTES, maximumMegabytes
                 .map(BigInteger::toString));
            if (isDefaultFormat) {
                attr(WPS.Attr.AN_DEFAULT, "true");
            }
        });
    }

    private void writeLiteralDataDomain(LiteralDataDomain literalDataDomain,
                                        boolean defaultDomain)
            throws XMLStreamException {
        element(WPS.Elem.QN_LITERAL_DATA_DOMAIN, literalDataDomain, ldd -> {
            if (defaultDomain) {
                attr(WPS.Attr.AN_DEFAULT, "true");
            }

            OwsPossibleValues vd = ldd.getPossibleValues();

            if (vd.isAnyValue()) {
                writeAnyValue(vd.asAnyValues());
            } else if (vd.isAllowedValues()) {
                writeAllowedValues(vd.asAllowedValues());
            } else if (vd.isValuesReference()) {
                writeValuesReference(vd.asValuesReference());
            }

            writeDomainMetadata(OWS.Elem.QN_DATA_TYPE, ldd.getDataType());
            writeDomainMetadata(OWS.Elem.QN_UOM, ldd.getUOM());
            element(OWS.Elem.QN_DEFAULT_VALUE, ldd.getDefaultValue().map(OwsValue::getValue));
        });
    }

    private void writeBoundingBoxDescription(BoundingBoxDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_BOUNDING_BOX_DATA, input, x -> {
            element(WPS.Elem.QN_SUPPORTED_CRS, x.getDefaultCRS(), crs -> {
                attr(WPS.Attr.AN_DEFAULT, "true");
                chars(crs.getValue().toString());
            });
            for (OwsCRS crs : x.getSupportedCRS()) {
                element(WPS.Elem.QN_SUPPORTED_CRS, crs.getValue().toString());
            }

        });
    }

    private void writeComplexDescription(ComplexDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_COMPLEX_DATA, input, x -> {
            writeFormat(x.getDefaultFormat(), x.getMaximumMegabytes(), true);
            for (Format format : x.getSupportedFormats()) {
                writeFormat(format, x.getMaximumMegabytes(), false);
            }
        });
    }

    private void writeLiteralDescription(LiteralDescription input)
            throws XMLStreamException {
        element(WPS.Elem.QN_LITERAL_DATA, input, x -> {
            writeLiteralDataDomain(x.getDefaultLiteralDataDomain(), true);
            for (LiteralDataDomain ldd : x.getSupportedLiteralDataDomains()) {
                writeLiteralDataDomain(ldd, false);
            }
        });
    }

    private void writeProcessInputDescriptionContainer(ProcessInputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessInputDescription description : container
                .getInputDescriptions()) {
            writeProcessInputDescription(description);
        }
    }

    private void writeProcessOutputDescriptionContainer(ProcessOutputDescriptionContainer container)
            throws XMLStreamException {
        for (ProcessOutputDescription description : container
                .getOutputDescriptions()) {
            writeProcessOutputDescription(description);
        }
    }

    private void writeProcessDescription(ProcessDescription object)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS, object, x -> {
            writeNamespaces();
            writeDescriptionElements(object);
            writeProcessInputDescriptionContainer((ProcessInputDescriptionContainer) object);
            writeProcessOutputDescriptionContainer((ProcessOutputDescriptionContainer) object);
        });
    }

    private void writeReferenceData(ReferenceProcessData reference)
            throws XMLStreamException {
        element(WPS.Elem.QN_REFERENCE, () -> {
            writeNamespaces();

            attr(XLink.Attr.QN_HREF, reference.getURI().toString());
            writeDataEncodingAttributes(reference.getFormat());
            if (reference.getBody().isPresent()) {
                Body body = reference.getBody().get();
                if (body.isInline()) {
                    element(WPS.Elem.QN_BODY, body.asInline(), b -> cdata(b
                            .getBody()));
                } else if (body.isReferenced()) {
                    element(WPS.Elem.QN_BODY_REFERENCE, body.asReferenced(), b
                            -> attr(XLink.Attr.QN_HREF, b.getHref().toString()));
                } else {
                    throw new AssertionError();
                }
            }
        });
    }

    private void writeStatusInfo(StatusInfo object)
            throws XMLStreamException {
        element(WPS.Elem.QN_STATUS_INFO, () -> {
            writeNamespaces();
            element(WPS.Elem.QN_JOB_ID, object.getJobId().getValue());
            element(WPS.Elem.QN_STATUS, object.getStatus().getValue());
            element(WPS.Elem.QN_EXPIRATION_DATE, object.getExpirationDate()
                    .map(this::format));
            element(WPS.Elem.QN_ESTIMATED_COMPLETION, object
                    .getEstimatedCompletion().map(this::format));
            element(WPS.Elem.QN_NEXT_POLL, object.getNextPoll()
                    .map(this::format));
            element(WPS.Elem.QN_PERCENT_COMPLETED, object.getPercentCompleted()
                    .map(String::valueOf));
        });
    }

    private void writeDataEncodingAttributes(Format format)
            throws XMLStreamException {
        attr(WPS.Attr.AN_MIME_TYPE, format.getMimeType());
        attr(WPS.Attr.AN_ENCODING, format.getEncoding());
        attr(WPS.Attr.AN_SCHEMA, format.getSchema());
    }

    private void writeValueData(ValueProcessData value)
            throws XMLStreamException {
        element(WPS.Elem.QN_DATA, value, x -> {
            writeNamespaces();

            Format format = x.getFormat();
            try (InputStream data = x.getData()) {

                if (!x.getFormat().hasEncoding() || x.getFormat()
                    .isCharacterEncoding()) {

                    writeDataEncodingAttributes(format
                            .withEncoding(documentEncoding()));

                    String encoding = format.getEncoding()
                            .orElse(Format.DEFAULT_ENCODING);
                    Charset charset = Charset.forName(encoding);

                    try (InputStreamReader reader
                            = new InputStreamReader(data, charset)) {
                        if (format.isXML()) {
                            write(reader);
                        } else {
                            cdata(CharStreams.toString(reader));
                        }
                    }

                } else {
                    // format describes some other thing, e.g. binary
                    BaseEncoding base64 = base64();
                    writeDataEncodingAttributes(format.withBase64Encoding());

                    if (format.isBase64()) {
                        // it is already base64 encoded
                        try (Reader reader
                                = new InputStreamReader(data, StandardCharsets.US_ASCII);
                             InputStream in = base64.decodingStream(reader)) {
                            writeBase64(in);
                        }
                    } else {
                        writeBase64(data);
                    }
                }
            } catch (IOException | EncodingException ex) {
                throw new XMLStreamException(ex);
            }
        });
    }

    private void writeResult(Result result)
            throws XMLStreamException {
        element(WPS.Elem.QN_RESULT, result, x -> {
            writeNamespaces();

            element(WPS.Elem.QN_JOB_ID, x.getJobId().map(JobId::getValue));
            element(WPS.Elem.QN_EXPIRATION_DATE, x.getExpirationDate().map(this::format));

            for (ProcessData data : x.getOutputs()) {
                writeOutput(data);
            }
        });

    }

    private void writeOutput(ProcessData data)
            throws XMLStreamException {
        element(WPS.Elem.QN_OUTPUT, data, x -> {
            writeNamespaces();
            attr(WPS.Attr.AN_ID, x.getId().getValue());
            // FIXME codeSpace is not allowed here...
            attr(OWS.Attr.AN_CODE_SPACE, x.getId().getCodeSpace()
                 .map(URI::toString));

            if (x.isGroup()) {
                writeGroupData(x.asGroup());
            } else if (x.isReference()) {
                writeReferenceData(x.asReference());
            } else if (x.isValue()) {
                writeValueData(x.asValue());
            }
        });
    }

    private void writeProcessOffering(ProcessOffering offering)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS_OFFERING, offering, x -> {
            attr(WPS.Attr.AN_JOB_CONTROL_OPTIONS, x.getJobControlOptions(), JobControlOption::getValue);
            attr(WPS.Attr.AN_OUTPUT_TRANSMISSION, x.getOutputTransmissionModes(), DataTransmissionMode::getValue);
            attr(WPS.Attr.AN_PROCESS_VERSION, x.getProcessVersion());
            attr(WPS.Attr.AN_PROCESS_MODEL, x.getProcessModel());
            writeProcessDescription(offering.getProcessDescription());
        });
    }

    private void writeNamespaces()
            throws XMLStreamException {
        namespace(WPS.NS_WPS_PREFIX, WPS.NS_WPS);
        namespace(OWS.NS_OWS_PREFIX, OWS.NS_OWS);
        namespace(XLink.NS_XLINK_PREFIX, XLink.NS_XLINK);
    }

    private void writeGroupData(GroupProcessData x)
            throws XMLStreamException {
        for (ProcessData output : x.getElements()) {
            writeOutput(output);
        }
    }

    private void writeProcessOfferings(ProcessOfferings offerings)
            throws XMLStreamException {
        element(WPS.Elem.QN_PROCESS_OFFERINGS, () -> {
            writeNamespaces();
            for (ProcessOffering processOffering : offerings) {
                writeProcessOffering(processOffering);
            }
        });
    }

    private class ConcreteInputWriter implements
            ProcessInputDescription.ThrowingVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxInputDescription input)
                throws XMLStreamException {
            writeBoundingBoxDescription((BoundingBoxDescription) input);
        }

        @Override
        public void visit(ComplexInputDescription input)
                throws XMLStreamException {
            writeComplexDescription((ComplexDescription) input);
        }

        @Override
        public void visit(LiteralInputDescription input)
                throws XMLStreamException {
            writeLiteralDescription((LiteralDescription) input);
        }

        @Override
        public void visit(GroupInputDescription input)
                throws XMLStreamException {
            writeProcessInputDescriptionContainer((ProcessInputDescriptionContainer) input);
        }
    }

    private class ConcreteOutputWriter
            implements ProcessOutputDescription.ThrowingVisitor<XMLStreamException> {
        @Override
        public void visit(BoundingBoxOutputDescription output)
                throws XMLStreamException {
            writeBoundingBoxDescription((BoundingBoxDescription) output);
        }

        @Override
        public void visit(ComplexOutputDescription output)
                throws XMLStreamException {
            writeComplexDescription((ComplexDescription) output);
        }

        @Override
        public void visit(LiteralOutputDescription output)
                throws XMLStreamException {
            writeLiteralDescription((LiteralDescription) output);
        }

        @Override
        public void visit(GroupOutputDescription output)
                throws XMLStreamException {
            writeProcessOutputDescriptionContainer((ProcessOutputDescriptionContainer) output);
        }
    }

}
