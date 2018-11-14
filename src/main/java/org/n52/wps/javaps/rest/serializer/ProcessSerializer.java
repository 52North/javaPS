/*
 * Copyright (C) 2016 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.wps.javaps.rest.serializer;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsRange;
import org.n52.shetland.ogc.ows.OwsRange.BoundType;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.OwsValueRestriction;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.JobControlOption;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.model.AllowedValues;
import io.swagger.model.AnyValue;
import io.swagger.model.BoundingBoxDataType;
import io.swagger.model.ComplexDataType;
import io.swagger.model.FormatDescription;
import io.swagger.model.InputDescription;
import io.swagger.model.JobControlOptions;
import io.swagger.model.LiteralDataDomain;
import io.swagger.model.LiteralDataType;
import io.swagger.model.OutputDescription;
import io.swagger.model.Process;
import io.swagger.model.ProcessCollection;
import io.swagger.model.ProcessOffering;
import io.swagger.model.ProcessSummary;
import io.swagger.model.Range;
import io.swagger.model.Range.RangeClosureEnum;
import io.swagger.model.SupportedCRS;
import io.swagger.model.TransmissionMode;

@Configurable
public class ProcessSerializer {
    
    private static final Logger log = LoggerFactory.getLogger(ProcessSerializer.class);

    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", "/processes/");
    }
    
    public ProcessSerializer() {}
    
    public ProcessOffering serializeProcessOffering(org.n52.shetland.ogc.wps.ProcessOffering processOffering) {
                
        io.swagger.model.ProcessOffering serializedProcessOffering = new io.swagger.model.ProcessOffering();
        
        Process process = new Process();
        
        addProcessSummary((ProcessSummary)process, processOffering);
        
        process.setExecuteEndpoint(serviceURL + process.getId() + "/jobs");
        
        ProcessDescription processDescription = processOffering.getProcessDescription();
        
        process.setInputs(createInputs(processDescription.getInputDescriptions()));
        
        process.setOutputs(createOutputs(processDescription.getOutputDescriptions()));
        
        serializedProcessOffering.process(process);
        
        return serializedProcessOffering;
    }

    private void addProcessSummary(ProcessSummary process, org.n52.shetland.ogc.wps.ProcessOffering processOffering) {

        ProcessDescription processDescription = processOffering.getProcessDescription();
        
        String id = processDescription.getId().getValue();
        
        process.setId(id);
        
        try {            
            process.setVersion(processOffering.getProcessVersion().get());
        } catch (Exception e) {
            log.trace("Version not present.");
        }
        
        Collection<JobControlOption> jobControlOptions = processOffering.getJobControlOptions();
        
        List<JobControlOptions> serializedJobControlOptions = new ArrayList<>();
        
        for (JobControlOption jobControlOption : jobControlOptions) {
            if(jobControlOption.equals(JobControlOption.async())) {
                serializedJobControlOptions.add(JobControlOptions.ASYNC_EXECUTE);
            } else if(jobControlOption.equals(JobControlOption.sync())) {
                    serializedJobControlOptions.add(JobControlOptions.SYNC_EXECUTE);
                }
        }
        
        process.setJobControlOptions(serializedJobControlOptions);
        
        Collection<DataTransmissionMode> outputTransmissionModes = processOffering.getOutputTransmissionModes();
        
        List<TransmissionMode> serializedOutputTransmissionModes = new ArrayList<>();
        
        for (DataTransmissionMode outputTransmissionMode : outputTransmissionModes) {
            if(outputTransmissionMode.equals(DataTransmissionMode.VALUE)) {
                serializedOutputTransmissionModes.add(TransmissionMode.VALUE);
            } else if(outputTransmissionMode.equals(DataTransmissionMode.REFERENCE)) {
                    serializedOutputTransmissionModes.add(TransmissionMode.REFERENCE);
                }
        }
        
        process.setOutputTransmission(serializedOutputTransmissionModes);
        
        try {
            process.setAbstract(processDescription.getAbstract().get().getValue());
        } catch (Exception e) {
            log.trace("Abstract not present.");
        }
        
        try {
            process.setTitle(processDescription.getTitle().getValue());
        } catch (Exception e) {
            log.trace("Title not present.");
        }
        //TODO keywords, metadata
        
    }

    private List<OutputDescription> createOutputs(Collection<? extends ProcessOutputDescription> outputDescriptions) {
              
        List<OutputDescription> outputs = new ArrayList<>();        
        
        for (ProcessOutputDescription processOutputDescription : outputDescriptions) {

            OutputDescription outputDescription = new OutputDescription();
            
            outputDescription.setId(processOutputDescription.getId().getValue());
            
            try {
                outputDescription.setAbstract(processOutputDescription.getAbstract().get().getValue());
            } catch (Exception e) {
                log.trace("Abstract not present.");
            }
            
            try {
                outputDescription.setTitle(processOutputDescription.getTitle().getValue());
            } catch (Exception e) {
                log.trace("Title not present.");
            }
            
            if(processOutputDescription.isComplex()) {
                ComplexOutputDescription complexOutputDescription = processOutputDescription.asComplex();
                ComplexDataType complexInputType = new ComplexDataType();
                
                BigInteger maximumMegabytes = null;
                
                if(complexOutputDescription.getMaximumMegabytes().isPresent()) {
                    maximumMegabytes = complexOutputDescription.getMaximumMegabytes().get();
                }
                
                complexInputType.setFormats(createFormats(complexOutputDescription.getDefaultFormat(), complexOutputDescription.getSupportedFormats(), maximumMegabytes));
                
                outputDescription.setOutput(complexInputType);
                
            }   else if(processOutputDescription.isLiteral()) {
                LiteralDataType literalOutput = new LiteralDataType();
                
                LiteralOutputDescription literalOutputDescription = processOutputDescription.asLiteral();
                
                literalOutput.setLiteralDataDomain(serializeLiteralDataDomain(literalOutputDescription.getDefaultLiteralDataDomain()));//TODO what about the supported domains?
                
                outputDescription.setOutput(literalOutput);
                
            } else if(processOutputDescription.isBoundingBox()) {
                BoundingBoxDataType boundingBoxOutput = new BoundingBoxDataType();
                
                BoundingBoxOutputDescription boundingBoxDescription = processOutputDescription.asBoundingBox();
                
                OwsCRS defaultCRS = boundingBoxDescription.getDefaultCRS();
                
                Set<OwsCRS> supportedCRS = boundingBoxDescription.getSupportedCRS();
                
                boundingBoxOutput.setSupportedCRS(createSupportedCR(defaultCRS, supportedCRS));
                
                outputDescription.setOutput(boundingBoxOutput);
            }
            
            outputs.add(outputDescription);
            
        }
        
        return outputs;
    }

    private List<InputDescription> createInputs(Collection<? extends ProcessInputDescription> inputDescriptions) {
        
        List<InputDescription> inputs = new ArrayList<>();
        
        for (ProcessInputDescription processInputDescription : inputDescriptions) {
            
            InputDescription descriptionType = new InputDescription();
            
            InputOccurence occurrence = processInputDescription.getOccurence();
            try {
                descriptionType.setMinOccurs(Integer.parseInt(""+occurrence.getMin()));
            } catch (Exception e) {
                log.trace("Could not parse BigInteger: " + occurrence.getMin());
            }
            
            Optional<BigInteger> maxOccurrence = occurrence.getMax();
            
            if(maxOccurrence.isPresent()) {
                try {
                    descriptionType.setMaxOccurs(Integer.parseInt(""+maxOccurrence.get()));
                } catch (Exception e) {
                    log.trace("Could not parse BigInteger: " + maxOccurrence.get());
                }
            }
            
            if(processInputDescription.isComplex()) {
                ComplexInputDescription complexInputDescription = processInputDescription.asComplex();
                ComplexDataType complexInputType = new ComplexDataType();
                
                BigInteger maximumMegabytes = null;
                
                if(complexInputDescription.getMaximumMegabytes().isPresent()) {
                    maximumMegabytes = complexInputDescription.getMaximumMegabytes().get();
                }
                
                complexInputType.setFormats(createFormats(complexInputDescription.getDefaultFormat(), complexInputDescription.getSupportedFormats(), maximumMegabytes));
                
                descriptionType.setInput(complexInputType);
                
            } else if(processInputDescription.isLiteral()) {
                LiteralDataType literalInput = new LiteralDataType();
                
                LiteralInputDescription literalInputDescription = processInputDescription.asLiteral();
                
                literalInput.setLiteralDataDomain(serializeLiteralDataDomain(literalInputDescription.getDefaultLiteralDataDomain()));//TODO what about the supported domains?
                
                descriptionType.setInput(literalInput);
                
            } else if(processInputDescription.isBoundingBox()) {
                BoundingBoxDataType boundingBoxInput = new BoundingBoxDataType();
                
                BoundingBoxInputDescription boundingBoxDescription = processInputDescription.asBoundingBox();
                
                OwsCRS defaultCRS = boundingBoxDescription.getDefaultCRS();
                
                Set<OwsCRS> supportedCRS = boundingBoxDescription.getSupportedCRS();
                
                boundingBoxInput.setSupportedCRS(createSupportedCR(defaultCRS, supportedCRS));
                
                descriptionType.setInput(boundingBoxInput);
            }
            
            descriptionType.setId(processInputDescription.getId().getValue());
            
            try {
                descriptionType.setAbstract(processInputDescription.getAbstract().get().getValue());
            } catch (Exception e) {
                log.trace("Abstract not present.");
            }
            
            try {
                descriptionType.setTitle(processInputDescription.getTitle().getValue());
            } catch (Exception e) {
                log.trace("Title not present.");
            }
            //TODO keywords
            
            inputs.add(descriptionType);
        }
        
        return inputs;
    }

    private List<SupportedCRS> createSupportedCR(OwsCRS defaultCRS,
            Set<OwsCRS> supportedCRS) {
        
        List<SupportedCRS> serializedSupportedCRS = new ArrayList<>();
        
        SupportedCRS defaultSupportedCRS = new SupportedCRS();
        
        defaultSupportedCRS.setDefault(true);
        
        defaultSupportedCRS.setCrs(defaultCRS.getValue().toString());
        
        serializedSupportedCRS.add(defaultSupportedCRS);
        
        for (OwsCRS owsCRS : supportedCRS) {
            SupportedCRS supportedSupportedCRS = new SupportedCRS();
            
            supportedSupportedCRS.setDefault(false);
            
            supportedSupportedCRS.setCrs(owsCRS.getValue().toString()); 
            
            serializedSupportedCRS.add(supportedSupportedCRS);     
        }
        
        return serializedSupportedCRS;
    }

    private LiteralDataDomain serializeLiteralDataDomain(
            org.n52.shetland.ogc.wps.description.LiteralDataDomain defaultLiteralDataDomain) {

        LiteralDataDomain domain = new LiteralDataDomain();

        try {
            domain.setDataType(defaultLiteralDataDomain.getDataType().get().getValue().get());
        } catch (Exception e) {
            log.info("No data type present.");
        }

        Optional<OwsValue> defaultValue = defaultLiteralDataDomain.getDefaultValue();

        if (defaultValue.isPresent() && !defaultValue.get().getValue().isEmpty()) {
            domain.setDefaultValue(defaultValue.get().getValue());// TODO range possible here?
        }

        domain.setValueDefinition(serializeValueDefinition(defaultLiteralDataDomain));

        return domain;
    }

    private Object serializeValueDefinition(org.n52.shetland.ogc.wps.description.LiteralDataDomain defaultLiteralDataDomain) {
        
        OwsPossibleValues possibleValues = defaultLiteralDataDomain.getPossibleValues();
        
        if(possibleValues.isAnyValue()) {
            return new AnyValue().anyValue(true);
        } else if(possibleValues.isAllowedValues()) {
            
          AllowedValues allowedValues = new AllowedValues();
          
            Iterator<OwsValueRestriction> valueIterator = possibleValues.asAllowedValues().iterator();
            
            while (valueIterator.hasNext()) {
                
                OwsValueRestriction owsValueRestriction = (OwsValueRestriction) valueIterator.next();
                
                if(owsValueRestriction.isValue()) {
                    allowedValues.add(owsValueRestriction.asValue().getValue());
                } else if(owsValueRestriction.isRange()) {
                    Range serializedRange = new Range();
                    OwsRange range = owsValueRestriction.asRange();
                    
                    Optional<OwsValue> lowerBound = range.getLowerBound();
                    if(lowerBound.isPresent()) {
                        serializedRange.setMinimumValue(lowerBound.get().getValue());
                    }
                    
                    Optional<OwsValue> upperBound = range.getLowerBound();
                    if(upperBound.isPresent()) {
                        serializedRange.setMaximumValue(upperBound.get().getValue());
                    }
                    
                    Optional<OwsValue> spacing = range.getSpacing();
                    if(spacing.isPresent()) {
                        serializedRange.setSpacing(spacing.get().getValue());
                    }
                    
                    createBoundType(range, serializedRange);
                    
                    allowedValues.add(owsValueRestriction.asValue().getValue());
                }
            }
            
            return allowedValues;
        }
        
        throw new IllegalArgumentException("No value definition present." + defaultLiteralDataDomain);
    }

    private void createBoundType(OwsRange range,
            Range serializedRange) {
        
        BoundType lowerBoundType = range.getLowerBoundType();
        BoundType upperBoundType = range.getUpperBoundType();
        
        RangeClosureEnum rangeClosureEnum = null;
        
        switch (lowerBoundType) {
        case CLOSED:
            switch (upperBoundType) {
            case CLOSED:
                rangeClosureEnum = RangeClosureEnum.CLOSED;
                break;
            case OPEN:
                rangeClosureEnum = RangeClosureEnum.CLOSED_OPEN;
                break;
            default:
                break;
            }
            break;
        case OPEN:
            switch (upperBoundType) {
            case CLOSED:
                rangeClosureEnum = RangeClosureEnum.OPEN_CLOSED;
                break;
            case OPEN:
                rangeClosureEnum = RangeClosureEnum.OPEN;
                break;
            default:
                break;
            }
        default:
            break;
        }
        if(rangeClosureEnum != null) {
            serializedRange.setRangeClosure(rangeClosureEnum);
        }
        
    }

    private List<FormatDescription> createFormats(Format defaultFormat,
            Set<Format> supportedFormats, BigInteger maximumMegabytes) {
        
        List<FormatDescription> formatDescriptions = new ArrayList<>();
        
        FormatDescription defaultFormatDescription = new FormatDescription();
        
        defaultFormatDescription.setDefault(true);
        
        addFormat(defaultFormatDescription, defaultFormat, maximumMegabytes);
        
        formatDescriptions.add(defaultFormatDescription);
        
        for (Format format : supportedFormats) {
            
            FormatDescription supportedFormatDescription = new FormatDescription();
            
            addFormat(supportedFormatDescription, format, maximumMegabytes);
            
            formatDescriptions.add(supportedFormatDescription);
            
        }
        
        return formatDescriptions;
    }

    private void addFormat(FormatDescription formatDescription,
            Format format, BigInteger maximumMegabytes) {
        
        try {
            formatDescription.setMimeType(format.getMimeType().get());            
        } catch (Exception e) {
            log.error("MIME type not present.");
        }
        try {
            formatDescription.setSchema(format.getSchema().get());            
        } catch (Exception e) {
            log.trace("Schema not present.");
        }
        try {
            formatDescription.setEncoding(format.getEncoding().get());            
        } catch (Exception e) {
            log.trace("Encoding not present.");
        }
        if(maximumMegabytes != null) {
            try {
                formatDescription.setMaximumMegabytes(Integer.parseInt(""+maximumMegabytes));
            } catch (Exception e) {
                log.trace("Could not parse BigInteger: " + maximumMegabytes);
            }
        }
    }

    public ProcessCollection serializeProcessOfferings(Set<org.n52.shetland.ogc.wps.ProcessOffering> offerings) {
        ProcessCollection collection = new ProcessCollection();
        
      List<ProcessSummary> processes = new ArrayList<>();
      
      ProcessSummary process;
      
      for (org.n52.shetland.ogc.wps.ProcessOffering processOffering : offerings) {
          process = new ProcessSummary();
           
          addProcessSummary(process, processOffering);
          
          processes.add(process);
          
      }
      
      collection.setProcesses(processes);
        
        return collection;
    }
    
}
