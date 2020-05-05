/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.rest;

import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.javaps.rest.model.Link;
import org.n52.javaps.rest.model.LandingPage;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Home redirection to swagger api documentation
 */
@Controller
@Configurable
public class LandingPageApiImpl implements LandingPageApi {

    private String serviceURL;

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", BASE_URL);
    }

    @Override
    public LandingPage landingPage() {

        String title = "52°North OGC API - Processes";

        String description = "52°North OGC API - Processes, powered by javaPS";

        List<Link> links = new ArrayList<>();

        Link link = new Link();
        link.setHref(serviceURL);
        link.setRel("self");
        link.setType(MediaTypes.APPLICATION_JSON);
        link.setTitle("this document");
        links.add(link);

        link = new Link();
        link.setHref(serviceURL + "/api/");
        link.setRel("service");
        link.setType(MediaTypes.APPLICATION_OPENAPI_JSON_3_0);
        link.setTitle("the API definition");
        links.add(link);

        link = new Link();
        link.setHref(serviceURL + "/conformance/");
        link.setRel("conformance");
        link.setType(MediaTypes.APPLICATION_JSON);
        link.setTitle("WPS 2.0 REST/JSON Binding Extension conformance classes implemented by this server");
        links.add(link);

        link = new Link();
        link.setHref(serviceURL + "/processes/");
        link.setRel("processes");
        link.setType(MediaTypes.APPLICATION_JSON);
        link.setTitle("The processes offered by this server");
        links.add(link);

        return new LandingPage().title(title).description(description).links(links);
    }
}
