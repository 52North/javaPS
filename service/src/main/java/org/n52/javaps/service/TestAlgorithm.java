/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.service;

import org.n52.javaps.algorithm.annotation.Algorithm;
import org.n52.javaps.algorithm.annotation.Execute;
import org.n52.javaps.algorithm.annotation.LiteralInput;
import org.n52.javaps.algorithm.annotation.LiteralOutput;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Algorithm(version = "1.0.0")
public class TestAlgorithm {
    private String input1;
    private String input2;
    private String output1;
    private String output2;

    @LiteralInput(identifier = "input1")
    public void setInput1(String value) {
        this.input1 = value;
    }

    @LiteralInput(identifier = "input2")
    public void setInput2(String value) {
        this.input2 = value;
    }

    @Execute
    public void execute() {
        this.output1 = input1;
        this.output2 = input2;
    }

    @LiteralOutput(identifier = "output1")
    public String getOutput1() {
        return this.output1;
    }

    @LiteralOutput(identifier = "output2")
    public String getOutput2() {
        return this.output2;
    }

}
