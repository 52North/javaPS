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
package org.n52.javaps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class GetCapabilitiesKvpIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void complete() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        String response = getClient.sendRequest(url, "Service=WPS&Request=GetCapabilities");

        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
        assertThat(response, response, not(containsString("ExceptionReport")));

        assertThat(response, response, containsString("<wps:Capabilities"));
        assertThat(response, response, containsString("<ows:Operation name=\"Execute\">"));
        assertThat(response, response, containsString("<ows:ServiceType>OGC:WPS</ows:ServiceType>"));
        assertThat(response, response, containsString("org.n52.javaps.test.EchoProcess"));
    }

    @Test
    public void missingRequestParameter() throws IOException, ParserConfigurationException, SAXException,
            URISyntaxException {
        getClient.checkForExceptionReport(url, "Service=WPS", HttpServletResponse.SC_BAD_REQUEST,
                "MissingParameterValue");
    }

    @Test
    public void missingServiceParameter() throws IOException, ParserConfigurationException, SAXException,
            URISyntaxException {
        getClient.checkForExceptionReport(url, "Request=GetCapabilities", HttpServletResponse.SC_BAD_REQUEST,
                "MissingParameterValue");
    }

    @Test
    public void noVersionParameter() throws ParserConfigurationException, SAXException, IOException,
            URISyntaxException {
        String response = getClient.sendRequest(url, "Service=WPS&Request=GetCapabilities");

        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
        assertThat(response, response, not(containsString("ExceptionReport")));

        assertThat(response, response, containsString("<wps:Capabilities"));
        assertThat(response, response, containsString("<ows:Operation name=\"Execute\">"));
        assertThat(response, response, containsString("<ows:ServiceType>OGC:WPS</ows:ServiceType>"));
    }

    @Test
    public void wrongVersion() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetCapabilities&acceptVersions=42.17",
                HttpServletResponse.SC_BAD_REQUEST, "VersionNegotiationFailed");
    }

    @Test
    public void wrongServiceParameter() throws ParserConfigurationException, SAXException, IOException,
            URISyntaxException {
        getClient.checkForExceptionReport(url, "Service=HotDogStand&Request=GetCapabilities",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue");
    }
}
