/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.stream;

import java.util.Collections;
import java.util.Set;

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.ExceptionEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * Helper class to wrap a thrown {@link OwsExceptionReport} into a
 * {@link OwsServiceResponse}.
 *
 * @author Christian Autermann
 */
public class OwsExceptionReportEncoder implements Encoder<OwsExceptionReportResponse, OwsExceptionReport> {
    private static final ExceptionEncoderKey KEY = new ExceptionEncoderKey(MediaTypes.APPLICATION_XML);
    private static final Set<EncoderKey> KEYS = Collections.singleton(KEY);
    private final MediaType contentType = MediaTypes.APPLICATION_XML;

    @Override
    public OwsExceptionReportResponse encode(OwsExceptionReport report) {
        OwsExceptionReportResponse response = new OwsExceptionReportResponse(report);
        response.setContentType(this.contentType);
        return response;
    }

    @Override
    public OwsExceptionReportResponse encode(OwsExceptionReport objectToEncode, EncodingContext additionalValues)
            throws EncodingException {
        return encode(objectToEncode);
    }

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return KEYS;
    }

}
