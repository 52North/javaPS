/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.io.literal;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.iceland.ogc.wps.Format;
import org.n52.iceland.ogc.wps.description.LiteralDataDomain;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralInputDescription;
import org.n52.iceland.ogc.wps.description.typed.TypedLiteralOutputDescription;
import org.n52.iceland.ogc.wps.description.typed.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.DecodingException;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.literal.xsd.LiteralIntegerType;
import org.n52.javaps.io.literal.xsd.LiteralStringType;

import com.google.common.io.CharStreams;

/**
 * @author Christian Autermann
 */
public class LiteralDataHandlerTest {
    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private LiteralDataHandler handler;
    private TypedProcessDescriptionFactory descriptionFactory;

    @Before
    public void setup() {
        this.handler = new LiteralDataHandler();
        this.descriptionFactory = new TypedProcessDescriptionFactory();
    }

    @Test
    public void testPlainDecodingBigInteger() throws IOException, DecodingException {
        ByteArrayInputStream input = new ByteArrayInputStream("1234123412341234".getBytes(StandardCharsets.UTF_8));
        Data<?> decode = this.handler.parse(input(new LiteralIntegerType()), input, new Format("text/plain"));
        errors.checkThat(decode, is(notNullValue()));
        errors.checkThat(decode, is(instanceOf(LiteralData.class)));
        errors.checkThat(decode.getPayload(), is(instanceOf(BigInteger.class)));
        errors.checkThat((BigInteger) decode.getPayload(), is(BigInteger.valueOf(1234123412341234l)));
    }

    @Test
    public void testPlainDecodingString() throws IOException, DecodingException {
        String value = "läöaaslödfr2";
        ByteArrayInputStream input = new ByteArrayInputStream(value.getBytes(StandardCharsets.ISO_8859_1));
        Data<?> decode = this.handler.parse(input(new LiteralStringType()), input, new Format("text/plain", StandardCharsets.ISO_8859_1));
        errors.checkThat(decode, is(notNullValue()));
        errors.checkThat(decode, is(instanceOf(LiteralData.class)));
        errors.checkThat(decode.getPayload(), is(instanceOf(String.class)));
        errors.checkThat((String) decode.getPayload(), is(value));
    }

    @Test
    public void testXmlDecodingString() throws IOException, DecodingException {
        String value = "<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" uom=\"m\">läöaaslödfr2</wps:LiteralValue>";
        ByteArrayInputStream input = new ByteArrayInputStream(value.getBytes(StandardCharsets.ISO_8859_1));
        Data<?> decode = this.handler.parse(input(new LiteralStringType()), input, new Format("application/xml", StandardCharsets.ISO_8859_1));
        errors.checkThat(decode, is(notNullValue()));
        errors.checkThat(decode, is(instanceOf(LiteralData.class)));
        errors.checkThat(decode.getPayload(), is(instanceOf(String.class)));
        errors.checkThat((String) decode.getPayload(), is("läöaaslödfr2"));
        errors.checkThat(((LiteralData)decode).getUnitOfMeasurement(), is(Optional.of("m")));
    }

    @Test
    public void testPlainEncodingString() throws IOException, EncodingException {
        String value = "läöaaslödfr2";
        InputStream stream = this.handler.generate(output(new LiteralStringType()), new LiteralData(value, "m"), new Format("text/plain", StandardCharsets.ISO_8859_1));
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is(value));
        }
    }

    @Test
    public void testXmlEncodingString() throws IOException, EncodingException {
        InputStream stream = this.handler.generate(output(new LiteralStringType()), new LiteralData("läöaaslödfr2", "m"), new Format("text/xml", StandardCharsets.ISO_8859_1));
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is("<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" dataType=\"https://www.w3.org/2001/XMLSchema-datatypes#string\" uom=\"m\">läöaaslödfr2</wps:LiteralValue>"));
        }
    }

    @Test
    public void testXmlEncodingBigInteger() throws IOException, EncodingException {
        InputStream stream = this.handler.generate(output(new LiteralIntegerType()), new LiteralData(BigInteger.valueOf(1234123412341234l), "m"), new Format("text/xml", StandardCharsets.ISO_8859_1));
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.ISO_8859_1)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is("<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" dataType=\"https://www.w3.org/2001/XMLSchema-datatypes#integer\" uom=\"m\">1234123412341234</wps:LiteralValue>"));
        }
    }

    private TypedLiteralInputDescription input(LiteralType<?> dataType) {
        LiteralDataDomain literalDataDomain = descriptionFactory.literalDataDomain()
                .withDataType(dataType.getDataType())
                .build();
        return descriptionFactory.literalInput()
                        .withIdentifier("input")
                        .withDefaultLiteralDataDomain(literalDataDomain)
                        .withSupportedLiteralDataDomain(literalDataDomain)
                        .withType(dataType)
                        .build();
    }

    private TypedLiteralOutputDescription output(LiteralType<?> dataType) {
        LiteralDataDomain literalDataDomain = descriptionFactory.literalDataDomain()
                .withDataType(dataType.getDataType())
                .build();
        return descriptionFactory.literalOutput()
                        .withIdentifier("input")
                        .withDefaultLiteralDataDomain(literalDataDomain)
                        .withSupportedLiteralDataDomain(literalDataDomain)
                        .withType(dataType)
                        .build();
    }

}
