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

import static java.util.Collections.singletonMap;

import java.util.Map;
import java.util.Set;

import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.coding.encode.Encoder;
import org.n52.iceland.coding.encode.EncoderKey;
import org.n52.iceland.coding.encode.OperationResponseEncoderKey;
import org.n52.iceland.coding.encode.XmlEncoderKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.iceland.ogc.ows.OWSConstants.HelperValues;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;
import org.n52.javaps.algorithm.ProcessDescription;
import org.n52.javaps.ogc.wps.WPS200Constants;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.DescribeProcessResponse;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class DescribeProcessResponseEncoder implements
Encoder<DescribeProcessResponse, ProcessDescription>{

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS = singletonMap(
            WPSConstants.SERVICE,
            singletonMap(WPS200Constants.SERVICEVERSION,
                    (Set<String>) ImmutableSet.of(MediaTypes.TEXT_XML.toString())));

    private static final Set<EncoderKey> ENCODER_KEYS = ImmutableSet.of(new XmlEncoderKey(WPS200Constants.NS_WPS2, DescribeProcessResponse.class));

    @Override
    public Set<EncoderKey> getKeys() {
        OperationKey key = new OperationKey(WPSConstants.SERVICE, WPSConstants.VERSION_200, WPSConstants.Operations.DescribeProcess);
        Set<EncoderKey> encoderKeys =
                Sets.newHashSet(new OperationResponseEncoderKey(key, MediaTypes.APPLICATION_XML), new OperationResponseEncoderKey(key, MediaTypes.TEXT_XML));
        return encoderKeys;
    }

    @Override
    public DescribeProcessResponse encode(ProcessDescription objectToEncode) throws OwsExceptionReport, UnsupportedEncoderInputException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DescribeProcessResponse encode(ProcessDescription objectToEncode,
            Map<HelperValues, String> additionalValues) throws OwsExceptionReport, UnsupportedEncoderInputException {
        return encode(objectToEncode);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_XML;
    }

}
