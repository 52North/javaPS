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

import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.ogc.wps.WPS200Constants;
import org.n52.iceland.ogc.wps.WPSConstants;
import org.n52.javaps.request.GetStatusRequest;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetStatusKvpDecoder extends AbstractJobIdKvpDecoder<GetStatusRequest> {
    private static final DecoderKey KEY
            = createKey(WPSConstants.SERVICE, WPS200Constants.VERSION, WPSConstants.Operations.GetStatus);

    @Override
    protected GetStatusRequest createRequest() {
        return new GetStatusRequest();
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
