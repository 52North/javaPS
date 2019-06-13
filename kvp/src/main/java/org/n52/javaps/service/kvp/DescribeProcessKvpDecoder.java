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

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.janmayen.function.ThrowingBiConsumer;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.WPS200Constants;
import org.n52.shetland.ogc.wps.WPSConstants;
import org.n52.shetland.ogc.wps.request.DescribeProcessRequest;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

public class DescribeProcessKvpDecoder extends AbstractKvpDecoder<DescribeProcessRequest> {
    private static final String IDENTIFIER = "identifier";

    public DescribeProcessKvpDecoder() {
        super(DescribeProcessRequest::new, new OperationDecoderKey(WPSConstants.SERVICE, null,
                WPSConstants.Operations.DescribeProcess.name(), MediaTypes.APPLICATION_KVP), new OperationDecoderKey(
                        WPSConstants.SERVICE, WPS200Constants.VERSION, WPSConstants.Operations.DescribeProcess.name(),
                        MediaTypes.APPLICATION_KVP));
    }

    @Override
    protected void getRequestParameterDefinitions(Builder<DescribeProcessRequest> builder) {
        builder.add(IDENTIFIER, decodeList(DescribeProcessRequest::addProcessIdentifiers));
    }

    protected <T> ThrowingBiConsumer<T, String, DecodingException> asOwsCode(ThrowingBiConsumer<T, OwsCode,
            DecodingException> delegate) {
        return (t,
                u) -> delegate.accept(t, new OwsCode(u));
    }

}
