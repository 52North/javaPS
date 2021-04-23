/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import java.util.Collection;
import java.util.function.Supplier;

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.shetland.ogc.wps.request.AbstractJobIdRequest;
import org.n52.svalbard.decode.DecoderKey;

public abstract class AbstractJobIdKvpDecoder<T extends AbstractJobIdRequest> extends AbstractKvpDecoder<T> {

    private static final String JOB_ID = "JobId";

    public AbstractJobIdKvpDecoder(Supplier<? extends T> supplier, String service, String version, String operation) {
        super(supplier, service, version, operation);
    }

    public AbstractJobIdKvpDecoder(Supplier<? extends T> supplier, String service, String version, Enum<?> operation) {
        super(supplier, service, version, operation);
    }

    public AbstractJobIdKvpDecoder(Supplier<? extends T> supplier, DecoderKey... keys) {
        super(supplier, keys);
    }

    public AbstractJobIdKvpDecoder(Supplier<? extends T> supplier, Collection<? extends DecoderKey> keys) {
        super(supplier, keys);
    }

    @Override
    protected void getRequestParameterDefinitions(Builder<T> builder) {
        builder.add(JOB_ID, AbstractJobIdRequest::setJobId);
    }

}
