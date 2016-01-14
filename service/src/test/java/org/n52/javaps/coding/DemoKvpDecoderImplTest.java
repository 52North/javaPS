/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.coding;

import org.n52.javaps.coding.DemoKvpDecoderImpl;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.n52.iceland.exception.ows.CompositeOwsException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.request.DemoRequest;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class DemoKvpDecoderImplTest {

    @Test
    public void testDecode() throws OwsExceptionReport {
        Map<String, String> requestMap = ImmutableMap.of("service", "1", "version", "2", "request", "demo",
                "one", "onevalue", "two", "52");

        DemoKvpDecoderImpl decoder = new DemoKvpDecoderImpl();
        DemoRequest request = decoder.decode(requestMap);

        assertThat(request.getOne(), is("onevalue"));
        assertThat(request.getTwo(), is(52));
        assertThat(request.getService(), is("1"));
        assertThat(request.getVersion(), is("2"));
    }

    @Test(expected = CompositeOwsException.class)
    public void testDecodeTwoNotANumber() throws OwsExceptionReport {
        Map<String, String> requestMap = ImmutableMap.of("service", "1", "version", "2", "request", "demo",
                "one", "onevalue", "two", "twovaluenotanumber");
        DemoKvpDecoderImpl decoder = new DemoKvpDecoderImpl();
        decoder.decode(requestMap);
    }

    @Test(expected = CompositeOwsException.class)
    public void testDecodeUnsupportedParameters() throws OwsExceptionReport {
        Map<String, String> requestMap = ImmutableMap.of("stuff", "stuff");
        DemoKvpDecoderImpl decoder = new DemoKvpDecoderImpl();
        decoder.decode(requestMap);
    }

}
