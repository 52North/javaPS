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
package org.n52.javaps.ogc.wps;

import org.n52.iceland.w3c.SchemaLocation;

/**
 * @author Christian Autermann
 */
public interface WPS200Constants {

    String NS_WPS2 = "http://www.opengis.net/wps/2.0";

    /** Constant for the schema repository of the WPS */
    String SCHEMA_LOCATION_WPS2 = "http://schemas.opengis.net/wps/2.0/wps.xsd";

    SchemaLocation WPS2_SCHEMA_LOCATION = new SchemaLocation(NS_WPS2, SCHEMA_LOCATION_WPS2);

    String SERVICEVERSION = "2.0.0";

    String JOB_CONTROL_OPTION_SYNC_EXECUTE = "sync-execute";
    String JOB_CONTROL_OPTION_ASYNC_EXECUTE = "async-execute";

    String JOB_CONTROL_OPTIONS_SEPARATOR = " ";

    String OUTPUT_TRANSMISSION_VALUE = "value";
    String OUTPUT_TRANSMISSION_REFERENCE = "reference";

    String OUTPUT_TRANSMISSIONS_SEPARATOR = " ";
}
