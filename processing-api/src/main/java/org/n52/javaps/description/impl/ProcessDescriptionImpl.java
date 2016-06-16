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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessOutputDescription;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessDescriptionImpl extends AbstractDescription implements
        ProcessDescription {

    private final Map<OwsCodeType, ProcessInputDescription> inputs;
    private final Map<OwsCodeType, ProcessOutputDescription> outputs;
    private final boolean storeSupported;
    private final boolean statusSupported;
    private final String version;

    protected ProcessDescriptionImpl(
            AbstractProcessDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.version = Objects.requireNonNull(builder.getVersion());
        this.inputs = builder.getInputs();
        this.outputs = builder.getOutputs();
        this.storeSupported = builder.isStoreSupported();
        this.statusSupported = builder.isStatusSupported();
    }

    @Override
    public ProcessInputDescription getInput(OwsCodeType id) {
        return this.inputs.get(id);
    }

    @Override
    public ProcessOutputDescription getOutput(OwsCodeType id) {
        return this.outputs.get(id);
    }

    @Override
    public Set<OwsCodeType> getInputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCodeType> getOutputs() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    @Override
    public Collection<? extends ProcessOutputDescription> getOutputDescriptions() {
        return Collections.unmodifiableCollection(outputs.values());
    }

    @Override
    public boolean isStoreSupported() {
        return storeSupported;
    }

    @Override
    public boolean isStatusSupported() {
        return statusSupported;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public static ProcessDescriptionBuilder<?, ?> builder() {
        return new Builder();
    }

    private static class Builder extends AbstractProcessDescriptionBuilder<ProcessDescriptionImpl, Builder> {
        @Override
        public ProcessDescriptionImpl build() {
            return new ProcessDescriptionImpl(this);
        }
    }

}
