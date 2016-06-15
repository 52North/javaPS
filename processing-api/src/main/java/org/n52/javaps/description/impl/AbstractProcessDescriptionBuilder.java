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
package org.n52.javaps.description.impl;

import static com.google.common.base.Strings.emptyToNull;

import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableMap;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessDescriptionBuilder<T extends ProcessDescription, B extends ProcessDescriptionBuilder<T, B>>
        extends AbstractDescriptionBuilder<T, B> implements ProcessDescriptionBuilder<T, B> {

    private final ImmutableMap.Builder<OwsCodeType, ProcessInputDescription> inputs = ImmutableMap.builder();
    private final ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> outputs = ImmutableMap.builder();
    private boolean storeSupported = false;
    private boolean statusSupported = false;
    private String version;

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withVersion(String version) {
        this.version = Objects.requireNonNull(emptyToNull(version));
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B storeSupported(boolean storeSupported) {
        this.storeSupported = storeSupported;
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B statusSupported(boolean statusSupported) {
        this.statusSupported = statusSupported;
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withInput(ProcessInputDescription input) {
        if (input != null) {
            inputs.put(input.getId(), input);
        }
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withOutput(ProcessOutputDescription output) {
        if (output != null) {
            outputs.put(output.getId(), output);
        }
        return (B) this;
    }

    ImmutableMap.Builder<OwsCodeType, ProcessInputDescription> getInputs() {
        return this.inputs;
    }

    ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> getOutputs() {
        return this.outputs;
    }

    boolean isStoreSupported() {
        return this.storeSupported;
    }

    boolean isStatusSupported() {
        return this.statusSupported;
    }

    String getVersion() {
        return this.version;
    }

}
