/*
 * Copyright 2016-2021 52°North Spatial Information Research GmbH
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
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Test;
import org.xml.sax.SAXException;

import net.opengis.wps.x20.CapabilitiesDocument;

public class GetCapabilitiesPostIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void complete() throws XmlException, IOException {

        URL resource = getClass().getClassLoader().getResource("requests/GetCapabilities/GetCapabilities.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = "";
        try {
            response = postClient.sendRequest(url, payload.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        CapabilitiesDocument capsDoc = CapabilitiesDocument.Factory.parse(response);

        XmlOptions opts = new XmlOptions();
        ArrayList<XmlError> errors = new ArrayList<XmlError>();
        opts.setErrorListener(errors);
        boolean valid = capsDoc.validate(opts);

        assertTrue(Arrays.deepToString(errors.toArray()), valid);
    }

    @Test
    public void validateCapabilities() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/GetCapabilities/GetCapabilities.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        String response = "";
        try {
            response = postClient.sendRequest(url, payload.toString());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        CapabilitiesDocument capsDoc = CapabilitiesDocument.Factory.parse(response);

        XmlOptions opts = new XmlOptions();
        ArrayList<XmlError> errors = new ArrayList<XmlError>();
        opts.setErrorListener(errors);
        boolean valid = capsDoc.validate(opts);

        assertTrue(Arrays.deepToString(errors.toArray()), valid);
    }

    @Test
    public void wrongVersion() throws XmlException, IOException {
        URL resource = getClass().getClassLoader().getResource("requests/GetCapabilities/WrongVersion.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "VersionNegotiationFailed");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void wrongServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException {
        URL resource = getClass().getClassLoader().getResource("requests/GetCapabilities/WrongService.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "InvalidParameterValue");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void missingServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException {
        URL resource = getClass().getClassLoader().getResource("requests/GetCapabilities/MissingService.xml");
        XmlObject payload = XmlObject.Factory.parse(resource);

        try {
            postClient.checkForExceptionReport(url, payload.toString(), HttpServletResponse.SC_BAD_REQUEST,
                    "MissingParameterValue");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
