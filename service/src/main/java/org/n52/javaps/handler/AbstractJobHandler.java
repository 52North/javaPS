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

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsAnyValue;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsNoValues;
import org.n52.iceland.ogc.ows.OwsPossibleValues;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.javaps.Engine;
import org.n52.iceland.ogc.wps.JobId;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractJobHandler extends AbstractEngineHandler {
    private static final String JOB_ID = "JobId";

    private final boolean discloseJobIds;

    public AbstractJobHandler(Engine engine, boolean discloseJobIds) {
        super(engine);
        this.discloseJobIds = discloseJobIds;
    }

    @Override
    protected Set<OwsDomain> getOperationParameters(String service, String version) {
        OwsPossibleValues allowedValues;
        if (discloseJobIds) {
            Set<OwsValue> values = getEngine().getJobIdentifiers().stream().map(JobId::getValue).map(OwsValue::new).collect(toSet());
            if (values.isEmpty()) {
                allowedValues = OwsNoValues.instance();
            } else {
                allowedValues = new OwsAllowedValues(values);
            }
        } else {
            allowedValues = OwsAnyValue.instance();
        }
        return Collections.singleton(new OwsDomain(JOB_ID, allowedValues));
    }
}
