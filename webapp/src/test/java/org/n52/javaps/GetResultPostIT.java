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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.xml.sax.SAXException;

import net.opengis.wps.x20.StatusInfoDocument;

public class GetResultPostIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void resultNotReady() throws ParserConfigurationException, SAXException, IOException, XmlException {

        URL resource = getClass().getClassLoader().getResource("requests/Execute/EchoProcessDuration1000.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = postClient.sendRequest(url, payload.toString());
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("Status"));
        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));

        String jobId = "";

        try {
            StatusInfoDocument statusInfoDocument = StatusInfoDocument.Factory.parse(response);

            jobId = statusInfoDocument.getStatusInfo().getJobID();
        } catch (XmlException e) {
            fail(e.getMessage());
        }

        URL getResultResource = getClass().getClassLoader().getResource("requests/GetResult/GetResult.xml");
        String getResultPayload = XmlObject.Factory.parse(getResultResource).toString();

        getResultPayload =getResultPayload.replace("{job-id}", jobId.trim());

        postClient.checkForExceptionReport(url, getResultPayload, HttpServletResponse.SC_BAD_REQUEST, "ResultNotReady", jobId);
    }

    @Test
    public void wrongVersion() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/GetResult/WrongVersion.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "InvalidParameterValue", "locator=\"version\"");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void missingVersion() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/GetResult/MissingVersion.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "locator=\"version\"");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void wrongServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException {
        URL resource = getClass().getClassLoader().getResource("requests/GetResult/WrongService.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "InvalidParameterValue", "locator=\"service\"");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void missingServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException {
        URL resource = getClass().getClassLoader().getResource("requests/GetResult/MissingService.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "locator=\"service\"");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
