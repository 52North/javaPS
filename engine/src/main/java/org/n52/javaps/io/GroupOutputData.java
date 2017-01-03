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
package org.n52.javaps.io;

import java.util.Objects;

import org.n52.javaps.algorithm.ProcessOutputs;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GroupOutputData implements Data<ProcessOutputs> {
    private static final long serialVersionUID = 7948964429686164730L;

    private final ProcessOutputs payload;

    public GroupOutputData(ProcessOutputs payload) {
        this.payload = Objects.requireNonNull(payload);
    }

    @Override
    public ProcessOutputs getPayload() {
        return this.payload;
    }

    @Override
    public Class<?> getSupportedClass() {
        return ProcessOutputs.class;
    }
}
