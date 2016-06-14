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

import javax.xml.stream.XMLStreamException;

import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.ows.OwsExceptionCode;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Attributes;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Entities;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Namespaces;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.QNames;
import org.n52.javaps.response.OwsExceptionReportResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Ows20ExceptionReportWriter extends AbstractXmlStreamWriter<OwsExceptionReportResponse> {
    public static final StreamWriterKey KEY = new XmlStreamWriterKey(OwsExceptionReportResponse.class);
    private final boolean includeStackTraceInExceptionReport = true;

    @Override
    protected void write(OwsExceptionReportResponse response)
            throws XMLStreamException {
        OwsExceptionReport report = response.getOwsExceptionReport();
        start(QNames.OWS_EXCEPTION_REPORT);
        namespace(Namespaces.OWS_PREFIX, Namespaces.OWS_20);
        attr(Entities.OWS_VERSION, report.getVersion());
        for (CodedException exception : report.getExceptions()) {
            writeCodedException(exception);
        }
        end(QNames.OWS_EXCEPTION_REPORT);
    }

    private void writeCodedException(CodedException exception)
            throws XMLStreamException {
        start(QNames.OWS_EXCEPTION);
        if (exception.getLocator() != null) {
            attr(Attributes.OWS_LOCATOR, exception.getLocator());
        }
        if (exception.getCode() != null) {
            attr(Attributes.OWS_EXCEPTION_CODE, exception.getCode().toString());
        } else {
            attr(Attributes.OWS_EXCEPTION_CODE, OwsExceptionCode.NoApplicableCode.toString());
        }
        if (exception.getMessage() != null) {
            start(QNames.OWS_EXCEPTION_TEXT);
            chars(exception.getMessage());
            end(QNames.OWS_EXCEPTION_TEXT);
        }
        if (includeStackTraceInExceptionReport) {
            start(QNames.OWS_EXCEPTION_TEXT);
            chars("[EXEPTION]: \n");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exception.printStackTrace(new PrintStream(os));
            chars(os.toString());
            end(QNames.OWS_EXCEPTION_TEXT);
        }
        end(QNames.OWS_EXCEPTION);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
