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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.base.Joiner;

public class PostClient {

    public String buildRequest(String value) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode("process", "UTF-8");
        data += "&" + URLEncoder.encode("payload", "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");

        return data;
    }

    public static String sendRequest(String targetURL,
            String payload) throws IOException {
        // Construct data
        InputStream in = sendRequestForInputStream(targetURL, payload);

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        List<String> lines = new LinkedList<String>();
        String line;
        while ((line = rd.readLine()) != null) {
            lines.add(line);
        }
        rd.close();
        return Joiner.on('\n').join(lines);
    }

    public static InputStream sendRequestForInputStream(String targetURL,
            String payload) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(targetURL);

        post.addHeader("Accept-Encoding", "gzip");
        post.addHeader("Content-Type", "text/xml");

        post.setEntity(new StringEntity(payload));

        CloseableHttpResponse response = httpClient.execute(post);

        return response.getEntity().getContent();
    }

    public static void checkForExceptionReport(String targetURL,
            String payload,
            int expectedHTTPStatusCode,
            String... expectedExceptionParameters) throws IOException {
        // Send data
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(targetURL);

        post.addHeader("Accept-Encoding", "gzip");
        post.addHeader("Content-Type", "text/xml");

        post.setEntity(new StringEntity(payload));

        CloseableHttpResponse response = httpClient.execute(post);

        InputStream error = response.getEntity().getContent();

        String exceptionReport = "";

        int data = error.read();
        while (data != -1) {
            exceptionReport = exceptionReport + (char) data;
            data = error.read();
        }
        error.close();
        assertTrue(response.getStatusLine().getStatusCode() == expectedHTTPStatusCode);

        for (String expectedExceptionParameter : expectedExceptionParameters) {

            assertTrue(exceptionReport.contains(expectedExceptionParameter));

        }
    }
}
