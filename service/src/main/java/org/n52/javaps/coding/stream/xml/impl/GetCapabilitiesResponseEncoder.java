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

import java.util.Map;
import java.util.Set;

import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.EncoderKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.iceland.ogc.ows.OWSConstants.HelperValues;
import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.iceland.util.http.MediaType;
import org.n52.javaps.ogc.wps.WPSCapabilities;

public class GetCapabilitiesResponseEncoder implements Encoder<GetCapabilitiesResponse, WPSCapabilities>{

    @Override
    public Set<EncoderKey> getKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetCapabilitiesResponse encode(WPSCapabilities objectToEncode) throws OwsExceptionReport, UnsupportedEncoderInputException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetCapabilitiesResponse encode(WPSCapabilities objectToEncode,
            Map<HelperValues, String> additionalValues) throws OwsExceptionReport, UnsupportedEncoderInputException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MediaType getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

}
