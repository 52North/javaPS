/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.coding;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.SkeletonConstants;

import static org.n52.javaps.coding.KvpDecoder.createKey;

import org.n52.javaps.request.DemoRequest;
import org.n52.iceland.util.KvpHelper;

public class DemoKvpDecoderImpl extends KvpDecoder<DemoRequest> {

    private static final Set<DecoderKey> KEYS = Collections.unmodifiableSet(
            Sets.newHashSet(createKey(SkeletonConstants.SERVICE, SkeletonConstants.VERSION, SkeletonConstants.OPERATION_DEMO)));

    @Override
    protected DemoRequest createRequest() {
        return new DemoRequest(SkeletonConstants.VERSION);
    }

    @Override
    protected void decodeParameter(DemoRequest request, String name, String values) throws OwsExceptionReport {
        switch (name.toLowerCase()) {
            case SkeletonConstants.OperationParameter.service:
                request.setService(KvpHelper.checkParameterSingleValue(values, name));
                break;
            case SkeletonConstants.OperationParameter.version:
                request.setVersion(KvpHelper.checkParameterSingleValue(values, name));
                break;
            case SkeletonConstants.OperationParameter.request:
                KvpHelper.checkParameterSingleValue(values, name);
                break;
            case SkeletonConstants.DemoParameter.one:
                request.setOne(KvpHelper.checkParameterSingleValue(values, name));
                break;
            case SkeletonConstants.DemoParameter.two:
                String value = KvpHelper.checkParameterSingleValue(values, name);
                try {
                    int parsedInteger = Integer.parseInt(value);
                    request.setTwo(parsedInteger);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterValueException(name, values)
                            .withMessage("The parameter '%s' with value '%s' could not be parsed to a number.", name, value)
                            .causedBy(e);
                }
                break;
            default:
                throw new InvalidParameterValueException(name, values).withMessage("The parameter '%s' is not supported.", name); // OptionNotSupportedException (and thereby ParameterNotSupportedException) are OWS 1.1.0
        }
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return KEYS;
    }

}
