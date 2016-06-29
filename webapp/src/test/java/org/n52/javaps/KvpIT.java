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
package org.n52.javaps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.hasXPath;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.xmlmatchers.xpath.XpathReturnType.returningAString;

import java.io.IOException;

import org.junit.Test;
import org.xmlmatchers.xpath.XpathReturnType;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class KvpIT extends Base {

    @Test
    public void testGetCapabilities() throws IOException {
        String response = kvp("service=skeleton&request=GetCapabilities");

        assertThat("document contains root element", the(response),
                hasXPath("/ows:Capabilities", usingNamespaces));
        assertThat("service type version is correct in root", the(response),
                hasXPath("/ows:Capabilities/@version", usingNamespaces, returningAString(), equalTo("0.0.1")));
    }

    @Test
    public void testDemo() throws IOException {
        String response = kvp("service=skeleton&request=demo&one=52North&two=48151");

        assertThat("document contains root element", the(response),
                hasXPath("/DemoResponse", usingNamespaces));
        assertThat("parameters three is corret", the(response),
                hasXPath("count(/*/three[1])", usingNamespaces, XpathReturnType.returningANumber(), equalTo(2.0)));
        assertThat("parameters three is corret", the(response),
                hasXPath("/*/three[1]", usingNamespaces, returningAString(), equalTo("htroN25")));
        assertThat("parameter three is corret", the(response),
                hasXPath("/*/three[2]", usingNamespaces, returningAString(), equalTo("15184")));

    }
}
