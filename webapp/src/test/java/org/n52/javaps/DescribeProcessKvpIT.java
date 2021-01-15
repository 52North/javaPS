/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

public class DescribeProcessKvpIT extends Base {

    private final String testProcessID = "org.n52.javaps.test.EchoProcess";

    private final String testProcessID2 = "org.n52.javaps.test.MultiReferenceInputAlgorithm";

    private String url = getEndpointURL();

    @Test
    public void testDescribeProcessCompleteSingle() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessCompleteSingle");

        String response = getClient.sendRequest(url, "Service=WPS&Request=DescribeProcess&Version=2.0.0&Identifier="
                + testProcessID);

        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString(testProcessID));
    }

    @Test
    public void testDescribeProcessCompleteMultiple() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessCompleteMultiple");

        String response = getClient.sendRequest(url, "Service=WPS&Request=DescribeProcess&Version=2.0.0&Identifier="
                + testProcessID + "," + testProcessID2);

        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString(testProcessID));
        assertThat(response, response, containsString(testProcessID2));

    }

    @Test
    public void testDescribeProcessCompleteAll() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessCompleteAll");

        String response = getClient.sendRequest(url,
                "Service=WPS&Request=DescribeProcess&Version=2.0.0&Identifier=ALL");

        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString(testProcessID));
        assertThat(response, response, containsString(testProcessID2));

    }

    @Test
    public void testDescribeProcessMissingVersionParameter() throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessMissingVersionParameter");

        getClient.checkForExceptionReport(url, "Service=WPS&Request=DescribeProcess&Identifier=" + testProcessID,
                HttpServletResponse.SC_BAD_REQUEST, "version");
    }

    @Test
    public void testDescribeProcessMissingServiceParameter() throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessMissingServiceParameter");

        getClient.checkForExceptionReport(url, "Request=DescribeProcess&Version=2.0.0&Identifier=" + testProcessID,
                HttpServletResponse.SC_BAD_REQUEST, "service");
    }

    @Test
    public void testDescribeProcessMissingIdentifierParameter() throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessMissingIdentifierParameter");

        getClient.checkForExceptionReport(url, "Request=DescribeProcess&service=WPS&Version=2.0.0",
                HttpServletResponse.SC_BAD_REQUEST, "MissingParameterValue", "Identifier");
    }

    @Test
    public void testDescribeProcessWrongIdentifierParameter() throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessWrongIdentifierParameter");

        getClient.checkForExceptionReport(url, "Request=DescribeProcess&service=WPS&Version=2.0.0&Identifier=XXX",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "Identifier");
    }

    @Test
    public void testDescribeProcessMissingIdentifierValue() throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        System.out.println("\nRunning testDescribeProcessMissingIdentifierValue");

        getClient.checkForExceptionReport(url, "Request=DescribeProcess&service=WPS&Version=2.0.0&Identifier=",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "Identifier");
    }
}
