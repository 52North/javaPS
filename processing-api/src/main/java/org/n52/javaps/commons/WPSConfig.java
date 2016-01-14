/**
 * ﻿Copyright (C) 2006 - 2014 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.commons;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Benjamin Pross, Daniel Nüst
 *
 */
public class WPSConfig implements Serializable {

    private static final long serialVersionUID = 3198223084611936675L;
    private static transient WPSConfig wpsConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(WPSConfig.class);

    // constants for the Property change event names
    public static final String WPSCONFIG_PROPERTY_EVENT_NAME = "WPSConfigUpdate";
    public static final String WPSCAPABILITIES_SKELETON_PROPERTY_EVENT_NAME = "WPSCapabilitiesUpdate";
    public static final String CONFIG_FILE_PROPERTY = "wps.config.file";
    public static final String CONFIG_FILE_NAME = "wps_config.xml";
    private static final String CONFIG_FILE_DIR = "WEB-INF" + File.separator + "config";
    private static final String URL_DECODE_ENCODING = "UTF-8";
    // FvK: added Property Change support
    protected final PropertyChangeSupport propertyChangeSupport = null;

    public static final String SERVLET_PATH = "WebProcessingService";
	public static final String VERSION_100 = "1.0.0";
	public static final String VERSION_200 = "2.0.0";
	public static final List<String> SUPPORTED_VERSIONS = Arrays.asList(new String[]{VERSION_100, VERSION_200});

	public static final String JOB_CONTROL_OPTION_SYNC_EXECUTE = "sync-execute";
	public static final String JOB_CONTROL_OPTION_ASYNC_EXECUTE = "async-execute";

	public static final String JOB_CONTROL_OPTIONS_SEPARATOR = " ";

	public static final String OUTPUT_TRANSMISSION_VALUE = "value";
	public static final String OUTPUT_TRANSMISSION_REFERENCE = "reference";

	public static final String OUTPUT_TRANSMISSIONS_SEPARATOR = " ";

    /**
     * returns an instance of the WPSConfig class. WPSConfig is a single. If there is need for
     * reinstantitation, use forceInitialization().
     *
     * @return WPSConfig object representing the wps_config.xml from the classpath or webapps folder
     */
    public static WPSConfig getInstance() {
        if (wpsConfig == null) {
        	wpsConfig = new WPSConfig();
        }
        return wpsConfig;
    }

    public WPSConfig getWPSConfig() {
        return wpsConfig;
    }

    public String getServiceBaseUrl() {
        return "";
    }

    public String getServiceEndpoint() {
        String endpoint = getServiceBaseUrl() + "/" + SERVLET_PATH;
        return endpoint;
    }
}
