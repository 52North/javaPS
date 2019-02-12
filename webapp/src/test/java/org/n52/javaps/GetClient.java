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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;

public class GetClient {

    public static String sendRequest(String targetURL) throws IOException {
        return sendRequest(targetURL, null);
    }

    public static String sendRequest(String targetURL, String payload) throws IOException {
        // Send data
        InputStream in = sendRequestForInputStream(targetURL, payload);

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        List<String> lines= new LinkedList<String>();
        String line;
        while ( (line = rd.readLine()) != null) {
            lines.add(line);
        }
        rd.close();
        return Joiner.on('\n').join(lines);
    }

    public static InputStream sendRequestForInputStream(String targetURL, String payload) throws IOException {

        // Send data
        URL url = null;
        if (payload == null || payload.equalsIgnoreCase("")) {
            url = new URL(targetURL);
        }
        else {
            String payloadClean = payload.replace("?", "");
            url = new URL(targetURL + "?" + payloadClean);
        }

        return url.openStream();
    }

    public static void checkForExceptionReport(String targetURL, String payload, int expectedHTTPStatusCode, String... expectedExceptionParameters) throws IOException{
        // Send data
        String payloadClean = payload.replace("?", "");
        URL url = new URL(targetURL + "?" + payloadClean);

        URLConnection conn = url.openConnection();

        try {
            conn.getInputStream();
        } catch (IOException e) {
            /*
             * expected, ignore
             */
        }

        InputStream error = ((HttpURLConnection) conn).getErrorStream();

        String exceptionReport = "";

        int data = error.read();
        while (data != -1) {
            exceptionReport = exceptionReport + (char)data;
            data = error.read();
        }
        error.close();
        assertTrue(((HttpURLConnection) conn).getResponseCode() == expectedHTTPStatusCode);

        for (String expectedExceptionParameter : expectedExceptionParameters) {

            assertTrue(exceptionReport.contains(expectedExceptionParameter));

        }
    }
}
