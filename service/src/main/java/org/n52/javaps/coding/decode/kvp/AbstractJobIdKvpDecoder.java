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

import org.n52.iceland.binding.kvp.AbstractKvpDecoder;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.util.KvpHelper;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.request.AbstractJobIdRequest;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractJobIdKvpDecoder<T extends AbstractJobIdRequest<?>> extends AbstractKvpDecoder<T> {

    @Override
    protected void decodeParameter(T request, String name, String value) throws OwsExceptionReport {
        switch (name.toLowerCase()) {
            case "service":
                request.setService(KvpHelper.checkParameterSingleValue(value, name));
                break;
            case "version":
                request.setVersion(KvpHelper.checkParameterSingleValue(value, name));
                break;
            case "request":
                KvpHelper.checkParameterSingleValue(value, name);
                break;
            case "jobid":
                request.setJobId(new JobId(KvpHelper.checkParameterSingleValue(value, name)));
                break;
            default:
                throw unsupportedParameter(name, value);
        }
    }
}
