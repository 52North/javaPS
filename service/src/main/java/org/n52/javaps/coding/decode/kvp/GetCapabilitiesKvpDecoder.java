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

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.util.KvpHelper;
import org.n52.iceland.ogc.wps.WPS200Constants;
import org.n52.iceland.ogc.wps.WPSConstants;

import com.google.common.collect.Sets;

public class GetCapabilitiesKvpDecoder extends AbstractKvpDecoder<GetCapabilitiesRequest> {

    private static final Set<DecoderKey> KEYS = Sets.newHashSet(
            createKey(WPSConstants.SERVICE, null, OWSConstants.Operations.GetCapabilities),
            createKey(WPSConstants.SERVICE, WPS200Constants.VERSION, OWSConstants.Operations.GetCapabilities));

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
                request.setAcceptVersions(KvpHelper.checkParameterMultipleValues(value, name));
                break;
            case "acceptformats":
                request.setAcceptFormats(KvpHelper.checkParameterMultipleValues(value, name));
                break;
            case "updatesequence":
                request.setUpdateSequence(KvpHelper.checkParameterSingleValue(value, name));
                break;
            case "sections":
                request.setSections(KvpHelper.checkParameterMultipleValues(value, name));
                break;
            case "acceptlanguages":
                request.setAcceptLanguages(KvpHelper.checkParameterMultipleValues(value, name));
                break;
            default:
                throw unsupportedParameter(name, value);
        }
    }

}
