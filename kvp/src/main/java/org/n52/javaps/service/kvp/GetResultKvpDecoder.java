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
package org.n52.javaps.service.kvp;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.wps.WPS200Constants;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.wps.request.GetResultRequest;
import org.n52.svalbard.decode.OperationDecoderKey;

/**
 * @author Christian Autermann
 */
public class GetResultKvpDecoder extends AbstractJobIdKvpDecoder<GetResultRequest> {

    public GetResultKvpDecoder() {
        super(GetResultRequest::new, new OperationDecoderKey(WPSConstants.SERVICE, WPS200Constants.VERSION,
                WPSConstants.Operations.GetResult.name(), MediaTypes.APPLICATION_KVP), new OperationDecoderKey(
                        WPSConstants.SERVICE, null, WPSConstants.Operations.GetResult.name(),
                        MediaTypes.APPLICATION_KVP));
    }
}
