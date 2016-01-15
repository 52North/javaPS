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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.EncoderKey;
import org.n52.iceland.coding.encode.ExceptionEncoderKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.iceland.ogc.ows.OWSConstants.HelperValues;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;
import org.n52.javaps.response.OwsExceptionReportResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsExceptionReportEncoder implements
        Encoder<OwsExceptionReportResponse, OwsExceptionReport> {
    private final MediaType contentType = MediaTypes.APPLICATION_XML;

    @Override
    public OwsExceptionReportResponse encode(OwsExceptionReport objectToEncode)
            throws OwsExceptionReport, UnsupportedEncoderInputException {
        OwsExceptionReportResponse response = new OwsExceptionReportResponse(objectToEncode);
        response.setContentType(this.contentType);
        return response;
    }

    @Override
    public OwsExceptionReportResponse encode(OwsExceptionReport objectToEncode,
                                             Map<HelperValues, String> additionalValues)
            throws OwsExceptionReport, UnsupportedEncoderInputException {
        return encode(objectToEncode);
    }

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections
                .singleton(new ExceptionEncoderKey(MediaTypes.APPLICATION_XML));
    }


}
