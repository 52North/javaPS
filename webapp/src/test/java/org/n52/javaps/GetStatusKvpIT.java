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

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.xml.sax.SAXException;

public class GetStatusKvpIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void noSuchJob() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        String nonExistingJobID = "this-id-doesnt-exist";

        getClient.checkForExceptionReport(url, "Service=WPS&version=2.0.0&Request=GetStatus&jobID=" + nonExistingJobID,
                HttpServletResponse.SC_BAD_REQUEST, "NoSuchJob", nonExistingJobID);
    }

    @Test
    public void missingVersionParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetStatus&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "MissingParameter", "locator=\"version\"");
    }

    @Test
    public void wrongVersionParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=WPS&Request=GetStatus&version=42.0.0&&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "locator=\"version\"");
    }

    @Test
    public void missingServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Request=GetStatus&version=2.0.0&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "MissingParameter", "locator=\"service\"");
    }

    @Test
    public void wrongServiceParameter() throws ParserConfigurationException, SAXException, IOException, XmlException,
            URISyntaxException {

        getClient.checkForExceptionReport(url, "Service=my-service&Request=GetStatus&version=2.0.0&&jobID=",
                HttpServletResponse.SC_BAD_REQUEST, "InvalidParameterValue", "locator=\"service\"");
    }

}
