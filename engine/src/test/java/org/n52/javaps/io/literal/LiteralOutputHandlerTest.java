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
package org.n52.javaps.io.literal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.javaps.description.TypedLiteralOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.EncodingException;
import org.n52.javaps.io.OutputHandler;
import org.n52.javaps.io.literal.xsd.LiteralIntegerType;
import org.n52.javaps.io.literal.xsd.LiteralStringType;

import com.google.common.io.CharStreams;

/**
 * @author Christian Autermann
 */
public class LiteralOutputHandlerTest {
    @Rule
    public ErrorCollector errors = new ErrorCollector();

    private OutputHandler handler;
    private TypedProcessDescriptionFactory descriptionFactory;

    @Before
    public void setup() {
        this.handler = new LiteralInputOutputHandler();
        this.descriptionFactory = new TypedProcessDescriptionFactory();
    }

    @Test
    public void testPlainEncodingString() throws IOException, EncodingException {
        String value = "läöaaslödfr2";
        Charset charset = StandardCharsets.ISO_8859_1;
        Format format = Format.TEXT_PLAIN.withEncoding(charset);
        LiteralData data = new LiteralData(value, "m");
        InputStream stream = this.handler.generate(output(new LiteralStringType()), data, format);
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, charset)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is(value));
        }
    }

    @Test
    public void testXmlEncodingString() throws IOException, EncodingException {
        LiteralData data = new LiteralData("läöaaslödfr2", "m");
        Charset charset = StandardCharsets.ISO_8859_1;
        Format format = Format.TEXT_XML.withEncoding(charset);
        InputStream stream = this.handler.generate(output(new LiteralStringType()), data, format);
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, charset)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is("<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" dataType=\"https://www.w3.org/2001/XMLSchema-datatypes#string\" uom=\"m\">läöaaslödfr2</wps:LiteralValue>"));
        }
    }

    @Test
    public void testXmlEncodingBigInteger() throws IOException, EncodingException {
        Charset charset = StandardCharsets.ISO_8859_1;
        Format format = Format.TEXT_XML.withEncoding(charset);
        LiteralData data = new LiteralData(BigInteger.valueOf(1234123412341234l), "m");
        InputStream stream = this.handler.generate(output(new LiteralIntegerType()), data, format);
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, charset)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is("<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" dataType=\"https://www.w3.org/2001/XMLSchema-datatypes#integer\" uom=\"m\">1234123412341234</wps:LiteralValue>"));
        }
    }

    @Test
    public void testXmlEncodingEmptyFormat() throws IOException, EncodingException {
        LiteralData data = new LiteralData("läöaaslödfr2", "m");
        Charset charset = StandardCharsets.UTF_8;
        InputStream stream = this.handler.generate(output(new LiteralStringType()), data, new Format());
        errors.checkThat(stream, is(notNullValue()));
        try (InputStreamReader reader = new InputStreamReader(stream, charset)) {
            String encodedValue = CharStreams.toString(reader);
            errors.checkThat(encodedValue, is("<wps:LiteralValue xmlns:wps=\"http://www.opengis.net/wps/2.0\" dataType=\"https://www.w3.org/2001/XMLSchema-datatypes#string\" uom=\"m\">läöaaslödfr2</wps:LiteralValue>"));
        }
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
