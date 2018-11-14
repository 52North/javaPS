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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.model.BoundingBoxInputType;
import io.swagger.model.ComplexInputType;
import io.swagger.model.DataDescriptionType;
import io.swagger.model.FormatDescription;
import io.swagger.model.LiteralDataDomain;
import io.swagger.model.LiteralDataDomainValueDefinition;
import io.swagger.model.LiteralInputType;
import io.swagger.model.Process;
import io.swagger.model.ProcessOffering;

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
        
        ProcessDescription processDescription = processOffering.getProcessDescription();
        
        String id = processDescription.getId().getValue();
        
        io.swagger.model.ProcessOffering serializedProcessOffering = new io.swagger.model.ProcessOffering();
        
        Process process = new Process();
        
        process.setId(id);
        
        process.setExecuteEndpoint(serviceURL + id + "/jobs");
        
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
        //TODO keywords
        
        process.setInputs(createInputs(processDescription.getInputDescriptions()));
        
        serializedProcessOffering.process(process);
        
        return serializedProcessOffering;
    }

    private List<Object> createInputs(Collection<? extends ProcessInputDescription> inputDescriptions) {
        
        List<Object> inputs = new ArrayList<>();
        
        for (ProcessInputDescription processInputDescription : inputDescriptions) {
            
            DataDescriptionType descriptionType = null;
            
            if(processInputDescription.isComplex()) {
                ComplexInputDescription complexInputDescription = processInputDescription.asComplex();
                descriptionType = new ComplexInputType();
                
                ((ComplexInputType)descriptionType).setFormats(createFormats(complexInputDescription.getDefaultFormat(), complexInputDescription.getSupportedFormats()));
                
            } else if(processInputDescription.isLiteral()) {
                descriptionType = new LiteralInputType();
                
                LiteralInputDescription literalInputDescription = processInputDescription.asLiteral();
                
                ((LiteralInputType)descriptionType).setLiteralDataDomain(serializeLiteralDataDomain(literalInputDescription.getDefaultLiteralDataDomain()));//TODO what about the supported domains?
                
            } else if(processInputDescription.isBoundingBox()) {
                descriptionType = new BoundingBoxInputType();
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

    private LiteralDataDomain serializeLiteralDataDomain(
            org.n52.shetland.ogc.wps.description.LiteralDataDomain defaultLiteralDataDomain) {
        
        LiteralDataDomain domain = new LiteralDataDomain();
        
        try {
            domain.setDataType(defaultLiteralDataDomain.getDataType().get().getValue().get());
        } catch (Exception e) {
            log.info("No data type present.");
        }
        
        domain.setValueDefinition(serializeValueDefinition(defaultLiteralDataDomain));
        
        return domain;
    }

    private LiteralDataDomainValueDefinition serializeValueDefinition(
            org.n52.shetland.ogc.wps.description.LiteralDataDomain defaultLiteralDataDomain) {
        
        LiteralDataDomainValueDefinition literalDataDomainValueDefinition = new LiteralDataDomainValueDefinition();
        
        Optional<OwsValue> defaultValue = defaultLiteralDataDomain.getDefaultValue();
        
        if(defaultValue.isPresent()) {
            literalDataDomainValueDefinition.setDefaultValue(defaultValue.get().getValue());//TODO range possible here?
        }
        
        return literalDataDomainValueDefinition;
    }

    private List<FormatDescription> createFormats(Format defaultFormat,
            Set<Format> supportedFormats) {
        
        List<FormatDescription> formatDescriptions = new ArrayList<>();
        
        FormatDescription defaultFormatDescription = new FormatDescription();
        
        defaultFormatDescription.setDefault(true);
        
        addFormat(defaultFormatDescription, defaultFormat);
        
        formatDescriptions.add(defaultFormatDescription);
        
        for (Format format : supportedFormats) {
            
            FormatDescription supportedFormatDescription = new FormatDescription();
            
            addFormat(supportedFormatDescription, format);
            
            formatDescriptions.add(supportedFormatDescription);
            
        }
        
        return formatDescriptions;
    }

    private void addFormat(FormatDescription formatDescription,
            Format format) {
        
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
    }
    
}
