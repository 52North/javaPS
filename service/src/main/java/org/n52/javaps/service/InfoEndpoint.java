/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.iceland.util.JSONUtils;
import org.n52.iceland.util.Validation;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

/**
 * @author Daniel Nüst
 * @author Christian Autermann
 */
@Configurable
@Controller
@RequestMapping("/info")
public class InfoEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(InfoEndpoint.class);
    private static final String GIT_PROPERTIES_FILE = "/git.properties";
    private static final String VERSION_PROPERTIES_FILE = "/version.properties";
    private static final String TIME_PROPERTY = "git.build.time";
    private static final String COMMIT_PROPERTY = "git.commit.id";
    private static final String BRANCH_PROPERTY = "git.branch";
    private static final String VERSION_PROPERTY = "build.version";
    private static final String ENDPOINT = "endpoint";
    private static final String BRANCH = "branch";
    private static final String COMMIT = "commit";
    private static final String TIME = "time";
    private static final String VERSION = "version";
    private final String branch;
    private final String commit;
    private final String time;
    private final String version;
    private String serviceURL;

    public InfoEndpoint() {
        Properties gitProperties = loadProperties(GIT_PROPERTIES_FILE);
        this.time = gitProperties.getProperty(TIME_PROPERTY);
        this.commit = gitProperties.getProperty(COMMIT_PROPERTY);
        this.branch = gitProperties.getProperty(BRANCH_PROPERTY);
        Properties versionProperties = loadProperties(VERSION_PROPERTIES_FILE);
        this.version = versionProperties.getProperty(VERSION_PROPERTY);

        LOG.info("Created endpoint");
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String info() {

        return JSONUtils.print(JSONUtils.nodeFactory().objectNode()
                .put(ENDPOINT, this.serviceURL)
                .put(BRANCH, this.branch)
                .put(COMMIT, this.commit)
                .put(TIME, this.time)
                .put(VERSION, this.version));
    }

    private static Properties loadProperties(String path) {
        URL url = Resources.getResource(path);
        ByteSource source = Resources.asByteSource(url);
        Properties properties = new Properties();
        try (InputStream in = source.openStream()) {
            LOG.info("Loading git properties from {}", url);
            properties.load(in);
        } catch (IOException e) {
            throw new Error("Could not load properties", e);
        }
        return properties;
    }

}
