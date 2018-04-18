/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service.xml;





import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsNull.notNullValue;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.xml.sax.SAXException;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.ExecutionMode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.JobId;
import org.n52.shetland.ogc.wps.OutputDefinition;
import org.n52.shetland.ogc.wps.ResponseMode;
import org.n52.shetland.ogc.wps.data.Body;
import org.n52.shetland.ogc.wps.data.GroupProcessData;
import org.n52.shetland.ogc.wps.data.ReferenceProcessData;
import org.n52.shetland.ogc.wps.data.impl.StringValueProcessData;
import org.n52.shetland.ogc.wps.request.DescribeProcessRequest;
import org.n52.shetland.ogc.wps.request.DismissRequest;
import org.n52.shetland.ogc.wps.request.ExecuteRequest;
import org.n52.shetland.ogc.wps.request.GetResultRequest;
import org.n52.shetland.ogc.wps.request.GetStatusRequest;

import com.google.common.io.BaseEncoding;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WPSRequestReaderTest {
    private static final XMLInputFactory INPUT_FACTORY;
    private static final SchemaFactory SCHEMA_FACTORY;
    private static final Schema SCHEMA;

    static {
        try {
            SCHEMA_FACTORY = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            SCHEMA = SCHEMA_FACTORY.newSchema(WPSRequestReaderTest.class.getResource("/xsd/wps/2.0.0/wps.xsd"));
        } catch (SAXException ex) {
            throw new Error(ex);
        }
    }

    static {
        INPUT_FACTORY = XMLInputFactory.newFactory();
        INPUT_FACTORY.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        INPUT_FACTORY.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        INPUT_FACTORY.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        INPUT_FACTORY.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        INPUT_FACTORY.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    }

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void testGetStatus() throws Exception {
        GetStatusRequest request = read("wpsGetStatusRequestExample.xml");
        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getVersion(), is("2.0.0"));
        errors.checkThat(request.getJobId(), is(new JobId("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66")));
    }

    @Test
    public void testDescribeProcess() throws Exception {
        DescribeProcessRequest request = read("wpsDescribeProcessRequestExample.xml");
        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getVersion(), is("2.0.0"));
        errors.checkThat(request.getProcessIdentifier(), contains(new OwsCode("Buffer"), new OwsCode("Viewshed")));
    }

    @Test
    public void testDismissProcess() throws Exception {
        DismissRequest request = read("wpsDismissRequestExample.xml");
        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getVersion(), is("2.0.0"));
        errors.checkThat(request.getJobId(), is(new JobId("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66")));
    }

    @Test
    public void testGetCapabilitiesRequest() throws Exception {
        GetCapabilitiesRequest request = read("wpsGetCapabilitiesRequestExample.xml");

        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getUpdateSequence(), is(nullValue()));
        errors.checkThat(request.getAcceptFormats(), contains("text/xml"));
        errors.checkThat(request.getAcceptLanguages(), contains("en", "de"));
        errors.checkThat(request.getAcceptVersions(), contains("2.0.0", "1.0.0"));
        errors.checkThat(request.getSections(), containsInAnyOrder("ServiceIdentification", "ServiceProvider", "OperationsMetadata", "Languages", "Contents"));
    }

    @Test
    public void testGetResultRequest() throws Exception {
        GetResultRequest request = read("wpsGetResultRequestExample.xml");
        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getVersion(), is("2.0.0"));
        errors.checkThat(request.getJobId(), is(new JobId("FB6DD4B0-A2BB-11E3-A5E2-0800200C9A66")));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testExecuteRequest() throws Exception {
        ExecuteRequest request = read("wpsExecuteRequestExample.xml");
        errors.checkThat(request, is(notNullValue()));
        errors.checkThat(request.getService(), is("WPS"));
        errors.checkThat(request.getVersion(), is("2.0.0"));
        errors.checkThat(request.getExecutionMode(), is(ExecutionMode.ASYNC));
        errors.checkThat(request.getResponseMode(), is(ResponseMode.DOCUMENT));
        errors.checkThat(request.getId(), is(new OwsCode("http://my.wps.server/processes/proximity/Planar-Buffer")));

        errors.checkThat(request.getOutputs().size(), is(1));
        errors.checkThat(request.getOutputs(), contains(new OutputDefinition(new OwsCode("output1"), Format.empty(), DataTransmissionMode.REFERENCE)));
        errors.checkThat(request.getInputs(), contains(is(new ReferenceProcessData(new OwsCode("referenceInput1"), new Format("application/xml", "UTF-8"), URI.create("http://some.data.server/mygmldata.xml"))),
                is(new ReferenceProcessData(new OwsCode("referenceInput2"), new Format("text/xml"), URI.create("http://some.data.server/mygmldata.xml"), Body.inline("\n                <my-great-request-bayload>hello world</my-great-request-bayload>\n            "))),
                is(new ReferenceProcessData(new OwsCode("referenceInput3"), new Format(null, (String)null, "urn::schema:test"), URI.create("http://some.data.server/mygmldata.xml"), Body.inline("\n                <my-great-request-bayload>hello world</my-great-request-bayload>\n            "))),
                is(new ReferenceProcessData(new OwsCode("referenceInput4"), new Format(null, (String)null, "urn::schema:test"), URI.create("http://some.data.server/mygmldata.xml"), Body.reference("http://some.data.server/mygmldata.xml"))),
                is(new StringValueProcessData(new OwsCode("literalInput1"), new Format(), "10.0")),
                is(new StringValueProcessData(new OwsCode("literalInput2"), new Format(), "hello world")),
                is(new StringValueProcessData(new OwsCode("complexInput1"), new Format("text/xml", "UTF-8", "urn::schema:test"), "\n" +
                        "            <great-xml-snippet xmlns:wps=\"http://www.opengis.net/wps/2.0\" xmlns=\"urn::schema:test\">\n" +
"                <ows:Identifier xmlns:ows=\"http://www.opengis.net/ows/2.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"asdf\">asdf</ows:Identifier>\n" +
"            </great-xml-snippet>\n        ")),
                is(new StringValueProcessData(new OwsCode("complexInput2"), new Format("text/xml", "base64"), BaseEncoding.base64().encode("<sömekey>somevalüe</sömekey>".getBytes(StandardCharsets.UTF_8)))),
                is(new GroupProcessData(new OwsCode("outerNested"), Arrays.asList(new StringValueProcessData(new OwsCode("innerNested1"), "10.0"), new StringValueProcessData(new OwsCode("innerNested2"), "10.0"))))));
    }


    @SuppressWarnings("unchecked")
    private <T> T read(String file) throws XMLStreamException {
        XMLEventReader reader = INPUT_FACTORY.createXMLEventReader(WPSRequestReaderTest.class.getResourceAsStream("/" + file));
        return (T) new WPSRequestReader().readElement(reader);
    }

}
