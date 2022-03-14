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
package org.n52.javaps.rest.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.n52.javaps.engine.InputDecodingException;
import org.n52.javaps.rest.deserializer.ExecuteDeserializer;
import org.n52.javaps.rest.model.Input;
import org.n52.shetland.ogc.wps.data.ProcessData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExecuteDeserializerTest {

    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEmptyFormatComplexDataValue() {

        ObjectNode complexDataNode = objectMapper.createObjectNode();

        ObjectNode inlineValueNode = objectMapper.createObjectNode();

        inlineValueNode.put("inlineValue", "<test/>");

        complexDataNode.set("value", inlineValueNode);

        Input input1 = new Input();

        input1.setId("input1");

        input1.setInput(complexDataNode);

        List<Input> inputs = new ArrayList<Input>(3);

        inputs.add(input1);

        List<ProcessData> processData = null;

        try {
             processData = new ExecuteDeserializer(objectMapper).readInputs(inputs);
        } catch (InputDecodingException e) {
           fail(e.getMessage());
        }

        assertTrue(processData != null);
        assertTrue(processData.size() == 1);
        assertTrue(processData.get(0) != null);
        assertTrue(processData.get(0).isValue());
    }

    @Test
    public void testEmptyFormatComplexDataReference() {

        ObjectNode complexDataNode = objectMapper.createObjectNode();

        ObjectNode hrefNode = objectMapper.createObjectNode();

        hrefNode.put("href", "http://xy.z");

        complexDataNode.set("value", hrefNode);

        Input input1 = new Input();

        input1.setId("input1");

        input1.setInput(complexDataNode);

        List<Input> inputs = new ArrayList<Input>(3);

        inputs.add(input1);

        List<ProcessData> processData = null;

        try {
             processData = new ExecuteDeserializer(objectMapper).readInputs(inputs);
        } catch (InputDecodingException e) {
           fail(e.getMessage());
        }

        assertTrue(processData != null);
        assertTrue(processData.size() == 1);
        assertTrue(processData.get(0) != null);
        assertTrue(processData.get(0).isReference());
    }

    @Test
    public void testEmptyFormatLiteralData() {

        ObjectNode literalDataNode = objectMapper.createObjectNode();

        literalDataNode.put("datatype", "double");
        literalDataNode.put("value", 0.05);

        Input input1 = new Input();

        input1.setId("input1");

        input1.setInput(literalDataNode);

        List<Input> inputs = new ArrayList<Input>(3);

        inputs.add(input1);

        List<ProcessData> processData = null;

        try {
             processData = new ExecuteDeserializer(objectMapper).readInputs(inputs);
        } catch (InputDecodingException e) {
           fail(e.getMessage());
        }

        assertTrue(processData != null);
        assertTrue(processData.size() == 1);
        assertTrue(processData.get(0) != null);
        assertTrue(processData.get(0).isValue());
    }

    @Test
    public void testEmptyFormatBBox() {

        ObjectNode bboxNode = objectMapper.createObjectNode();

        ArrayNode arrayNode = bboxNode.arrayNode();
        arrayNode.add(50);
        arrayNode.add(7);
        arrayNode.add(51);
        arrayNode.add(8);
        bboxNode.set("bbox", arrayNode);
        bboxNode.put("crs", "EPSG:4326");

        Input input1 = new Input();

        input1.setId("input1");

        input1.setInput(bboxNode);

        List<Input> inputs = new ArrayList<Input>(3);

        inputs.add(input1);

        List<ProcessData> processData = null;

        try {
             processData = new ExecuteDeserializer(objectMapper).readInputs(inputs);
        } catch (InputDecodingException e) {
           fail(e.getMessage());
        }

        assertTrue(processData != null);
        assertTrue(processData.size() == 1);
        assertTrue(processData.get(0) != null);
        assertTrue(processData.get(0).isValue());
    }

//    @Test
//    public void testEmptyFormatOutput() {
//
//        Format format = new Format("text/my-mimetype");
//
//        ComplexDescription complexDescription = TypedProcessDescriptionFactory.instance().complexOutput().withDefaultFormat(format).withIdentifier("output1").withType(GenericXMLDataBinding.class).build();
//
//        String outputJSON = "{\"id\":\"output1\",\"transmissionMode\":\"value\"}";
//
//        JsonParser jsonParser = null;
//        try {
//            jsonParser = objectMapper.getFactory().createParser(outputJSON);
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }
//
//        Output output = null;
//        try {
//            output = objectMapper.reader().readValue(jsonParser, Output.class);
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }
//
//        List<Output> outputs = new ArrayList<Output>(1);
//
//        outputs.add(output);
//
//        List<OutputDefinition> outputDefinitions = null;
//
//        outputDefinitions = new ExecuteDeserializer(objectMapper).readOutputs(outputs);
//
//        assertTrue(outputDefinitions != null);
//        assertTrue(outputDefinitions.size() == 1);
//        OutputDefinition outputDefinition = outputDefinitions.get(0);
//        assertTrue(outputDefinition != null);
//        assertTrue(outputDefinition.getFormat().equals(format));
//    }

}
