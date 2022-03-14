/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AllTestsIT extends Base {

    private final static String TIFF_MAGIC = "II";

    public static Document parseXML(String xmlString) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        StringReader inStream = new StringReader(xmlString);
        InputSource inSource = new InputSource(inStream);
        return documentBuilder.parse(inSource);
    }

    public void validateBinaryBase64Async(String response) throws IOException, ParserConfigurationException,
            SAXException, URISyntaxException {
        String referencedDocument = getAsyncDoc(response);
        assertThat(referencedDocument, referencedDocument, not(containsString("ExceptionReport")));
        assertThat(referencedDocument, referencedDocument, containsString("ExecuteResponse"));
        assertThat(referencedDocument, referencedDocument, anyOf(containsString("AAEGAAMAAAABAAEAAAEVAAMAAAABA"),
                containsString("Tk9SVEg6IDIyOD"), containsString("SUkqAAgAAAASAAABAwABAAAAIwAA")));
    }

    public String getRefAsString(String response) throws ParserConfigurationException, SAXException,
            IOException, URISyntaxException {
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("ProcessSucceeded"));
        assertThat(response, response, containsString("Reference"));

        Document doc = AllTestsIT.parseXML(response);
        NodeList executeResponse = doc.getElementsByTagName("wps:Reference");

        assertThat(executeResponse.getLength(), greaterThan(0));

        NamedNodeMap attributes = executeResponse.item(0).getAttributes();
        String statusLocation = attributes.getNamedItem("href").getNodeValue();
        String[] splittedURL = statusLocation.split("RetrieveResultServlet?");

        assertThat(splittedURL.length, equalTo(2));

        String referencedDocument = getClient.sendRequest(splittedURL[0] + "RetrieveResultServlet", splittedURL[1]);

        return referencedDocument;
    }

    public InputStream getRefAsStream(String response) throws ParserConfigurationException, SAXException,
            IOException, URISyntaxException {
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("ProcessSucceeded"));
        assertThat(response, response, containsString("Reference"));

        Document doc = AllTestsIT.parseXML(response);
        NodeList executeResponse = doc.getElementsByTagName("wps:Reference");

        assertThat(executeResponse.getLength(), greaterThan(0));

        NamedNodeMap attributes = executeResponse.item(0).getAttributes();
        String statusLocation = attributes.getNamedItem("href").getNodeValue();
        String[] splittedURL = statusLocation.split("RetrieveResultServlet?");

        assertThat(splittedURL.length, equalTo(2));

        return getClient.sendRequestForInputStream(splittedURL[0] + "RetrieveResultServlet", splittedURL[1]);
    }

    public String getAsyncDoc(String response) throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("statusLocation"));

        Document doc = AllTestsIT.parseXML(response);

        NodeList executeResponse = doc.getElementsByTagName("wps:ExecuteResponse");

        assertThat(executeResponse.getLength(), greaterThan(0));

        NamedNodeMap attributes = executeResponse.item(0).getAttributes();
        String statusLocation = attributes.getNamedItem("statusLocation").getNodeValue();
        String[] splittedURL = statusLocation.split("RetrieveResultServlet?");

        assertThat(splittedURL.length, equalTo(2));

        String referencedDocument = getClient.sendRequest(splittedURL[0] + "RetrieveResultServlet", splittedURL[1]);

        assertThat(referencedDocument, referencedDocument, not(containsString("ExceptionReport")));
        assertThat(referencedDocument, referencedDocument, containsString("Status"));

        for (int i = 0; i < 4; i++) {
            if (!referencedDocument.contains("ProcessSucceeded") && !referencedDocument.contains("ProcessFailed")) {
                try {
                    System.out.println("WPS process still processing. Waiting...");
                    Thread.sleep(1000 * 3);
                    referencedDocument = getClient.sendRequest(splittedURL[0] + "RetrieveResultServlet",
                            splittedURL[1]);
                } catch (InterruptedException ignore) {
                    // do nothing
                }
            } else {
                return referencedDocument;
            }
        }
        throw new IOException("Test did not complete in allotted time");
    }

    public void checkReferenceXMLResult(String response,
            String stringThatShouldBeContained) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        String referencedDocument = getRefAsString(response);
        assertThat(referencedDocument, referencedDocument, not(containsString("ExceptionReport")));
        assertThat(referencedDocument, referencedDocument, containsString(stringThatShouldBeContained));
        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
    }

    public void checkReferenceBinaryResultBase64(String response) throws ParserConfigurationException,
            SAXException, IOException, URISyntaxException {
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("ProcessSucceeded"));
        assertThat(response, response, containsString("Reference"));

        String responseAsString = getRefAsString(response);

        assertTrue(Base64.isBase64(responseAsString));
    }

    public void checkReferenceBinaryResultDefault(String response) throws ParserConfigurationException,
            SAXException, IOException, URISyntaxException {
        assertThat(response, response, not(containsString("ExceptionReport")));
        assertThat(response, response, containsString("ProcessSucceeded"));
        assertThat(response, response, containsString("Reference"));

        String responseAsString = getRefAsString(response);

        assertThat(responseAsString, responseAsString, containsString(TIFF_MAGIC));
    }

    public static void checkRawBinaryResultBase64(InputStream stream) {

        String responseAsString = saveInputStreamToString(stream);

        assertTrue(Base64.isBase64(responseAsString));
    }

    public static void checkRawBinaryResultDefault(InputStream stream) {

        String responseAsString = saveInputStreamToString(stream);

        assertThat(responseAsString, responseAsString, containsString(TIFF_MAGIC));
    }

    public static String saveInputStreamToString(InputStream stream) {

        StringBuilder stringBuilder = new StringBuilder();

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (Exception e) {
            System.err.println("Could not save inputstream content to String.");
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                //
            }
        }

        return stringBuilder.toString();

    }
}
