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
package org.n52.javaps.coding.decode.kvp;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.MissingParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.util.KvpHelper;
import org.n52.javaps.ogc.wps.WPS200Constants;
import org.n52.javaps.ogc.wps.WPSConstants;

import com.google.common.collect.Sets;

public class GetCapabilitiesKvpDecoder extends AbstractKvpDecoder<GetCapabilitiesRequest> {

    private static final Set<DecoderKey> KEYS = Sets.newHashSet(
            createKey(WPSConstants.SERVICE, null, OWSConstants.Operations.GetCapabilities.toString()),
            createKey(WPSConstants.SERVICE, WPS200Constants.SERVICEVERSION, OWSConstants.Operations.GetCapabilities.toString()));

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    protected GetCapabilitiesRequest createRequest() {
        return new GetCapabilitiesRequest(WPSConstants.SERVICE);
    }

    @Override
    protected void decodeParameter(GetCapabilitiesRequest request, String name, String value) throws OwsExceptionReport {
        switch (name.toLowerCase()) {
            case "service":
                request.setService(KvpHelper.checkParameterSingleValue(value, name));
                break;
            case "request":
                KvpHelper.checkParameterSingleValue(value, name);
                break;
            case "acceptversions":
                if (value.isEmpty()) {
                    throw new MissingParameterValueException(name);
                }
                request.setAcceptVersions(Arrays.asList(value.split(",")));
                break;
//            case ACCEPT_FORMATS:
//                request.setAcceptFormats(KvpHelper.checkParameterMultipleValues(values, name));
//                break;
//            case UPDATE_SEQUENCE:
//                request.setUpdateSequence(KvpHelper.checkParameterSingleValue(values, name));
//                break;
//            case SECTIONS:
//                request.setSections(KvpHelper.checkParameterMultipleValues(values, name));
//                break;
//            case LANGUAGE:
//                Extension<String> le = new LanguageExtension(KvpHelper.checkParameterSingleValue(values, name));
//                request.addExtension(le);
//                break;
            default:
                throw new InvalidParameterValueException(name, value)
                        .withMessage("The parameter '%s' is not supported.", name);
        }
    }

}
