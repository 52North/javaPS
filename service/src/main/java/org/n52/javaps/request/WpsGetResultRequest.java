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
package org.n52.javaps.request;


import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.GetResultResponse;

/**
 * @author Christian Autermann
 */
public class GetResultRequest extends AbstractServiceRequest<GetResultResponse> {

    @Override
    public GetResultResponse getResponse()
            throws OwsExceptionReport {
        return (GetResultResponse) new GetResultResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetResult.toString();
    }

}
