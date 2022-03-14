/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.xmlmatchers.namespace.SimpleNamespaceContext;

import com.google.common.io.Resources;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class Base {

    static SimpleNamespaceContext usingNamespaces = new SimpleNamespaceContext().withBinding("gmd",
            "http://www.isotc211.org/2005/gmd").withBinding("ogc", "http://www.opengis.net/ogc").withBinding("xlink",
                    "http://www.w3.org/1999/xlink").withBinding("xsi", "http://www.w3.org/2001/XMLSchema-instance")
            .withBinding("xs", "http://www.w3.org/2001/XMLSchema").withBinding("ows", "http://www.opengis.net/ows/2.0");

    GetClient getClient = new GetClient();
    PostClient postClient = new PostClient();

    public int getPort() {
        return Integer.parseInt(System.getProperty("test.port", "8080"));
    }

    public String getHost() {
        return System.getProperty("test.host", "localhost");
    }

    public String getContext() {
        return System.getProperty("test.context", "/javaps");
    }

    public String getURL() {
        return "http://" + getHost() + ":" + getPort() + getContext();
    }

    public String getEndpointURL() {
        return getURL() + "/service";
    }

    public String kvp(String query) throws IOException {
        String url = getEndpointURL() + "?" + query;
        return Request.Get(url).execute().returnContent().asString();
    }

    public String pox(String filename) throws IOException {
        URL url = Resources.getResource("requests/" + filename);
        InputStream request = Resources.asByteSource(url).openBufferedStream();
        return Request.Post(getEndpointURL()).bodyStream(request, ContentType.APPLICATION_XML).execute().returnContent()
                .asString();
    }

}
