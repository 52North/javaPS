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
package org.n52.javaps.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;

@Properties(defaultPropertyFileName="test.properties")
public class TestAnnotation implements ConfigurableClass {

    public TestAnnotation(){

        assertThat(getProperties(), not(nullValue()));

        Object testValue = getProperties().get("testkey").asText();

        assertThat(testValue, not(nullValue()));
        assertThat(testValue.toString(), equalTo("testvalue"));
    }

    @Test
    public void testAnnotation() {
        TestAnnotation testAnnotation = new TestAnnotation();
    }

}
