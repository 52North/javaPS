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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.n52.geoprocessing.wps.client.ExecuteRequestBuilder;
import org.n52.geoprocessing.wps.client.WPSClientException;
import org.n52.geoprocessing.wps.client.WPSClientSession;
import org.n52.geoprocessing.wps.client.model.ExceptionReport;
import org.n52.geoprocessing.wps.client.model.Process;
import org.n52.geoprocessing.wps.client.model.Result;
import org.n52.geoprocessing.wps.client.model.StatusInfo;
import org.n52.geoprocessing.wps.client.model.execution.Data;

public class ExecutePostIT extends Base {

    private final static String TIFF_MAGIC = "<![CDATA[II";
    private String url = getEndpointURL();

    public final String referenceComplexBinaryInputURL = getURL() +
            "static/testData/elev_srtm_30m21.tif";
    public final String referenceComplexXMLInputURL = getURL() +
         "static/testData/test-data.xml";

    private ExecuteRequestBuilder echoProcessExecuteRequestBuilder;
    private final String echoProcessIdentifier = "org.n52.javaps.test.EchoProcess";
    private final String echoProcessInlineComplexXMLInput = "<TestData><this><is><xml><Data>Test</Data></xml></is></this></TestData>";
    private final String testDataNodeName = "TestData";
    private final String echoProcessLiteralInputID = "literalInput";
    private final String echoProcessLiteralInputString = "testData";
    private final String echoProcessComplexInputID = "complexInput";
    private final String echoProcessComplexMimeTypeTextXML = "text/xml";
    private final String echoProcessLiteralMimeTypeTextXML = "text/xml";
    private final String echoProcessComplexOutputID = "complexOutput";
    private final String echoProcessLiteralOutputID = "literalOutput";

    private ExecuteRequestBuilder multiReferenceBinaryInputAlgorithmExecuteRequestBuilder;
    private final String multiReferenceBinaryInputAlgorithmIdentifier = "org.n52.wps.server.algorithm.test.MultiReferenceBinaryInputAlgorithm";
    private final String multiReferenceBinaryInputAlgorithmComplexInputID = "data";
    private final String multiReferenceBinaryInputAlgorithmComplexOutputID = "result";
    private final String multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff= "image/tiff";
    private final String base64TiffStart= "SUkqAAgAAAASAAA";

    private String tiffImageBinaryInputAsBase64String;

    private String version200 = "2.0.0";

    @Before
    public void before(){

        WPSClientSession wpsClient = WPSClientSession.getInstance();

        try {
            wpsClient.connect(url, version200);
        } catch (WPSClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Process echoProcessDescription;

            echoProcessDescription = wpsClient
                    .getProcessDescription(url, echoProcessIdentifier, version200);

            echoProcessExecuteRequestBuilder = new ExecuteRequestBuilder(echoProcessDescription);


        assertThat(echoProcessExecuteRequestBuilder, is(not(nullValue())));

//        Process multiReferenceBinaryInputAlgorithmDescription;
//
//            multiReferenceBinaryInputAlgorithmDescription = wpsClient
//                    .getProcessDescription(url, multiReferenceBinaryInputAlgorithmIdentifier, version200);
//
//            multiReferenceBinaryInputAlgorithmExecuteRequestBuilder = new ExecuteRequestBuilder(multiReferenceBinaryInputAlgorithmDescription);
//
//
//        assertThat(multiReferenceBinaryInputAlgorithmExecuteRequestBuilder, is(not(nullValue())));
//
//        InputStream tiffImageInputStream = getClass().getResourceAsStream("/Execute/image.tiff.base64");
//
//        BufferedReader tiffImageInputStreamReader = new BufferedReader(new InputStreamReader(tiffImageInputStream));
//
//        StringBuilder tiffImageInputStringBuilder = new StringBuilder();
//
//        String line = "";
//
//        try {
//            while ((line = tiffImageInputStreamReader.readLine()) != null) {
//                tiffImageInputStringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        tiffImageBinaryInputAsBase64String = tiffImageInputStringBuilder.toString();
//
//        assertThat(tiffImageBinaryInputAsBase64String, is(not(nullValue())));
//        assertThat(tiffImageBinaryInputAsBase64String, is(not(equalTo(""))));

    }
//
//    /*Complex inline XML input */
//    @Test
//    public void testExecutePOSTInlineComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTInlineComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute(), version200);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof Result) {
//
//                Result result = (Result)responseObject;
//
//                List<Data> outputs = result.getOutputs();
//
//                assertTrue(outputs.size() == 1);
//                assertTrue(outputs.get(0).getId().equals(echoProcessComplexOutputID));
////                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
////
////                checkIfResultContainsTestXMLData(executeResponseDocument);
//
//            }
//        } catch (WPSClientException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    // cannot test anything else but getting no exception report right now
//    @Test
//    public void testExecutePOSTMultipleInlineComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTInlineComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//            echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute(), version200);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof Result) {
//
//                Result result = (Result)responseObject;
//
//                List<Data> outputs = result.getOutputs();
//
//                assertTrue(outputs.size() == 1);
//                assertTrue(outputs.get(0).getId().equals(echoProcessComplexOutputID));
////                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
////
////                checkIfResultContainsTestXMLData(executeResponseDocument);
//
//            }
//        } catch (WPSClientException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void testExecutePOSTMultipleInlineLiteralSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTInlineComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute(), version200);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof Result) {
//
//                Result result = (Result)responseObject;
//
//                List<Data> outputs = result.getOutputs();
//
//                assertTrue(outputs.size() == 1);
//                assertTrue(outputs.get(0).getId().equals(echoProcessComplexOutputID));
////                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
////
////                checkIfResultContainsTestXMLData(executeResponseDocument);
//
//            }
//        } catch (WPSClientException e) {
//            fail(e.getMessage());
//        }
//    }

//    /*Complex XML input by reference */
//    @Test
//    public void testExecutePOSTreferenceComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTreferenceComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID, AllTestsIT.referenceComplexXMLInputURL, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute());
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
//
//                checkIfResultContainsTestXMLData(executeResponseDocument);
//
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*Complex XML Input by reference using a post request*/
//    @Test
//    public void testExecutePOSTreferencePOSTComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTreferencePOSTComplexXMLSynchronousXMLOutput");
//
//        echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID,  AllTestsIT.referenceComplexXMLInputURL, null, null, echoProcessComplexMimeTypeTextXML);
//
//        echoProcessExecuteRequestBuilder.setRawData(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//                +"<wps:Execute service=\"WPS\" version=\"1.0.0\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"
//                +"    http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd\">"
//                +"    <ows:Identifier>org.n52.wps.server.algorithm.test.EchoProcess</ows:Identifier>"
//                +"    <wps:DataInputs>"
//                +"        <wps:Input>"
//                +"            <ows:Identifier>complexInput</ows:Identifier>"
//                +"            <wps:Reference mimeType=\"text/xml\" xlink:href=\"" + AllTestsIT.getURL() + "\">"
//                +"            <wps:Body>"
//                + echoProcessExecuteRequestBuilder.getExecute().toString()
//                +"            </wps:Body>"
//                +"            </wps:Reference>"
//                +"        </wps:Input>"
//                +"    </wps:DataInputs>"
//                +"    <wps:ResponseForm>"
//                +"    <wps:ResponseDocument storeExecuteResponse=\"false\">"
//                +"        <wps:Output asReference=\"false\">"
//                +"            <ows:Identifier>complexOutput</ows:Identifier>"
//                +"        </wps:Output>"
//                +"    </wps:ResponseDocument>"
//                +"    </wps:ResponseForm>"
//                +"</wps:Execute>";
//        String response = PostClient.sendRequest(url, payload);
//
//        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//        assertThat(response, response, not(containsString("ExceptionReport")));
//        assertThat(response, response, containsString(testDataNodeName));
//    }
//
//    /*Multiple complex XML Input by reference */
//    @Test
//    public void testExecutePOSTMultipleReferenceComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTMultipleReferenceComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID, AllTestsIT.referenceComplexXMLInputURL, null, null, echoProcessComplexMimeTypeTextXML);
//            echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID, AllTestsIT.referenceComplexXMLInputURL, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute());
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
//
//                checkIfResultContainsTestXMLData(executeResponseDocument);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private InputType createComplexInlineInput(String identifier, String value, String schema, String encoding, String mimeType){
//
//        InputType inputType = InputType.Factory.newInstance();
//
//        inputType.addNewIdentifier().setStringValue(identifier);
//
//        try {
//
//            ComplexDataType data = inputType.addNewData().addNewComplexData();
//
//            XmlOptions xmlOptions = new XmlOptions();
//
//            data.set(XmlObject.Factory.parse(value, xmlOptions));
//            if (schema != null) {
//                data.setSchema(schema);
//            }
//            if (mimeType != null) {
//                data.setMimeType(mimeType);
//            }
//            if (encoding != null) {
//                data.setEncoding(encoding);
//            }
//        } catch (XmlException e) {
//            throw new IllegalArgumentException(
//                    "error inserting node into complexdata", e);
//        }
//
//        return inputType;
//
//    }
//
//    private Object createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(List<InputType> inputs, boolean status, boolean storeSupport, boolean asReference) throws WPSClientException{
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        AllTestsIT.referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        AllTestsIT.referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setResponseDocument(multiReferenceBinaryInputAlgorithmComplexOutputID, null, "base64", multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStoreSupport(multiReferenceBinaryInputAlgorithmComplexOutputID, storeSupport);
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStatus(multiReferenceBinaryInputAlgorithmComplexOutputID, status);
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setAsReference(multiReferenceBinaryInputAlgorithmComplexOutputID, asReference);
//
//        Object responseObject =  WPSClientSession.getInstance().execute(url, multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.getExecute());
//
//        return responseObject;
//    }
//
//    /*Multiple complex XML Input by reference */
//    @Test
//    public void testExecutePOSTMultipleReferenceComplexBinarySynchronousBinaryOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTMultipleReferenceComplexBinarySynchronousBinaryOutput");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, false, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, multiReferenceBinaryInputAlgorithmComplexOutputID);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*Complex XML Input by reference, POST*/
//    @Test
//    public void testExecutePOSTReferenceComplexXMLSynchronousXMLOutput_WFS_POST_MissingMimeType() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTReferenceComplexXMLSynchronousXMLOutput_WFS_POST_MissingMimeType");
//
//        echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID, AllTestsIT.referenceComplexXMLInputURL, null, null, echoProcessComplexMimeTypeTextXML);
//
//        echoProcessExecuteRequestBuilder.setRawData(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//                +"<wps:Execute service=\"WPS\" version=\"1.0.0\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"
//                +"    http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd\">"
//                +"    <ows:Identifier>org.n52.wps.server.algorithm.test.EchoProcess</ows:Identifier>"
//                +"    <wps:DataInputs>"
//                +"        <wps:Input>"
//                +"            <ows:Identifier>complexInput</ows:Identifier>"
//                +"            <wps:Reference xlink:href=\"" + AllTestsIT.getURL() + "\">"
//                +"            <wps:Body>"
//                + echoProcessExecuteRequestBuilder.getExecute().toString()
//                +"            </wps:Body>"
//                +"            </wps:Reference>"
//                +"        </wps:Input>"
//                +"    </wps:DataInputs>"
//                +"    <wps:ResponseForm>"
//                +"    <wps:ResponseDocument storeExecuteResponse=\"false\">"
//                +"        <wps:Output asReference=\"false\">"
//                +"            <ows:Identifier>complexOutput</ows:Identifier>"
//                +"        </wps:Output>"
//                +"    </wps:ResponseDocument>"
//                +"    </wps:ResponseForm>"
//                +"</wps:Execute>";
//        String response = PostClient.sendRequest(url, payload);
//
//        assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//        assertThat(response, response, not(containsString("ExceptionReport")));
//        assertThat(response, response, containsString(testDataNodeName));
//    }
//
//    /*Complex binary Input by value */
//    @Test
//    public void testExecutePOSTInlineComplexBinaryASynchronousBinaryOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTInlineComplexBinaryASynchronousBinaryOutput");
//        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//                + "<wps:Execute service=\"WPS\" version=\"1.0.0\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"
//                + "http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd\">"
//                + "<ows:Identifier>org.n52.wps.server.algorithm.test.MultiReferenceBinaryInputAlgorithm</ows:Identifier>"
//                + "<wps:DataInputs>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Data>"
//                + "<wps:ComplexData mimeType=\"image/tiff\" encoding=\"base64\">"
//                + tiffImageBinaryInputAsBase64String
//                + "</wps:ComplexData>"
//                + "</wps:Data>"
//                + "</wps:Input>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Data>"
//                + "<wps:ComplexData mimeType=\"image/tiff\" encoding=\"base64\">"
//                + tiffImageBinaryInputAsBase64String
//                + "</wps:ComplexData>"
//                + "</wps:Data>"
//                + "</wps:Input>"
//                + "</wps:DataInputs>"
//                + "<wps:ResponseForm>"
//                + "<wps:ResponseDocument status=\"true\" storeExecuteResponse=\"true\">"
//                + "<wps:Output encoding=\"base64\" mimeType=\"image/tiff\">"
//                + "<ows:Identifier>result</ows:Identifier>"
//                + "</wps:Output>"
//                + "</wps:ResponseDocument>"
//                + "</wps:ResponseForm>"
//                + "</wps:Execute>";
//        String response = PostClient.sendRequest(url, payload);
//        AllTestsIT.validateBinaryBase64Async(response);
//    }
//
//    /*Complex binary Input by value */
//    @Test
//    public void testExecutePOSTInlineAndReferenceComplexBinaryASynchronousBinaryOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTInlineAndReferenceComplexBinaryASynchronousBinaryOutput");
//        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//                + "<wps:Execute service=\"WPS\" version=\"1.0.0\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"
//                + "http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd\">"
//                + "<ows:Identifier>org.n52.wps.server.algorithm.test.MultiReferenceBinaryInputAlgorithm</ows:Identifier>"
//                + "<wps:DataInputs>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Data>"
//                + "<wps:ComplexData mimeType=\"image/tiff\" encoding=\"base64\">"
//                + tiffImageBinaryInputAsBase64String
//                + "</wps:ComplexData>"
//                + "</wps:Data>"
//                + "</wps:Input>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Reference mimeType=\"image/tiff\" xlink:href=\"" + AllTestsIT.referenceComplexBinaryInputURL + "\">"
//                + "</wps:Reference>"
//                + "</wps:Input>"
//                + "</wps:DataInputs>"
//                + "<wps:ResponseForm>"
//                + "<wps:ResponseDocument status=\"true\" storeExecuteResponse=\"true\">"
//                + "<wps:Output encoding=\"base64\" mimeType=\"image/tiff\">"
//                + "<ows:Identifier>result</ows:Identifier>"
//                + "</wps:Output>"
//                + "</wps:ResponseDocument>"
//                + "</wps:ResponseForm>"
//                + "</wps:Execute>";
//        String response = PostClient.sendRequest(url, payload);
//        AllTestsIT.validateBinaryBase64Async(response);
//    }
//
//    /*Complex binary Input by reference */
//    @Test
//    public void testExecutePOSTReferenceComplexBinaryASynchronousBinaryOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTReferenceComplexBinaryASynchronousBinaryOutput");
//        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//                + "<wps:Execute service=\"WPS\" version=\"1.0.0\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0"
//                + "http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd\">"
//                + "<ows:Identifier>org.n52.wps.server.algorithm.test.MultiReferenceBinaryInputAlgorithm</ows:Identifier>"
//                + "<wps:DataInputs>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Reference mimeType=\"image/tiff\" xlink:href=\"" + AllTestsIT.referenceComplexBinaryInputURL
//                + "\">"
//                + "</wps:Reference>"
//                + "</wps:Input>"
//                + "<wps:Input>"
//                + "<ows:Identifier>data</ows:Identifier>"
//                + "<wps:Reference mimeType=\"image/tiff\" xlink:href=\"" + AllTestsIT.referenceComplexBinaryInputURL
//                + "\">"
//                + "</wps:Reference>"
//                + "</wps:Input>"
//                + "</wps:DataInputs>"
//                + "<wps:ResponseForm>"
//                + "<wps:ResponseDocument status=\"true\" storeExecuteResponse=\"true\">"
//                + "<wps:Output encoding=\"base64\" >"
//                + "<ows:Identifier>result</ows:Identifier>"
//                + "</wps:Output>"
//                + "</wps:ResponseDocument>"
//                + "</wps:ResponseForm>"
//                + "</wps:Execute>";
//        String response = PostClient.sendRequest(url, payload);
//        AllTestsIT.validateBinaryBase64Async(response);
//    }
//
//    /*Literal Input by value String */
//    @Test
//    public void testExecutePOSTLiteralStringSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTLiteralStringSynchronousXMLOutput");
//
//        try {
////            echoProcessExecuteRequestBuilder.addComplexDataReference(echoProcessComplexInputID, echoProcessReferenceComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//            echoProcessExecuteRequestBuilder.addLiteralData(echoProcessLiteralInputID, echoProcessLiteralInputString);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessLiteralOutputID, null, null, null);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute());
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, echoProcessLiteralOutputID);
//
//                checkIfResultContainsTestStringData(executeResponseDocument);
//
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*Complex XML Output by value TODO: this could be checked with the testExecutePOSTInlineComplexXMLSynchronousXMLOutput*/
//    @Test
//    public void testExecutePOSTComplexXMLSynchronousXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLSynchronousXMLOutput");
//
//        try {
//            echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);
//
//            echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);
//
//            Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute());
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
//
//                checkIfResultContainsTestXMLData(executeResponseDocument);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*Complex XML Output by reference*/
//    @Test
//    public void testExecutePOSTComplexXMLSynchronousXMLOutputByReference() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLSynchronousXMLOutputByReference");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(false, false, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                checkIdentifier(executeResponseDocument, echoProcessComplexOutputID);
//
//                AllTestsIT.checkReferenceXMLResult(executeResponseDocument.toString(), testDataNodeName);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
    /*Complex inline XML Output*/
    @Test
    public void testExecutePOSTComplexXMLSynchronousXMLOutputStatusTrue() throws IOException {
        System.out.println("\nRunning testExecutePOSTComplexXMLSynchronousXMLOutputStatusTrue");

        try {

            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(false, false);

            assertThat(responseObject, is(not(nullValue())));
            assertThat(responseObject, is(not(instanceOf(ExceptionReport.class))));

            if (responseObject instanceof Result) {

                Result result = (Result) responseObject;

                checkIdentifier(result,
                        echoProcessComplexOutputID);

                checkOutputContainsValue(result, echoProcessComplexOutputID, testDataNodeName);
            }
        } catch (WPSClientException e) {
            fail(e.getMessage());
        }
    }

    /*Test concurrent process execution*/
//    @Test
    public void testExecutePOSTConcurrency() throws IOException {
        System.out.println("\nRunning testExecutePOSTConcurrency");


        new Thread("testExecutePOSTConcurrency thread 2") {
            public void run() {
            };
        }.start();

        String statusLocation005 = "";

        String literalValue005 = "0.05";

        try {

            Object responseObject =  createAndSubmitEchoProcessExecuteForConcurrencyTest(3000, literalValue005);

            assertThat(responseObject, is(not(nullValue())));
            assertThat(responseObject, is(not(instanceOf(ExceptionReport.class))));

            if (responseObject instanceof StatusInfo) {

                StatusInfo result = (StatusInfo) responseObject;

                statusLocation005 = result.getStatusLocation();
            }
        } catch (WPSClientException e) {
            fail(e.getMessage());
        }

        String statusLocation006 = "";
        String literalValue006 = "0.06";

        try {

            Object responseObject =  createAndSubmitEchoProcessExecuteForConcurrencyTest(1000, literalValue006);

            assertThat(responseObject, is(not(nullValue())));
            assertThat(responseObject, is(not(instanceOf(ExceptionReport.class))));

            if (responseObject instanceof StatusInfo) {

                StatusInfo result = (StatusInfo) responseObject;

                statusLocation006 = result.getStatusLocation();
            }
        } catch (WPSClientException | IOException e) {
            fail(e.getMessage());
        }

        System.out.println(statusLocation005);
        System.out.println(statusLocation006);

        try {
            Result result005 = (Result) WPSClientSession.getInstance().getResultFromStatusLocation(statusLocation005);

            checkOutputContainsValue(result005, echoProcessLiteralOutputID, literalValue005);

            Result result006 = (Result) WPSClientSession.getInstance().getResultFromStatusLocation(statusLocation006);

            checkOutputContainsValue(result006, echoProcessLiteralOutputID, literalValue006);
        } catch (WPSClientException | IOException e) {
            fail(e.getMessage());
        }

    }


//
//    @Test
//    public void testExecutePOSTComplexXMLASynchronousXMLOutputStoreStatusTrue() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLASynchronousXMLOutputStoreStatusTrue");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(true, true, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument) responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, containsString("statusLocation"));
//
//                String refResult = AllTestsIT.getAsyncDoc(response);
//                assertThat(refResult, refResult, containsString(echoProcessComplexOutputID));
//                assertThat(refResult, refResult, containsString(testDataNodeName));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexXMLASynchronousXMLOutputStoreTrue() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLASynchronousXMLOutputStoreTrue");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(false, true, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument) responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, containsString("statusLocation"));
//
//                String refResult = AllTestsIT.getAsyncDoc(response);
//                assertThat(refResult, refResult, containsString(echoProcessComplexOutputID));
//                assertThat(refResult, refResult, containsString(testDataNodeName));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexXMLASynchronousXMLOutputReferenceStoreTrue() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLASynchronousXMLOutputReferenceStoreTrue");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(false, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument) responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, containsString("statusLocation"));
//
//                String doc = AllTestsIT.getAsyncDoc(response);
//                String refResult = AllTestsIT.getRefAsString(doc);
//                assertThat(refResult, refResult, containsString(testDataNodeName));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexXMLASynchronousXMLOutputByReferenceStatusStoreTrue() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLASynchronousXMLOutputByReferenceStatusStoreTrue");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithResponseDocument(true, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument) responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, containsString("statusLocation"));
//
//                String doc = AllTestsIT.getAsyncDoc(response);
//                String refResult = AllTestsIT.getRefAsString(doc);
//                assertThat(refResult, refResult, containsString(testDataNodeName));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexXMLASynchronousRawXMLOutput() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexXMLASynchronousRawXMLOutput");
//
//        try {
//
//            Object responseObject =  createAndSubmitEchoProcessExecuteWithRawData();
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument) responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(response, response, containsString(testDataNodeName));
//                assertThat(response, response, not(containsString("Execute")));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexBinaryASynchronousBinaryOutputStoreStatusReferenceTrueHasCorrectSuffix() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexBinaryASynchronousBinaryOutputStoreStatusReferenceTrueHasCorrectSuffix");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("statusLocation"));
//
//                AllTestsIT.checkContentDispositionOfRetrieveResultServlet(response, null, ".tiff");
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTComplexBinaryASynchronousBinaryOutputStoreStatusReferenceTrueHasCorrectFilename() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTComplexBinaryASynchronousBinaryOutputStoreStatusReferenceTrueHasCorrectFilename");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("statusLocation"));
//
//                AllTestsIT.checkContentDispositionOfRetrieveResultServlet(response, "result", ".tiff");
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputBase64() throws ParserConfigurationException, IOException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, false, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("ExecuteResponse"));
//                AllTestsIT.checkInlineResultBase64(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//  //TODO This test will produce binary data in the XML response, which is not feasible
////    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, false, false, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("ExecuteResponse"));
//                assertThat(response, response, containsString(base64TiffStart));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, false, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("ExecuteResponse"));
//                AllTestsIT.checkReferenceBinaryResultBase64(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, false, true, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                AllTestsIT.checkReferenceBinaryResultDefault(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputStatusBase64() throws ParserConfigurationException, SAXException, IOException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputStatusBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, false, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("ExecuteResponse"));
//                assertThat(response, response, containsString("Status"));
//                AllTestsIT.checkInlineResultBase64(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//  //TODO This test will produce binary data in the XML response, which is not feasible
////    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputStatusNoEncoding() throws ParserConfigurationException, SAXException, IOException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputStatusNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, false, false, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("ProcessSucceeded"));
//                assertThat(response, response, containsString(base64TiffStart));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceStatusBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceStatusBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, false, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                AllTestsIT.checkReferenceBinaryResultBase64(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceStatusNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputAsReferenceStatusNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, false, true, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                AllTestsIT.checkReferenceBinaryResultDefault(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                AllTestsIT.validateBinaryBase64Async(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//  //TODO This test will produce binary data in the XML response, which is not feasible
////    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, false, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                String async = AllTestsIT.getAsyncDoc(response);
//                assertThat(AllTestsIT.parseXML(async), is(not(nullValue())));
//                assertThat(async, async, not(containsString("ExceptionReport")));
//                assertThat(async, async, containsString("ProcessSucceeded"));
//                assertThat(async, async, containsString(base64TiffStart));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreReferenceBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreReferenceBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                String asynDoc = AllTestsIT.getAsyncDoc(response);
//                assertThat(AllTestsIT.parseXML(asynDoc), is(not(nullValue())));
//                assertThat(asynDoc, asynDoc, not(containsString("ExceptionReport")));
//                AllTestsIT.checkReferenceBinaryResultBase64(asynDoc);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreReferenceNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreReferenceNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(false, true, true, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                String asynDoc = AllTestsIT.getAsyncDoc(response);
//                AllTestsIT.checkReferenceBinaryResultDefault(asynDoc);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreStatusBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreStatusBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, true, false);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                AllTestsIT.validateBinaryBase64Async(response);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //TODO This test will produce binary data in the XML response, which is not feasible
////    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreStatusNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputStoreStatusNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, true, false, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                String asyncDoc = AllTestsIT.getAsyncDoc(response);
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                assertThat(asyncDoc, asyncDoc, containsString("ProcessSucceeded"));
//                assertThat(asyncDoc, asyncDoc, containsString(base64TiffStart));
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputRawBase64() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputRawBase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, true, true);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                String refDoc = AllTestsIT.getAsyncDoc(response);
//                AllTestsIT.checkReferenceBinaryResultBase64(refDoc);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinaryASynchronousBinaryOutputReferenceStoreStatusNoEncoding() throws IOException, ParserConfigurationException, SAXException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinaryASynchronousBinaryOutputReferenceStoreStatusNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(true, true, true, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof ExecuteResponseDocument) {
//
//                ExecuteResponseDocument executeResponseDocument = (ExecuteResponseDocument)responseObject;
//
//                String response = executeResponseDocument.toString();
//
//                assertThat(response, response, not(containsString("ExceptionReport")));
//                assertThat(response, response, containsString("Status"));
//                assertThat(AllTestsIT.parseXML(response), is(not(nullValue())));
//                String refDoc = AllTestsIT.getAsyncDoc(response);
//                AllTestsIT.checkReferenceBinaryResultDefault(refDoc);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputRawbase64() throws IOException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputRawbase64");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithRawData(true, true, true, "base64");
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof InputStream) {
//
//                AllTestsIT.checkRawBinaryResultBase64((InputStream) responseObject);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testExecutePOSTValueComplexBinarySynchronousBinaryOutputRawNoEncoding() throws IOException {
//        System.out.println("\nRunning testExecutePOSTValueComplexBinarySynchronousBinaryOutputRawNoEncoding");
//
//        try {
//
//            Object responseObject =  createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithRawData(true, true, true, null);
//
//            assertThat(responseObject, is(not(nullValue())));
//            assertThat(responseObject, is(not(instanceOf(ExceptionReportDocument.class))));
//
//            if (responseObject instanceof InputStream) {
//
//                AllTestsIT.checkRawBinaryResultDefault((InputStream) responseObject);
//            }
//        } catch (WPSClientException e) {
//            e.printStackTrace();
//        }
//    }

    //    private Object createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(boolean status, boolean storeSupport, boolean asReference, String outputEncoding) throws WPSClientException, IOException{
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setResponseDocument(multiReferenceBinaryInputAlgorithmComplexOutputID, null, outputEncoding, multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
////        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStoreSupport(multiReferenceBinaryInputAlgorithmComplexOutputID, storeSupport);
////        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStatus(multiReferenceBinaryInputAlgorithmComplexOutputID, status);
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setAsReference(multiReferenceBinaryInputAlgorithmComplexOutputID, asReference);
//
//        Object responseObject =  WPSClientSession.getInstance().execute(url, multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.getExecute(), version200);
//
//        return responseObject;
//    }
//
//    private Object createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithResponseDocument(boolean status, boolean storeSupport, boolean asReference) throws WPSClientException, IOException{
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setResponseDocument(multiReferenceBinaryInputAlgorithmComplexOutputID, null, "base64", multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
////        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStoreSupport(multiReferenceBinaryInputAlgorithmComplexOutputID, storeSupport);
////        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setStatus(multiReferenceBinaryInputAlgorithmComplexOutputID, status);
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setAsReference(multiReferenceBinaryInputAlgorithmComplexOutputID, asReference);
//
//        Object responseObject =  WPSClientSession.getInstance().execute(url, multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.getExecute(), version200);
//
//        return responseObject;
//    }
//
//    private Object createAndSubmitMultiReferenceBinaryInputAlgorithmExecuteWithRawData(boolean status, boolean storeSupport, boolean asReference, String encoding) throws WPSClientException, IOException{
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.addComplexDataReference(multiReferenceBinaryInputAlgorithmComplexInputID,
//                                                                                        referenceComplexBinaryInputURL,
//                                                                                        null,
//                                                                                        null,
//                                                                                        multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.setRawData(multiReferenceBinaryInputAlgorithmComplexOutputID, null, encoding, multiReferenceBinaryInputAlgorithmComplexMimeTypeImageTiff);
//
//        Object responseObject =  WPSClientSession.getInstance().execute(url, multiReferenceBinaryInputAlgorithmExecuteRequestBuilder.getExecute(), version200);
//
//        return responseObject;
//    }
//
    private Object createAndSubmitEchoProcessExecuteWithResponseDocument(boolean async, boolean asReference) throws WPSClientException, IOException {

        echoProcessExecuteRequestBuilder.reset();

        echoProcessExecuteRequestBuilder.addComplexData(echoProcessComplexInputID, echoProcessInlineComplexXMLInput, null, null, echoProcessComplexMimeTypeTextXML);

        echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessComplexOutputID, null, null, echoProcessComplexMimeTypeTextXML);

//        echoProcessExecuteRequestBuilder.setStoreSupport(echoProcessComplexOutputID, storeSupport);
//        echoProcessExecuteRequestBuilder.setStatus(echoProcessComplexOutputID, status);

        echoProcessExecuteRequestBuilder.setAsReference(echoProcessComplexOutputID, asReference);

        if(async) {
            echoProcessExecuteRequestBuilder.setAsynchronousExecute();
        }



        Object responseObject =  WPSClientSession.getInstance().execute(url, echoProcessExecuteRequestBuilder.getExecute(), version200);

        return responseObject;
    }
//
    private synchronized Object createAndSubmitEchoProcessExecuteForConcurrencyTest(int duration, String literalValue) throws WPSClientException, IOException {

        echoProcessExecuteRequestBuilder.reset();

        echoProcessExecuteRequestBuilder.addLiteralData("duration", "" + duration, null, null, echoProcessLiteralMimeTypeTextXML);
        echoProcessExecuteRequestBuilder.addLiteralData(echoProcessLiteralInputID, literalValue, null, null, echoProcessLiteralMimeTypeTextXML);

        echoProcessExecuteRequestBuilder.setResponseDocument(echoProcessLiteralOutputID, null, null, echoProcessLiteralMimeTypeTextXML);

        echoProcessExecuteRequestBuilder.setAsynchronousExecute();

        Object responseObject =  WPSClientSession.getInstance().executeAsyncGetResponseImmediately(url, echoProcessExecuteRequestBuilder.getExecute(), version200);

        return responseObject;
    }

    private void checkIdentifier(Result result, String outputID){

        String identifier = getFirstOutputData(result).getId();

        assertThat(identifier, is(equalTo(outputID)));
    }

    private Data getFirstOutputData(Result result){
        List<Data> outputs = result.getOutputs();

        assertThat(outputs, not(nullValue()));
        assertThat(outputs.size(), not(0));

        Data outputData= outputs.get(0);

        return outputData;
    }

    private Data getData(Result result, String outputID) {

        List<Data> outputs = result.getOutputs();

        assertThat(outputs, not(nullValue()));
        assertThat(outputs.size(), not(0));

        Data outputData = null;

        for (Data data : outputs) {
            if(data.getId().equals(outputID)) {
                outputData = data;
                break;
            }
        }

        return outputData;
    }

    private void checkOutputContainsValue(Result result,
            String outputID,
            String value) {
        Data data = getData(result, outputID);

        assertNotNull("Data must not be null.", data);

        Object valueObj =  data.getValue();

        assertNotNull("Data value must not be null.", valueObj);
        assertThat("Data value must be String.", valueObj, is(instanceOf(String.class)));
        assertThat("Data value must contain " + value, ((String)valueObj).contains(value));
    }

}
