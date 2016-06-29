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
package org.n52.javaps.handler;


import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.ds.GenericOperationHandler;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.Engine;
import org.n52.javaps.ogc.wps.JobId;
import org.n52.javaps.ogc.wps.StatusInfo;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.request.DismissRequest;
import org.n52.javaps.response.DismissResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DismissHandler extends AbstractJobHandler
        implements GenericOperationHandler<DismissRequest, DismissResponse> {
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.Dismiss);

    @Inject
    public DismissHandler(Engine engine) {
        super(engine, true);
    }

    @Override
    public DismissResponse handle(DismissRequest request)
            throws OwsExceptionReport {
        JobId jobId = request.getJobId();
        StatusInfo status = getEngine().dismiss(jobId);
        String service = request.getService();
        String version = request.getVersion();
        return new DismissResponse(service, version,status);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Dismiss.toString();
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
