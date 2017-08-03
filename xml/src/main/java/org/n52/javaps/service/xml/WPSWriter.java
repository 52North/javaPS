/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.codec.binary.Base64InputStream;

import org.n52.svalbard.stream.XLinkConstants;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobControlOption;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ProcessOfferings;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.WPSCapabilities;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.ValueProcessData;
import org.n52.shetland.ogc.wps.description.BoundingBoxDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescriptionContainer;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescriptionContainer;
import org.n52.javaps.io.bbox.BoundingBoxInputOutputHandler;
import org.n52.javaps.io.literal.LiteralInputOutputHandler;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.svalbard.encode.exception.EncodingException;

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
            throws XMLStreamException, EncodingException {
        if (object instanceof ProcessOffering) {
            writeProcessOffering((ProcessOffering) object);
        } else if (object instanceof Result) {
            writeResult((Result) object);
        } else if (object instanceof StatusInfo) {
            writeStatusInfo((StatusInfo) object);
        } else if (object instanceof ProcessData) {
            writeOutput((ProcessData) object);
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
        element(WPSConstants.Elem.QN_CAPABILITIES, object, (WPSCapabilities capabilities) -> {
            writeNamespaces();

            attr(OWSConstants.Attr.AN_SERVICE, capabilities.getService());
            attr(OWSConstants.Attr.AN_VERSION, capabilities.getVersion());
            attr(OWSConstants.Attr.AN_UPDATE_SEQUENCE, capabilities.getUpdateSequence());

            writeServiceIdentification(capabilities);
            writeServiceProvider(capabilities);
            writeOperationsMetadata(capabilities);
            writeLanguages(capabilities);
            writeContents(capabilities);
        });
    }

    private void writeContents(WPSCapabilities capabilities)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_CONTENTS, capabilities.getProcessOfferings(), (ProcessOfferings offerings) -> {
            forEach(WPSConstants.Elem.QN_PROCESS_SUMMARY, offerings, offering -> {
                attr(WPSConstants.Attr.AN_JOB_CONTROL_OPTIONS, offering
                     .getJobControlOptions(), JobControlOption::getValue);
                attr(WPSConstants.Attr.AN_OUTPUT_TRANSMISSION, offering
                     .getOutputTransmissionModes(), DataTransmissionMode::getValue);
                attr(WPSConstants.Attr.AN_PROCESS_VERSION, offering.getProcessVersion());
                attr(WPSConstants.Attr.AN_PROCESS_MODEL, offering.getProcessModel());

                writeDescriptionElements(offering.getProcessDescription());
            });
        });
    }

    private void writeProcessInputDescription(ProcessInputDescription input)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_INPUT, input, x -> {
            attr(XMLSchemaConstants.Attr.AN_MIN_OCCURS, x.getOccurence().getMin()
                 .toString());
            attr(XMLSchemaConstants.Attr.AN_MAX_OCCURS, x.getOccurence().getMax()
                 .map(Object::toString).orElse(UNBOUNDED));
            writeDescriptionElements(x);
            x.visit(this.concreteInputWriter);
        });
    }

    private void writeProcessOutputDescription(ProcessOutputDescription output)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_OUTPUT, () -> {
            writeDescriptionElements(output);
            output.visit(this.concreteOutputWriter);
        });
    }

    private void writeDescriptionElements(Description description)
            throws XMLStreamException {
        writeLanguageString(OWSConstants.Elem.QN_TITLE, description.getTitle());
        writeLanguageString(OWSConstants.Elem.QN_ABSTRACT, description.getAbstract());
        writeKeywords(description.getKeywords());
        writeCode(OWSConstants.Elem.QN_IDENTIFIER, description.getId());
        writeMetadata(description.getMetadata());
    }

    private void writeFormat(Format format,
                             Optional<BigInteger> maximumMegabytes,
                             boolean isDefaultFormat)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_FORMAT, () -> {
            writeDataEncodingAttributes(format);
            attr(WPSConstants.Attr.AN_MAXIMUM_MEGABYTES, maximumMegabytes
                 .map(BigInteger::toString));
            if (isDefaultFormat) {
                attr(WPSConstants.Attr.AN_DEFAULT, "true");
            }
        });
    }

    private void writeLiteralDataDomain(LiteralDataDomain literalDataDomain,
                                        boolean defaultDomain)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_LITERAL_DATA_DOMAIN, literalDataDomain, ldd -> {
            if (defaultDomain) {
                attr(WPSConstants.Attr.AN_DEFAULT, "true");
            }

            OwsPossibleValues vd = ldd.getPossibleValues();

            if (vd.isAnyValue()) {
                writeAnyValue(vd.asAnyValues());
            } else if (vd.isAllowedValues()) {
                writeAllowedValues(vd.asAllowedValues());
            } else if (vd.isValuesReference()) {
                writeValuesReference(vd.asValuesReference());
            }

            writeDomainMetadata(OWSConstants.Elem.QN_DATA_TYPE, ldd.getDataType());
            writeDomainMetadata(OWSConstants.Elem.QN_UOM, ldd.getUOM());
            element(OWSConstants.Elem.QN_DEFAULT_VALUE, ldd.getDefaultValue().map(OwsValue::getValue));
        });
    }

    private void writeBoundingBoxDescription(BoundingBoxDescription input)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_BOUNDING_BOX_DATA, input, x -> {
            writeFormats(BoundingBoxInputOutputHandler.FORMATS);

            element(WPSConstants.Elem.QN_SUPPORTED_CRS, x.getDefaultCRS(), crs -> {
                attr(WPSConstants.Attr.AN_DEFAULT, "true");
                chars(crs.getValue().toString());
            });
            for (OwsCRS crs : x.getSupportedCRS()) {
                element(WPSConstants.Elem.QN_SUPPORTED_CRS, crs.getValue().toString());
            }

        });
    }

    private void writeComplexDescription(ComplexDescription input)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_COMPLEX_DATA, input, x -> writeFormats(x));
    }

    private void writeLiteralDescription(LiteralDescription input)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_LITERAL_DATA, input, x -> {
            writeFormats(LiteralInputOutputHandler.FORMATS);

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
        element(WPSConstants.Elem.QN_PROCESS, object, x -> {
            writeNamespaces();
            writeDescriptionElements(object);
            writeProcessInputDescriptionContainer((ProcessInputDescriptionContainer) object);
            writeProcessOutputDescriptionContainer((ProcessOutputDescriptionContainer) object);
        });
    }

    private void writeReferenceData(ReferenceProcessData reference)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_REFERENCE, () -> {
            writeNamespaces();

            attr(XLinkConstants.Attr.QN_HREF, reference.getURI().toString());
            writeDataEncodingAttributes(reference.getFormat());
            if (reference.getBody().isPresent()) {
                Body body = reference.getBody().get();
                if (body.isInline()) {
                    element(WPSConstants.Elem.QN_BODY, body.asInline(), b -> cdata(b
                            .getBody()));
                } else if (body.isReferenced()) {
                    element(WPSConstants.Elem.QN_BODY_REFERENCE, body.asReferenced(), b
                            -> attr(XLinkConstants.Attr.QN_HREF, b.getHref().toString()));
                } else {
                    throw new AssertionError();
                }
            }
        });
    }

    private void writeStatusInfo(StatusInfo object)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_STATUS_INFO, () -> {
            writeNamespaces();
            element(WPSConstants.Elem.QN_JOB_ID, object.getJobId().getValue());
            element(WPSConstants.Elem.QN_STATUS, object.getStatus().getValue());
            element(WPSConstants.Elem.QN_EXPIRATION_DATE, object.getExpirationDate()
                    .map(this::format));
            element(WPSConstants.Elem.QN_ESTIMATED_COMPLETION, object
                    .getEstimatedCompletion().map(this::format));
            element(WPSConstants.Elem.QN_NEXT_POLL, object.getNextPoll()
                    .map(this::format));
            element(WPSConstants.Elem.QN_PERCENT_COMPLETED, object.getPercentCompleted()
                    .map(String::valueOf));
        });
    }

    private void writeDataEncodingAttributes(Format format)
            throws XMLStreamException {
        attr(WPSConstants.Attr.AN_MIME_TYPE, format.getMimeType());
        attr(WPSConstants.Attr.AN_ENCODING, format.getEncoding());
        attr(WPSConstants.Attr.AN_SCHEMA, format.getSchema());
    }

    private void writeValueData(ValueProcessData value)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_DATA, value, x -> {
            writeNamespaces();

            Format format = x.getFormat();
            try (InputStream data = x.getData()) {

                if (!x.getFormat().hasEncoding() || x.getFormat()
                    .isCharacterEncoding()) {

                    writeDataEncodingAttributes(format
                            .withEncoding(documentEncoding()));

                    Charset charset = format.getEncodingAsCharsetOrDefault();

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
                    writeDataEncodingAttributes(format.withBase64Encoding());

                    if (format.isBase64()) {
                        // it is already base64 encoded
                        try (InputStream in = new Base64InputStream(data, false)) {
                            writeBase64(in);
                        }
                    } else {
                        writeBase64(data);
                    }
                }
            } catch (IOException ex) {
                throw new XMLStreamException(ex);
            }
        });
    }

    private void writeResult(Result result)
            throws XMLStreamException {
        if (result.getResponseMode() == ResponseMode.RAW) {
            writeRawResult(result);
        } else {
            element(WPSConstants.Elem.QN_RESULT, result, x -> {
                writeNamespaces();
                element(WPSConstants.Elem.QN_JOB_ID, x.getJobId().map(JobId::getValue));
                element(WPSConstants.Elem.QN_EXPIRATION_DATE, x.getExpirationDate().map(this::format));
                for (ProcessData data : x.getOutputs()) {
                    writeOutput(data);
                }
            });
        }
    }

    private void writeOutput(ProcessData data)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_OUTPUT, data, x -> {
            writeNamespaces();
            attr(WPSConstants.Attr.AN_ID, x.getId().getValue());
            // FIXME codeSpace is not allowed here...
            attr(OWSConstants.Attr.AN_CODE_SPACE, x.getId().getCodeSpace()
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
        element(WPSConstants.Elem.QN_PROCESS_OFFERING, offering, x -> {
            attr(WPSConstants.Attr.AN_JOB_CONTROL_OPTIONS, x.getJobControlOptions(), JobControlOption::getValue);
            attr(WPSConstants.Attr.AN_OUTPUT_TRANSMISSION, x.getOutputTransmissionModes(), DataTransmissionMode::getValue);
            attr(WPSConstants.Attr.AN_PROCESS_VERSION, x.getProcessVersion());
            attr(WPSConstants.Attr.AN_PROCESS_MODEL, x.getProcessModel());
            writeProcessDescription(offering.getProcessDescription());
        });
    }

    private void writeNamespaces()
            throws XMLStreamException {
        namespace(WPSConstants.NS_WPS_PREFIX, WPSConstants.NS_WPS);
        namespace(OWSConstants.NS_OWS_PREFIX, OWSConstants.NS_OWS);
        namespace(XLinkConstants.NS_XLINK_PREFIX, XLinkConstants.NS_XLINK);
    }

    private void writeGroupData(GroupProcessData x)
            throws XMLStreamException {
        for (ProcessData output : x.getElements()) {
            writeOutput(output);
        }
    }

    private void writeProcessOfferings(ProcessOfferings offerings)
            throws XMLStreamException {
        element(WPSConstants.Elem.QN_PROCESS_OFFERINGS, () -> {
            writeNamespaces();
            for (ProcessOffering processOffering : offerings) {
                writeProcessOffering(processOffering);
            }
        });
    }

    private void writeFormats(ComplexDescription x) throws XMLStreamException {
        writeFormat(x.getDefaultFormat(), x.getMaximumMegabytes(), true);
        for (Format format : x.getSupportedFormats()) {
            writeFormat(format, x.getMaximumMegabytes(), false);
        }
    }

    private void writeFormats(Set<Format> formats) throws XMLStreamException {
        Iterator<Format> iter = formats.iterator();
        if (iter.hasNext()) {
            writeFormat(iter.next(), Optional.empty(), true);
            while (iter.hasNext()) {
                writeFormat(iter.next(), Optional.empty(), false);
            }
        }
    }

    private void writeRawResult(Result result) throws XMLStreamException {
        ProcessData output = result.getOutputs().iterator().next();
        if (output.isGroup() || output.isReference()) {
            writeOutput(output);
        } else {
            // if we end up here the output is guaranteed to be XML
            Charset charset = output.asValue().getFormat().getEncodingAsCharsetOrDefault();
            try (InputStream in = output.asValue().getData();
                 InputStreamReader reader = new InputStreamReader(in, charset)) {
                write(reader);
            } catch (IOException ex) {
                throw new XMLStreamException(ex);
            }
        }
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
