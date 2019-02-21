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

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class GetStatusKvpIT extends Base {

    private String url = getEndpointURL();

    @Test
    public void noSuchJob() throws IOException, ParserConfigurationException, SAXException {

        String nonExistingJobID = "this-id-doesnt-exist";

        GetClient.checkForExceptionReport(url, "Service=WPS&version=2.0.0&Request=GetStatus&jobID=" + nonExistingJobID,
                HttpServletResponse.SC_BAD_REQUEST, "NoSuchJob", nonExistingJobID);
    }

}
