/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.service.kvp;

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.shetland.ogc.wps.WPS200Constants;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.OWSConstants.GetCapabilitiesParams;
import org.n52.svalbard.decode.OperationDecoderKey;

public class GetCapabilitiesKvpDecoder extends AbstractKvpDecoder<GetCapabilitiesRequest> {

    public GetCapabilitiesKvpDecoder() {
        super(GetCapabilitiesRequest::new, new OperationDecoderKey(WPSConstants.SERVICE, null,
                OWSConstants.Operations.GetCapabilities.name(), MediaTypes.APPLICATION_KVP), new OperationDecoderKey(
                        WPSConstants.SERVICE, WPS200Constants.VERSION, OWSConstants.Operations.GetCapabilities.name(),
                        MediaTypes.APPLICATION_KVP), new OperationDecoderKey(null, WPS200Constants.VERSION,
                                OWSConstants.Operations.GetCapabilities.name(), MediaTypes.APPLICATION_KVP),
                // FIXME isn't this the only one needed?
                new OperationDecoderKey(null, null, OWSConstants.Operations.GetCapabilities.name(),
                        MediaTypes.APPLICATION_KVP));
    }

    @Override
    protected void getRequestParameterDefinitions(Builder<GetCapabilitiesRequest> builder) {
        builder.add(GetCapabilitiesParams.Sections, decodeList(GetCapabilitiesRequest::setSections));
        builder.add(GetCapabilitiesParams.updateSequence, GetCapabilitiesRequest::setUpdateSequence);
        builder.add(GetCapabilitiesParams.AcceptFormats, decodeList(GetCapabilitiesRequest::setAcceptFormats));
        builder.add(GetCapabilitiesParams.AcceptVersions, decodeList(GetCapabilitiesRequest::setAcceptVersions));
        builder.add(GetCapabilitiesParams.AcceptLanguages, decodeList(GetCapabilitiesRequest::setAcceptLanguages));
    }

}
