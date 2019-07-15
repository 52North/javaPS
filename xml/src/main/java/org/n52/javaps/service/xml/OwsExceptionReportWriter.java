/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Optional;

import javax.xml.stream.XMLStreamException;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.stream.OwsExceptionReportResponse;
import org.n52.svalbard.encode.stream.xml.AbstractSingleElementXmlStreamWriter;

import com.google.common.base.Charsets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Configurable
public class OwsExceptionReportWriter extends AbstractSingleElementXmlStreamWriter<OwsExceptionReportResponse> {
    private boolean includeStackTraceInExceptionReport;

    public OwsExceptionReportWriter() {
        super(OwsExceptionReportResponse.class);
    }

    @Setting("misc.includeStackTraceInExceptionReport")
    public void setIncludeStackTraceInExceptionReport(boolean includeStackTraceInExceptionReport) {
        this.includeStackTraceInExceptionReport = includeStackTraceInExceptionReport;
    }

    @Override
    public void write(OwsExceptionReportResponse response) throws XMLStreamException {
        element(OWSConstants.Elem.QN_EXCEPTION_REPORT, response.getOwsExceptionReport(), report -> {
            namespace(OWSConstants.NS_OWS_PREFIX, OWSConstants.NS_OWS);
            schemaLocation(Collections.singleton(new SchemaLocation(OWSConstants.NS_OWS,
                    XMLSchemaConstants.OWS20_SCHEMALOCTION)));
            attr(OWSConstants.Attr.AN_VERSION, report.getVersion());
            for (CodedException exception : report.getExceptions()) {
                element(OWSConstants.Elem.QN_EXCEPTION, exception, x -> {
                    attr(OWSConstants.Attr.AN_LOCATOR, Optional.ofNullable(x.getLocator()));
                    attr(OWSConstants.Attr.AN_EXCEPTION_CODE, Optional.ofNullable(x.getCode()).orElse(
                            OwsExceptionCode.NoApplicableCode).toString());
                    element(OWSConstants.Elem.QN_EXCEPTION_TEXT, Optional.ofNullable(x.getMessage()));
                    if (includeStackTraceInExceptionReport) {
                        element(OWSConstants.Elem.QN_EXCEPTION_TEXT, x, ex -> {
                            chars("[EXCEPTION]: \n");
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            try {
                            ex.printStackTrace(new PrintStream(os, false, Charsets.UTF_8.name()));
                                chars(os.toString(Charsets.UTF_8.name()));
                            } catch (UnsupportedEncodingException e) {
                                // do nothing
                            }
                        });
                    }
                });
            }
        });
    }
}
