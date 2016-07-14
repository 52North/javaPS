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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.ows.OwsExceptionCode;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.response.OwsExceptionReportResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Ows20ExceptionReportWriter extends AbstractXmlStreamWriter<OwsExceptionReportResponse> {
    private static final String AN_EXCEPTION_CODE = "exceptionCode";
    private static final String AN_LOCATOR = "locator";
    private static final String EN_EXCEPTION = "Exception";
    private static final String EN_EXCEPTION_REPORT = "ExceptionReport";
    private static final String EN_EXCEPTION_TEXT = "ExceptionText";
    private static final String EN_VERSION = "version";
    private static final String NS_OWS_20 = "http://www.opengis.net/ows/2.0";
    private static final String NS_OWS_PREFIX = "ows";//TODO setting
    private static final QName QN_EXCEPTION
            = new QName(NS_OWS_20, EN_EXCEPTION, NS_OWS_PREFIX);
    private static final QName QN_EXCEPTION_REPORT
            = new QName(NS_OWS_20, EN_EXCEPTION_REPORT, NS_OWS_PREFIX);
    private static final QName QN_EXCEPTION_TEXT
            = new QName(NS_OWS_20, EN_EXCEPTION_TEXT, NS_OWS_PREFIX);
    public static final StreamWriterKey KEY
            = new XmlStreamWriterKey(OwsExceptionReportResponse.class);
    private boolean includeStackTraceInExceptionReport = true;

    @Override
    protected void write(OwsExceptionReportResponse response)
            throws XMLStreamException {
        OwsExceptionReport report = response.getOwsExceptionReport();
        start(QN_EXCEPTION_REPORT);
        namespace(NS_OWS_PREFIX, NS_OWS_20);
        attr(EN_VERSION, report.getVersion());
        for (CodedException exception : report.getExceptions()) {
            writeCodedException(exception);
        }
        end(QN_EXCEPTION_REPORT);
    }

    private void writeCodedException(CodedException exception)
            throws XMLStreamException {
        start(QN_EXCEPTION);
        if (exception.getLocator() != null) {
            attr(AN_LOCATOR, exception.getLocator());
        }
        if (exception.getCode() != null) {
            attr(AN_EXCEPTION_CODE, exception.getCode().toString());
        } else {
            attr(AN_EXCEPTION_CODE, OwsExceptionCode.NoApplicableCode.toString());
        }
        if (exception.getMessage() != null) {
            start(QN_EXCEPTION_TEXT);
            chars(exception.getMessage());
            end(QN_EXCEPTION_TEXT);
        }
        if (includeStackTraceInExceptionReport) {
            start(QN_EXCEPTION_TEXT);
            chars("[EXCEPTION]: \n");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exception.printStackTrace(new PrintStream(os));
            chars(os.toString());
            end(QN_EXCEPTION_TEXT);
        }
        end(QN_EXCEPTION);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
