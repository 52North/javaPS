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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.javaps.algorithm.ProcessDescription;
import org.n52.javaps.algorithm.descriptor.AlgorithmDescriptor;
import org.n52.javaps.algorithm.descriptor.ComplexDataInputDescriptor;
import org.n52.javaps.algorithm.descriptor.ComplexDataOutputDescriptor;
import org.n52.javaps.algorithm.descriptor.InputDescriptor;
import org.n52.javaps.algorithm.descriptor.LiteralDataInputDescriptor;
import org.n52.javaps.algorithm.descriptor.LiteralDataOutputDescriptor;
import org.n52.javaps.algorithm.descriptor.OutputDescriptor;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.io.Format;
import org.n52.javaps.io.GeneratorFactory;
import org.n52.javaps.io.IGenerator;
import org.n52.javaps.io.IOHandler;
import org.n52.javaps.io.IParser;
import org.n52.javaps.io.ParserFactory;
import org.n52.javaps.response.DescribeProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class encodes a ProcessDescription object as a DescribeProcessResponse
 * XML document.
 *
 * @author Benjamin Pross
 *
 */
public class DescribeProcessResponseWriter extends AbstractXmlStreamWriter<DescribeProcessResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DescribeProcessResponseWriter.class);

    private static final String NS_OWS_20 = "http://www.opengis.net/ows/2.0";

    private static final String NS_WPS_20 = "http://www.opengis.net/wps/2.0";

    private static final String EN_PROCESS_OFFERINGS = "ProcessOfferings";

    private static final String EN_PROCESS_OFFERING = "ProcessOffering";

    private static final String NS_OWS_PREFIX = "ows";

    private static final String NS_WPS_PREFIX = "wps";

    private static final QName QN_PROCESS_OFFERINGS = new QName(NS_WPS_20, EN_PROCESS_OFFERINGS, NS_WPS_PREFIX);

    private static final QName QN_PROCESS_OFFERING = new QName(NS_WPS_20, EN_PROCESS_OFFERING, NS_WPS_PREFIX);

    private static final String EN_PROCESS = "Process";

    private static final QName QN_PROCESS = new QName(NS_WPS_20, EN_PROCESS, NS_WPS_PREFIX);

    private static final String EN_INPUT = "Input";

    private static final QName QN_INPUT = new QName(NS_WPS_20, EN_INPUT, NS_WPS_PREFIX);

    private static final String EN_COMPLEX_DATA = "ComplexData";

    private static final QName QN_COMPLEX_DATA = new QName(NS_WPS_20, EN_COMPLEX_DATA, NS_WPS_PREFIX);

    private static final String EN_LITERAL_DATA = "LiteralData";

    private static final QName QN_LITERAL_DATA = new QName(NS_WPS_20, EN_LITERAL_DATA, NS_WPS_PREFIX);

    private static final String EN_BOUNDING_BOX_DATA = "BoundingBoxData";

    private static final QName QN_BOUNDING_BOX_DATA = new QName(NS_WPS_20, EN_BOUNDING_BOX_DATA, NS_WPS_PREFIX);

    private static final String EN_LITERAL_DATA_DOMAIN = "LiteralDataDomain";

    private static final QName QN_LITERAL_DATA_DOMAIN = new QName(NS_OWS_20, EN_LITERAL_DATA_DOMAIN, NS_OWS_PREFIX);

    private static final String EN_FORMAT = "Format";

    private static final QName QN_FORMAT = new QName(NS_WPS_20, EN_FORMAT, NS_WPS_PREFIX);

    private static final String EN_OUTPUT = "Output";

    private static final QName QN_OUTPUT = new QName(NS_WPS_20, EN_OUTPUT, NS_WPS_PREFIX);

    private static final String EN_TITLE = "Title";

    private static final QName QN_TITLE = new QName(NS_OWS_20, EN_TITLE, NS_OWS_PREFIX);

    private static final String EN_IDENTIFIER = "Identifier";

    private static final QName QN_IDENTIFIER = new QName(NS_OWS_20, EN_IDENTIFIER, NS_OWS_PREFIX);

    private static final String EN_ABSTRACT = "Abstract";

    private static final QName QN_ABSTRACT = new QName(NS_OWS_20, EN_ABSTRACT, NS_OWS_PREFIX);

    private static final String EN_DATA_TYPE = "DataType";

    private static final QName QN_DATA_TYPE = new QName(NS_OWS_20, EN_DATA_TYPE, NS_OWS_PREFIX);

    private static final String EN_DEFAULT_VALUE = "DefaultValue";

    private static final QName QN_DEFAULT_VALUE = new QName(NS_OWS_20, EN_DEFAULT_VALUE, NS_OWS_PREFIX);

    private static final String EN_ANY_VALUE = "AnyValue";

    private static final QName QN_ANY_VALUE = new QName(NS_OWS_20, EN_ANY_VALUE, NS_OWS_PREFIX);

    private static final String EN_UOM = "UOM";

    private static final QName QN_UOM = new QName(NS_OWS_20, EN_UOM, NS_OWS_PREFIX);

    private static final String EN_ALLOWED_VALUES = "AllowedValues";

    private static final QName QN_ALLOWED_VALUES = new QName(NS_OWS_20, EN_ALLOWED_VALUES, NS_OWS_PREFIX);

    private static final String EN_VALUE = "Value";

    private static final QName QN_VALUE = new QName(NS_OWS_20, EN_VALUE, NS_OWS_PREFIX);

    private static final String EN_RANGE = "Range";

    private static final QName QN_RANGE = new QName(NS_OWS_20, EN_RANGE, NS_OWS_PREFIX);

    private static final String EN_MAXIMUM_VALUE = "MaximumValue";

    private static final QName QN_MAXIMUM_VALUE = new QName(NS_OWS_20, EN_MAXIMUM_VALUE, NS_OWS_PREFIX);

    private static final String EN_MINIMUM_VALUE = "MinimumValue";

    private static final QName QN_MINIMUM_VALUE = new QName(NS_OWS_20, EN_MINIMUM_VALUE, NS_OWS_PREFIX);

    private static final String EN_SPACING = "Spacing";

    private static final QName QN_SPACING = new QName(NS_OWS_20, EN_SPACING, NS_OWS_PREFIX);

    private static final String AN_PROCESS_VERSION = "processVersion";

    private static final String AN_JOB_CONTROL_OPTIONS = "jobControlOptions";

    private static final String AN_OUTPUT_TRANSMISSION = "outputTransmission";

    private static final String AN_MIN_OCCURS = "minOccurs";

    private static final String AN_MAX_OCCURS = "maxOccurs";

    private static final String AN_DEFAULT = "default";

    private static final String AN_MIME_TYPE = "mimeType";

    private static final String AN_SCHEMA = "schema";

    private static final String AN_ENCODING = "encoding";

    private static final String AN_REFERENCE = "reference";

    public static final StreamWriterKey KEY = new XmlStreamWriterKey(DescribeProcessResponse.class);

    @Autowired
    private ParserFactory parserFactory;

    @Autowired
    private GeneratorFactory generatorFactory;

    public DescribeProcessResponseWriter() {
        LOGGER.info("DescribeProcessResponseWriter constructed.");
    }

    @Override
    protected void write(DescribeProcessResponse describeProcessResponse) throws XMLStreamException {

        start(QN_PROCESS_OFFERINGS);
        namespace(NS_OWS_PREFIX, NS_OWS_20);
        namespace(NS_WPS_PREFIX, NS_WPS_20);

        for (ProcessDescription processDescription : describeProcessResponse.getProcessDescriptions()) {

            AlgorithmDescriptor algorithmDescriptor = processDescription.getAlgorithmDescriptor();

            if(algorithmDescriptor == null){
                LOGGER.warn("Algorithm descriptor is null");
                continue;
            }

            start(QN_PROCESS_OFFERING);
            attr(AN_PROCESS_VERSION, algorithmDescriptor.getVersion());
            attr(AN_JOB_CONTROL_OPTIONS, algorithmDescriptor.getJobControlOption());
            attr(AN_OUTPUT_TRANSMISSION, algorithmDescriptor.getOutputTransmissionMode());
            start(QN_PROCESS);
            start(QN_TITLE);
            chars(algorithmDescriptor.getTitle());
            end(QN_TITLE);
            start(QN_IDENTIFIER);
            chars(algorithmDescriptor.getIdentifier());
            end(QN_IDENTIFIER);
            start(QN_ABSTRACT);
            chars(algorithmDescriptor.getAbstract());
            end(QN_ABSTRACT);

            for (InputDescriptor<?> inputDescriptor : algorithmDescriptor.getInputDescriptors()) {

                start(QN_INPUT);
                attr(AN_MIN_OCCURS, inputDescriptor.getMinOccurs().toString());
                attr(AN_MAX_OCCURS, inputDescriptor.getMaxOccurs().toString());
                start(QN_IDENTIFIER);
                chars(inputDescriptor.getIdentifier());
                end(QN_IDENTIFIER);
                start(QN_TITLE);
                chars(inputDescriptor.getIdentifier());
                end(QN_TITLE);
                if (inputDescriptor.hasAbstract()) {
                    start(QN_ABSTRACT);
                    chars(inputDescriptor.getAbstract());
                    end(QN_ABSTRACT);
                }

                if (inputDescriptor instanceof LiteralDataInputDescriptor) {
                    LiteralDataInputDescriptor<?> literalDescriptor = (LiteralDataInputDescriptor<?>) inputDescriptor;

                    start(QN_LITERAL_DATA);

                    // default format
                    start(QN_FORMAT);
                    attr(AN_DEFAULT, "true");
                    attr(AN_MIME_TYPE, "text/plain");
                    end(QN_FORMAT);

                    // additional XML format
                    start(QN_FORMAT);
                    attr(AN_MIME_TYPE, "text/xml");
                    end(QN_FORMAT);

                    start(QN_LITERAL_DATA_DOMAIN);
                    start(QN_DATA_TYPE);
                    attr(AN_REFERENCE, literalDescriptor.getDataType());
                    end(QN_DATA_TYPE);

                    if (literalDescriptor.hasDefaultValue()) {
                        start(QN_DEFAULT_VALUE);
                        chars(literalDescriptor.getDefaultValue());
                        end(QN_DEFAULT_VALUE);
                    }
                    if (literalDescriptor.hasAllowedValues()) {

                        start(QN_ALLOWED_VALUES);

                        for (String allowedValue : literalDescriptor.getAllowedValues()) {
                            start(QN_VALUE);
                            chars(allowedValue);
                            end(QN_VALUE);
                        }

                        end(QN_ALLOWED_VALUES);

                        // TODO Range? UOM? have to be added to
                        // AnnotatedAlgorithm classes

                    } else {
                        empty(QN_ANY_VALUE);
                    }

                    end(QN_LITERAL_DATA_DOMAIN);

                    end(QN_LITERAL_DATA);
                } else if (inputDescriptor instanceof ComplexDataInputDescriptor) {

                    ComplexDataInputDescriptor<?> complexInputDescriptor = (ComplexDataInputDescriptor<?>) inputDescriptor;

                    start(QN_COMPLEX_DATA);

                    writeComplexDataInput(complexInputDescriptor.getBinding());

                    end(QN_COMPLEX_DATA);
                }
            }

            // 3. Outputs
            if (algorithmDescriptor.getOutputDescriptors().size() < 1) {
                LOGGER.error("No outputs found for algorithm {}", algorithmDescriptor.getIdentifier());
            }
            for (OutputDescriptor<?> outputDescriptor : algorithmDescriptor.getOutputDescriptors()) {

                start(QN_OUTPUT);

                start(QN_IDENTIFIER);
                chars(outputDescriptor.getIdentifier());
                end(QN_IDENTIFIER);
                start(QN_TITLE);
                chars(outputDescriptor.getIdentifier());
                end(QN_TITLE);
                if (outputDescriptor.hasAbstract()) {
                    start(QN_ABSTRACT);
                    chars(outputDescriptor.getAbstract());
                    end(QN_ABSTRACT);
                }

                if (outputDescriptor instanceof LiteralDataOutputDescriptor) {
                    LiteralDataOutputDescriptor<?> literalDescriptor = (LiteralDataOutputDescriptor<?>) outputDescriptor;

                    start(QN_LITERAL_DATA);

                    // default format
                    start(QN_FORMAT);
                    attr(AN_DEFAULT, "true");
                    attr(AN_MIME_TYPE, "text/plain");
                    end(QN_FORMAT);

                    // additional XML format
                    start(QN_FORMAT);
                    attr(AN_MIME_TYPE, "text/xml");
                    end(QN_FORMAT);

                    start(QN_LITERAL_DATA_DOMAIN);
                    start(QN_DATA_TYPE);
                    attr(AN_REFERENCE, literalDescriptor.getDataType());
                    end(QN_DATA_TYPE);

                    end(QN_LITERAL_DATA_DOMAIN);

                    end(QN_LITERAL_DATA);

                } else if (outputDescriptor instanceof ComplexDataOutputDescriptor) {

                    start(QN_COMPLEX_DATA);

                    writeComplexDataOutput(outputDescriptor.getBinding());

                    end(QN_COMPLEX_DATA);
                }

                end(QN_OUTPUT);
            }

            end(QN_PROCESS);

            end(QN_PROCESS_OFFERING);
        }

        end(QN_PROCESS_OFFERINGS);
    }

    private void writeComplexDataOutput(Class<?> dataTypeClass) {
        Collection<IGenerator> generators = generatorFactory.getAllGenerators();
        List<IGenerator> foundGenerators = new ArrayList<IGenerator>();
        for (IGenerator generator : generators) {
            Class<?>[] supportedClasses = generator.getSupportedDataBindings();
            for (Class<?> clazz : supportedClasses) {
                if (dataTypeClass.isAssignableFrom(clazz)) {
                    foundGenerators.add(generator);
                }
            }
        }
        try {
            writeComplexDataType(foundGenerators);
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void writeComplexDataInput(Class<?> dataTypeClass) {
        Collection<IParser> parsers = parserFactory.getAllParsers();
        List<IParser> foundParsers = new ArrayList<IParser>();
        for (IParser parser : parsers) {
            Class<?>[] supportedClasses = parser.getSupportedDataBindings();
            for (Class<?> clazz : supportedClasses) {
                if (dataTypeClass.isAssignableFrom(clazz)) {
                    foundParsers.add(parser);
                }
            }
        }
        try {
            writeComplexDataType(foundParsers);
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void writeComplexDataType(List<? extends IOHandler> handlers) throws XMLStreamException {

        boolean needDefault = true;

        for (IOHandler handler : handlers) {

            List<Format> fullFormats = handler.getSupportedFormats();
            if (fullFormats != null && fullFormats.size() > 0) {
                //FIXME this way always the default format of the first parser is used
                //as this could change due to different loading orders of the parsers
                //the default format could maybe be an additional property of the algorithm
                if (needDefault) {
                    needDefault = false;
                    writeFormat(fullFormats.get(0), true);
                }
                for (int formatIndex = 1, formatCount = fullFormats.size(); formatIndex < formatCount; ++formatIndex) {
                    writeFormat(fullFormats.get(formatIndex), false);
                }
            }
        }
    }

    private void writeFormat(Format format, boolean defaultFormat) throws XMLStreamException{
        // default format
        start(QN_FORMAT);
        if(defaultFormat){
            attr(AN_DEFAULT, ""+defaultFormat);
        }
        attr(AN_MIME_TYPE, format.getMimeType());
        if(format.hasSchema()){
            attr(AN_SCHEMA, format.getSchema());
        }
        if(format.hasEncoding()){
            attr(AN_ENCODING, format.getEncoding());
        }
        end(QN_FORMAT);
    }


    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
