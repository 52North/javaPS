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
package org.n52.javaps.rest.serializer;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.n52.faroe.annotation.Configurable;
import org.n52.javaps.rest.MediaTypes;
import org.n52.javaps.rest.model.AllowedValues;
import org.n52.javaps.rest.model.AnyValue;
import org.n52.javaps.rest.model.BoundingBoxDataType;
import org.n52.javaps.rest.model.ComplexDataType;
import org.n52.javaps.rest.model.DescriptionType;
import org.n52.javaps.rest.model.FormatDescription;
import org.n52.javaps.rest.model.InputDescription;
import org.n52.javaps.rest.model.JobControlOptions;
import org.n52.javaps.rest.model.Link;
import org.n52.javaps.rest.model.LiteralDataDomain;
import org.n52.javaps.rest.model.LiteralDataType;
import org.n52.javaps.rest.model.Metadata;
import org.n52.javaps.rest.model.NameReferenceType;
import org.n52.javaps.rest.model.OutputDescription;
import org.n52.javaps.rest.model.Process;
import org.n52.javaps.rest.model.ProcessCollection;
import org.n52.javaps.rest.model.ProcessSummary;
import org.n52.javaps.rest.model.Range;
import org.n52.javaps.rest.model.Range.RangeClosureEnum;
import org.n52.javaps.rest.model.SupportedCRS;
import org.n52.javaps.rest.model.TransmissionMode;
import org.n52.javaps.rest.model.ValueReference;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsRange;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.OwsValueRestriction;
import org.n52.shetland.ogc.ows.OwsValuesReference;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobControlOption;
import org.n52.shetland.ogc.wps.description.BoundingBoxDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.LiteralDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ProcessSerializer extends AbstractSerializer {
    public ProcessSerializer() {
    }

    public Process serializeProcessOffering(org.n52.shetland.ogc.wps.ProcessOffering processOffering) {
        Process process = createProcessSummary(processOffering, Process::new);
        process.setLinks(Collections.singletonList(createExecuteLink(process)));
        process.setInputs(createInputDescriptions(processOffering.getProcessDescription().getInputDescriptions()));
        process.setOutputs(createOutputDescriptions(processOffering.getProcessDescription().getOutputDescriptions()));
        return process;
    }

    private <T extends ProcessSummary> T createProcessSummary(org.n52.shetland.ogc.wps.ProcessOffering processOffering,
            Supplier<T> factory) {
        T processSummary = createDescription(processOffering.getProcessDescription(), factory);
        processOffering.getProcessVersion().ifPresent(processSummary::setVersion);
        processSummary.setJobControlOptions(createJobControlOptions(processOffering));
        processSummary.setOutputTransmission(createOutputTransmissionModes(processOffering));
        return processSummary;
    }

    private ProcessSummary createProcessSummary(org.n52.shetland.ogc.wps.ProcessOffering processOffering) {
        ProcessSummary process = createProcessSummary(processOffering, ProcessSummary::new);
        process.links(Collections.singletonList(getProcessLink(process)));
        return process;
    }

    private List<TransmissionMode> createOutputTransmissionModes(
            org.n52.shetland.ogc.wps.ProcessOffering processOffering) {
        return processOffering.getOutputTransmissionModes().stream().map(this::createDataTransmissionMode)
                .collect(toList());
    }

    private TransmissionMode createDataTransmissionMode(DataTransmissionMode outputTransmissionMode) {
        switch (outputTransmissionMode) {
            case VALUE:
                return TransmissionMode.VALUE;
            case REFERENCE:
                return TransmissionMode.REFERENCE;
            default:
                throw new IllegalArgumentException("unsupported data transmission mode" + outputTransmissionMode);
        }
    }

    private List<JobControlOptions> createJobControlOptions(org.n52.shetland.ogc.wps.ProcessOffering processOffering) {
        return processOffering.getJobControlOptions().stream().map(this::createJobControlOption)
                .filter(Objects::nonNull).collect(toList());
    }

    private JobControlOptions createJobControlOption(JobControlOption jobControlOption) {
        if (jobControlOption.equals(JobControlOption.async())) {
            return JobControlOptions.ASYNC_EXECUTE;
        } else if (jobControlOption.equals(JobControlOption.sync())) {
            return JobControlOptions.SYNC_EXECUTE;
        } else {
            return null;
        }
    }

    private <T extends DescriptionType> T createDescription(Description description, Supplier<T> factory) {
        T descriptionType = factory.get();
        descriptionType.setId(description.getId().getValue());
        descriptionType.setTitle(description.getTitle().getValue());
        List<String> keywords = description.getKeywords().stream().map(OwsKeyword::getKeyword)
                .map(OwsLanguageString::getValue).collect(toList());
        descriptionType.setKeywords(keywords);
        description.getAbstract().map(OwsLanguageString::getValue).ifPresent(descriptionType::setDescription);
        description.getAbstract().map(OwsLanguageString::getValue).ifPresent(descriptionType::setDescription);
        descriptionType.setMetadata(description.getMetadata().stream().map(this::createMetadata).collect(toList()));
        return descriptionType;
    }

    private Metadata createMetadata(OwsMetadata x) {
        Metadata metadata = new Metadata();
        x.getRole().map(URI::toString).ifPresent(metadata::setRole);
        x.getHref().map(URI::toString).ifPresent(metadata::setHref);
        return metadata;
    }

    private List<OutputDescription> createOutputDescriptions(
            Collection<? extends ProcessOutputDescription> descriptions) {
        return descriptions.stream().map(this::createProcessOutputDescription).collect(toList());
    }

    private OutputDescription createProcessOutputDescription(ProcessOutputDescription processOutputDescription) {
        OutputDescription outputDescription = createDescription(processOutputDescription, OutputDescription::new);
        if (processOutputDescription.isComplex()) {
            outputDescription.setOutput(createComplexOutput(processOutputDescription.asComplex()));
        } else if (processOutputDescription.isLiteral()) {
            outputDescription.setOutput(createLiteralOutput(processOutputDescription.asLiteral()));
        } else if (processOutputDescription.isBoundingBox()) {
            outputDescription.setOutput(createBoundingBoxOutput(processOutputDescription.asBoundingBox()));
        }
        return outputDescription;
    }

    private BoundingBoxDataType createBoundingBoxOutput(BoundingBoxOutputDescription description) {
        return new BoundingBoxDataType().supportedCRS(createSupportedCRS(description));
    }

    private LiteralDataType createLiteralOutput(LiteralOutputDescription description) {
        return new LiteralDataType().literalDataDomains(createLiteralDataDomains(description));
    }

    private ComplexDataType createComplexOutput(ComplexOutputDescription description) {
        return new ComplexDataType().formats(createFormats(description));
    }

    private List<LiteralDataDomain> createLiteralDataDomains(LiteralDescription description) {
        return Stream
                .concat(Stream.of(description.getDefaultLiteralDataDomain()),
                        description.getSupportedLiteralDataDomains().stream())
                .map(this::createLiteralDataDomain).collect(toList());
    }

    private List<InputDescription> createInputDescriptions(Collection<? extends ProcessInputDescription> descriptions) {
        return descriptions.stream().map(this::createInputDescription).collect(toList());
    }

    private InputDescription createInputDescription(ProcessInputDescription processInputDescription) {
        InputDescription inputDescription = createDescription(processInputDescription, InputDescription::new);
        inputDescription.setMinOccurs(processInputDescription.getOccurence().getMin());
        processInputDescription.getOccurence().getMax().ifPresent(inputDescription::setMaxOccurs);
        if (processInputDescription.isComplex()) {
            inputDescription.setInput(createComplexInput(processInputDescription.asComplex()));
        } else if (processInputDescription.isLiteral()) {
            inputDescription.setInput(createLiteralInput(processInputDescription.asLiteral()));
        } else if (processInputDescription.isBoundingBox()) {
            inputDescription.setInput(createBoundingBoxInput(processInputDescription.asBoundingBox()));
        } else if (processInputDescription.isGroup()) {
            throw new IllegalArgumentException("group inputs are not supported");
        }
        return inputDescription;
    }

    private BoundingBoxDataType createBoundingBoxInput(BoundingBoxInputDescription description) {
        return new BoundingBoxDataType().supportedCRS(createSupportedCRS(description));
    }

    private LiteralDataType createLiteralInput(LiteralInputDescription description) {
        return new LiteralDataType().literalDataDomains(createLiteralDataDomains(description));
    }

    private ComplexDataType createComplexInput(ComplexInputDescription description) {
        return new ComplexDataType().formats(createFormats(description));
    }

    private List<SupportedCRS> createSupportedCRS(BoundingBoxDescription description) {
        List<SupportedCRS> serializedSupportedCRS = Stream
                .concat(Stream.of(description.getDefaultCRS()), description.getSupportedCRS().stream())
                .map(OwsCRS::getValue).map(URI::toString).map(x -> new SupportedCRS().crs(x))
                .collect(Collectors.toList());
        serializedSupportedCRS.get(0).setDefault(true);
        return serializedSupportedCRS;
    }

    private LiteralDataDomain createLiteralDataDomain(
            org.n52.shetland.ogc.wps.description.LiteralDataDomain defaultLiteralDataDomain) {
        LiteralDataDomain literalDataDomain = new LiteralDataDomain();
        literalDataDomain.setDataType(createLiteralDataDomainDataType(defaultLiteralDataDomain));
        literalDataDomain.setValueDefinition(createPossibleValues(defaultLiteralDataDomain.getPossibleValues()));
        defaultLiteralDataDomain.getDefaultValue().map(OwsValue::getValue)
                .ifPresent(literalDataDomain::setDefaultValue);
        return literalDataDomain;
    }

    private NameReferenceType createLiteralDataDomainDataType(
            org.n52.shetland.ogc.wps.description.LiteralDataDomain literalDataDomain) {
        NameReferenceType literalDataDomainDataType = new NameReferenceType();
        Optional<OwsDomainMetadata> dataType = literalDataDomain.getDataType();
        dataType.flatMap(OwsDomainMetadata::getValue).ifPresent(literalDataDomainDataType::setName);
        dataType.flatMap(OwsDomainMetadata::getReference).map(URI::toString)
                .ifPresent(literalDataDomainDataType::setReference);
        return literalDataDomainDataType;
    }

    private Object createPossibleValues(OwsPossibleValues possibleValues) {
        if (possibleValues.isAnyValue()) {
            return new AnyValue().anyValue(true);
        } else if (possibleValues.isAllowedValues()) {
            return createAllowedValues(possibleValues.asAllowedValues());
        } else if (possibleValues.isValuesReference()) {
            return createValuesReference(possibleValues.asValuesReference());
        } else {
            throw new IllegalArgumentException("unsupported possible values: " + possibleValues);
        }
    }

    private Object createValuesReference(OwsValuesReference valuesReference) {
        return new ValueReference().valueReference(valuesReference.getReference().toString());
    }

    private AllowedValues createAllowedValues(OwsAllowedValues allowedValues) {
        
        List<Object> allowedValuesList = new ArrayList<Object>();
        
        allowedValues.stream().map(this::createAllowedValue).collect(Collectors.toCollection(() -> allowedValuesList));
        
        AllowedValues allowedValues2Serialize = new AllowedValues();
        
        allowedValues2Serialize.allowedValues(allowedValuesList);
        
        return allowedValues2Serialize;
    }

    private Object createAllowedValue(OwsValueRestriction allowedValue) {
        if (allowedValue.isValue()) {
            return allowedValue.asValue().getValue();
        } else if (allowedValue.isRange()) {
            return createRange(allowedValue.asRange());
        } else {
            throw new IllegalArgumentException("unsupported allowed value " + allowedValue);
        }
    }

    private Range createRange(OwsRange owsRange) {
        Range range = new Range();
        owsRange.getLowerBound().map(OwsValue::getValue).ifPresent(range::setMinimumValue);
        owsRange.getUpperBound().map(OwsValue::getValue).ifPresent(range::setMaximumValue);
        owsRange.getSpacing().map(OwsValue::getValue).ifPresent(range::setSpacing);
        range.setRangeClosure(getRangeClosure(owsRange));
        return range;
    }

    private RangeClosureEnum getRangeClosure(OwsRange range) {
        switch (range.getLowerBoundType()) {
            case CLOSED:
                switch (range.getUpperBoundType()) {
                    case CLOSED:
                        return RangeClosureEnum.CLOSED;
                    case OPEN:
                        return RangeClosureEnum.CLOSED_OPEN;
                    default:
                        throw unsupportedBoundType(range.getUpperBoundType());
                }
            case OPEN:
                switch (range.getUpperBoundType()) {
                    case CLOSED:
                        return RangeClosureEnum.OPEN_CLOSED;
                    case OPEN:
                        return RangeClosureEnum.OPEN;
                    default:
                        throw unsupportedBoundType(range.getUpperBoundType());
                }
            default:
                throw unsupportedBoundType(range.getLowerBoundType());
        }
    }

    private IllegalArgumentException unsupportedBoundType(OwsRange.BoundType lowerBoundType) {
        return new IllegalArgumentException("unsupported bound type: " + lowerBoundType);
    }

    private List<FormatDescription> createFormats(ComplexDescription description) {
        List<FormatDescription> formats = Stream
                .concat(Stream.of(description.getDefaultFormat()), description.getSupportedFormats().stream())
                .map(format -> createFormat(format, description.getMaximumMegabytes())).collect(toList());
        formats.get(0).setDefault(true);
        return formats;
    }

    private FormatDescription createFormat(Format format, Optional<BigInteger> maximumMegabytes) {
        FormatDescription formatDescription = new FormatDescription();
        format.getMimeType().ifPresent(formatDescription::setMimeType);
        format.getSchema().ifPresent(formatDescription::setSchema);
        format.getEncoding().ifPresent(formatDescription::setEncoding);
        maximumMegabytes.ifPresent(formatDescription::setMaximumMegabytes);
        return formatDescription;
    }

    public ProcessCollection createProcessCollection(Set<org.n52.shetland.ogc.wps.ProcessOffering> offerings) {
        ProcessCollection collection = new ProcessCollection();

        offerings.stream().map(this::createProcessSummary).forEachOrdered(collection::add);

        return collection;
    }

    private Link createExecuteLink(ProcessSummary process) {
        Link executeEndpointLink = new Link();
        executeEndpointLink.setHref(getJobsHref(process.getId()));
        executeEndpointLink.setRel("execute");
        executeEndpointLink.setTitle("Execute endpoint");
        return executeEndpointLink;
    }

    private Link getProcessLink(ProcessSummary process) {
        Link link = new Link();
        link.setHref(getProcessHref(process.getId()));
        link.setType(MediaTypes.APPLICATION_JSON);
        link.setRel("process-desc");
        link.setTitle("Process description");
        return link;
    }

}
