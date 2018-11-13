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
package org.n52.restfulwpsproxy.serializer.json;

import java.io.IOException;
import java.net.URI;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.restfulwpsproxy.serializer.util.Utils;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ProcessOfferings;
import org.n52.shetland.ogc.wps.request.ExecuteRequest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import net.opengis.ows.x20.DomainMetadataType;
import net.opengis.wps.x20.DataDescriptionType;
import net.opengis.wps.x20.FormatDocument;
import net.opengis.wps.x20.InputDescriptionType;
import net.opengis.wps.x20.LiteralDataType;
import net.opengis.wps.x20.OutputDescriptionType;
import net.opengis.wps.x20.ProcessDescriptionType;
import net.opengis.wps.x20.ProcessOfferingDocument;
import net.opengis.wps.x20.ProcessOfferingsDocument;

/**
 * TODO JavaDoc
 *
 * @author dewall
 */
@Configurable
public class WPSProcessesJsonModule extends AbstractWPSJsonModule {

    /**
     * 
     */
    private static final long serialVersionUID = 2097957550663121948L;

    private static final String PROCESS_OFFERING = "ProcessOffering";

    private static final String OUTPUT_TRANSMISSION = "outputTransmission";

    private static final String JOB_CONTROL_OPTIONS = "jobControlOptions";

    private static final String PROCESS_VERSION = "processVersion";

    private static final String PROCESS = "Process";

    private static final String OUTPUT = "Output";

    private static final String INPUT = "Input";

    private static final String IDENTIFIER = "Identifier";

    private static final String TITLE = "Title";

    private static final String MAX_OCCURS = "maxOccurs";

    private static final String MIN_OCCURS = "minOccurs";

    private static final String COMPLEX_DATA = "ComplexData";

    private static final String LITERAL_DATA = "LiteralData";

    private static final String LITERAL_DATA_DOMAIN = "LiteralDataDomain";

    private static final String FORMAT = "Format";

    private static final String MAXIMUM_MEGABYTES = "maximumMegabytes";

    private static final String SCHEMA = "schema";

    private static final String ENCODING = "encoding";

    private static final String MIME_TYPE = "mimeType";

    private static final String DEFAULT = "default";

    private static final String DATA_TYPE = "DataType";

    private static final String ALLOWED_VALUES = "AllowedValues";

    private static final String ANY_VALUE = "AnyValue";

    private static final String REFERENCE = "reference";

    private static final String EXECUTE_URL = "executeEndpoint";

    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url;
    }

    /**
     * Constructor.
     */
    public WPSProcessesJsonModule() {
        addSerializer(new ProcessOfferingSerializer());
        addSerializer(new ProcessOfferingsSerializer());
        addSerializer(new ProcessDescriptionSerializer());
        addSerializer(new InputDescriptionSerializer());
        addSerializer(new OutputDescriptionSerializer());
        addSerializer(new DataDescriptionSerializer());
        addSerializer(new FormatSerializer());
        addSerializer(new LiteralDataDomainSerializer());
        addSerializer(new DomainMetadataTypeSerializer());
    }

    private static final class ProcessOfferingsDocumentSerializer extends JsonSerializer<ProcessOfferingsDocument> {

        @Override
        public void serialize(ProcessOfferingsDocument t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {

        }

        @Override
        public Class<ProcessOfferingsDocument> handledType() {
            return ProcessOfferingsDocument.class;
        }

    }

    private static final class ExecuteDeserializer extends JsonDeserializer<ExecuteRequest> {

        @Override
        public ExecuteRequest deserialize(JsonParser jp,
                DeserializationContext dc) throws IOException, JsonProcessingException {

            ExecuteRequest request = new ExecuteRequest();

            while (jp.nextToken() != null) {
                switch (jp.getCurrentToken()) {
                case START_OBJECT:
                    if (jp.getCurrentName().equals("Execute")) {
//                        executeDocument.setExecute(jp.readValueAs(ExecuteRequestType.class));
                        
                    }
                    break;
                default:
                    break;
                }
            }

            return request;
        }

        @Override
        public Class<ExecuteRequest> handledType() {
            return ExecuteRequest.class;
        }
    }

    private static final class ProcessOfferingsSerializer
            extends JsonSerializer<ProcessOfferings> {

        @Override
        public void serialize(ProcessOfferings t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS_OFFERING, t.getOfferings().first());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferings> handledType() {
            return ProcessOfferings.class;
        }
    }

    private final class ProcessOfferingSerializer
            extends JsonSerializer<ProcessOffering> {

        @Override
        public void serialize(ProcessOffering t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
//            jg.writeObjectField(PROCESS, t.getProcess());
            jg.writeStringField(PROCESS_VERSION, t.getProcessVersion().get());
//            jg.writeStringField(JOB_CONTROL_OPTIONS, Utils.getStringFromObjectList(t.getJobControlOptions()));
//            jg.writeStringField(OUTPUT_TRANSMISSION, Utils.getStringFromObjectList(t.getOutputTransmissionModes()));
//            jg.writeStringField(EXECUTE_URL,
//                    serviceURL + "processes/" + t.getProcess().getIdentifier().getStringValue() + "/jobs");
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOffering> handledType() {
            return ProcessOffering.class;
        }
    }

    private static final class ProcessDescriptionSerializer extends JsonSerializer<ProcessDescriptionType> {

        @Override
        public void serialize(ProcessDescriptionType t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());
            writeArrayOfObjects(INPUT, t.getInputArray(), jg);
            writeArrayOfObjects(OUTPUT, t.getOutputArray(), jg);
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessDescriptionType> handledType() {
            return ProcessDescriptionType.class; // To change body of generated
                                                 // methods, choose Tools |
                                                 // Templates.
        }
    }

    private static final class InputDescriptionSerializer extends JsonSerializer<InputDescriptionType> {

        @Override
        public void serialize(InputDescriptionType t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());

            DataDescriptionType dataDescription = t.getDataDescription();
            if (dataDescription instanceof LiteralDataType) {
                jg.writeObjectField(LITERAL_DATA, t.getDataDescription());
            } else {
                jg.writeObjectField(COMPLEX_DATA, t.getDataDescription());
            }

            jg.writeStringField(MIN_OCCURS, t.getMinOccurs().toString());
            jg.writeStringField(MAX_OCCURS, t.getMaxOccurs().toString());
            jg.writeEndObject();
        }

        @Override
        public Class<InputDescriptionType> handledType() {
            return InputDescriptionType.class;
        }
    }

    private static final class OutputDescriptionSerializer extends JsonSerializer<OutputDescriptionType> {

        @Override
        public void serialize(OutputDescriptionType t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(TITLE, t.getTitleArray(0).getStringValue());
            jg.writeStringField(IDENTIFIER, t.getIdentifier().getStringValue());
            jg.writeObjectField(COMPLEX_DATA, t.getDataDescription());
            jg.writeEndObject();
        }

        @Override
        public Class<OutputDescriptionType> handledType() {
            return OutputDescriptionType.class;
        }

    }

    private static final class DataDescriptionSerializer extends JsonSerializer<DataDescriptionType> {

        @Override
        public void serialize(DataDescriptionType t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            writeArrayOfObjects(FORMAT, t.getFormatArray(), jg);
            if (t instanceof LiteralDataType) {
                writeArrayOfObjects(LITERAL_DATA_DOMAIN, ((LiteralDataType) t).getLiteralDataDomainArray(), jg);
            }
            jg.writeEndObject();
        }

        @Override
        public Class<DataDescriptionType> handledType() {
            return DataDescriptionType.class;
        }
    }

    private static final class FormatSerializer extends JsonSerializer<FormatDocument.Format> {

        @Override
        public void serialize(FormatDocument.Format t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(DEFAULT, "" + t.getDefault());
            jg.writeStringField(MIME_TYPE, t.getMimeType());
            writeStringFieldIfNotNull(jg, ENCODING, t.getEncoding());
            writeStringFieldIfNotNull(jg, SCHEMA, t.getSchema());
            writeStringFieldIfNotNull(jg, MAXIMUM_MEGABYTES, t.getMaximumMegabytes());
            jg.writeEndObject();
        }

        @Override
        public Class<FormatDocument.Format> handledType() {
            return FormatDocument.Format.class;
        }
    }

    private static final class LiteralDataDomainSerializer extends JsonSerializer<LiteralDataType.LiteralDataDomain> {

        @Override
        public void serialize(LiteralDataType.LiteralDataDomain t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(ANY_VALUE, t.getAnyValue() == null ? null : "");
            // jg.writeStringField(ALLOWED_VALUES, t.getAllowedValues() == null
            // ? null : "");
            // writeStringFieldIfNotNull(jg, ALLOWED_VALUES,
            // t.getAllowedValues());//TODO implement serializer
            jg.writeObjectField(DATA_TYPE, t.getDataType());
            jg.writeEndObject();
        }

        @Override
        public Class<LiteralDataType.LiteralDataDomain> handledType() {
            return LiteralDataType.LiteralDataDomain.class;
        }
    }

    private static final class DomainMetadataTypeSerializer extends JsonSerializer<DomainMetadataType> {

        @Override
        public void serialize(DomainMetadataType t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeStringField(REFERENCE, t.getReference());
            jg.writeEndObject();
        }

        @Override
        public Class<DomainMetadataType> handledType() {
            return DomainMetadataType.class;
        }
    }

    private static final class ProcessOfferingDeserializer extends JsonSerializer<ProcessOfferingDocument> {

        @Override
        public void serialize(ProcessOfferingDocument t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS_OFFERING, t.getProcessOffering());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingDocument> handledType() {
            return ProcessOfferingDocument.class;
        }
    }

    private static final class ProcessDeserializer extends JsonSerializer<ProcessOfferingDocument.ProcessOffering> {

        @Override
        public void serialize(ProcessOfferingDocument.ProcessOffering t,
                JsonGenerator jg,
                SerializerProvider sp) throws IOException, JsonProcessingException {
            jg.writeStartObject();
            jg.writeObjectField(PROCESS, t.getProcess());
            jg.writeEndObject();
        }

        @Override
        public Class<ProcessOfferingDocument.ProcessOffering> handledType() {
            return ProcessOfferingDocument.ProcessOffering.class;
        }
    }
}
