/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.xml.sax.SAXException;

import net.opengis.wps.x20.StatusInfoDocument;

public class GetResultKvpIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void resultNotReady() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

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

        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetResult&version=2.0.0&jobID=" + jobId,
                HttpServletResponse.SC_BAD_REQUEST, "ResultNotReady", jobId);
    }

    @Test
    public void missingVersionParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetResult&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "MissingParameter", "locator=\"version\"");
    }

    @Test
    public void wrongVersionParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetResult&version=42.0.0&&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "locator=\"version\"");
    }

    @Test
    public void missingServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Request=GetResult&version=2.0.0&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "MissingParameter", "locator=\"service\"");
    }

    @Test
    public void wrongServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=my-service&Request=GetResult&version=2.0.0&&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "locator=\"service\"");
    }

}
