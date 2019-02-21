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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;

public class DescribeProcessPostIT extends Base {

    private final String testProcessID = "org.n52.javaps.test.EchoProcess";

    private final String testProcessID2 = "org.n52.javaps.test.MultiReferenceInputAlgorithm";

    private String url = getEndpointURL();

    /*
     * *GetCapabilities* - DescribeProcess POST request for a single process -
     * DescribeProcess POST request for a mutliple processes - DescribeProcess
     * POST request with missing "version" paramater - DescribeProcess POST
     * request with missing "service" paramater - DescribeProcess POST request
     * with missing "identifier" paramater - DescribeProcess POST request with
     * wrong "identifier" paramater value
     */

    @Test
    public void testDescribeProcessCompleteSingle() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/DescribeProcess/DescribeProcessSingle.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = "";
        try {
            response = PostClient.sendRequest(url, payload.toString());
            // parseXML(response);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertTrue(!response.contains("ExceptionReport"));
        assertTrue(response.contains(testProcessID));
    }

    // @Test
    // public void testDescribeProcessCompleteSingleWrongLanguage() throws
    // XmlException, IOException {
    // URL resource =
    // getClass().getClassLoader().getResource("requests/DescribeProcess/DescribeProcessSingleWrongLanguage.xml");
    // XmlObject payload = XmlObject.Factory.parse(resource);
    //
    // try {
    // PostClient.checkForExceptionReport(url, payload.toString(),
    // HttpServletResponse.SC_BAD_REQUEST, "language");
    // } catch (IOException e) {
    // fail(e.getMessage());
    // }
    // }

    @Test
    public void testDescribeProcessCompleteMultiple() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/DescribeProcess/DescribeProcessMultiple.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = "";
        try {
            response = PostClient.sendRequest(url, payload.toString());
            // parseXML(response);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(!response.contains("ExceptionReport"));
        assertTrue(response.contains(testProcessID));
        assertTrue(response.contains(testProcessID2));

    }

    @Test
    public void testDescribeProcessCompleteAll() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/DescribeProcess/DescribeProcessALL.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = "";
        try {
            response = PostClient.sendRequest(url, payload.toString());
            // parseXML(response);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertTrue(!response.contains("ExceptionReport"));
        assertTrue(response.contains(testProcessID));
        assertTrue(response.contains(testProcessID2));

    }

    @Test
    public void testDescribeProcessMissingVersionParameter() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource(
                "requests/DescribeProcess/DescribeProcessMissingVersion.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            PostClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "version");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDescribeProcessMissingServiceParameter() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource(
                "requests/DescribeProcess/DescribeProcessMissingService.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            PostClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "service");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDescribeProcessMissingIdentifierParameter() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource(
                "requests/DescribeProcess/DescribeProcessMissingIdentifierParameter.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            PostClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "Identifier");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDescribeProcessMissingIdentifierValue() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource(
                "requests/DescribeProcess/DescribeProcessMissingIdentifierValue.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            PostClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue", "Identifier");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDescribeProcessWrongIdentifierParameter() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource(
                "requests/DescribeProcess/DescribeProcessInvalidIdentifierValue.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            PostClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "InvalidParameterValue", "Identifier");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
