/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.algorithm.descriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.commons.WPSConfig;

import com.google.common.base.Preconditions;

/**
 *
 * @author tkunicki
 */
public class ProcessDescription extends Description {

    private final String version;

    private final boolean storeSupported;

    private final boolean statusSupported;

    private final String outputTransmissionMode;

    private final String jobControlOption;

    private final Map<OwsCodeType, InputDescription> inputDescriptorMap;

    private final Map<OwsCodeType, OutputDescription> outputDescriptorMap;

    ProcessDescription(Builder<? extends Builder<?>> builder) {
        super(builder);
        this.version = builder.version;
        this.storeSupported = builder.storeSupported;
        this.statusSupported = builder.statusSupported;
        this.outputTransmissionMode = builder.outputTransmissionMode;
        this.jobControlOption = builder.jobControlOption;

        Preconditions.checkState(builder.outputDescriptors.size() > 0, "Need at minimum 1 output for algorithm.");

        this.inputDescriptorMap = Collections.unmodifiableMap(builder.inputDescriptors.stream().collect(Collectors.toMap(InputDescription<?>::getIdentifier, Function.identity())));
        this.outputDescriptorMap = Collections.unmodifiableMap(builder.outputDescriptors.stream().collect(Collectors.toMap(OutputDescription<?>::getIdentifier, Function.identity())));

    }

    public String getVersion() {
        return version;
    }

    public boolean getStoreSupported() {
        return storeSupported;
    }

    public boolean getStatusSupported() {
        return statusSupported;
    }

    public String getOutputTransmissionMode() {
        return outputTransmissionMode;
    }

    public String getJobControlOption() {
        return jobControlOption;
    }

    public List<String> getInputIdentifiers() {
        return Collections.unmodifiableList(new ArrayList<String>(inputDescriptorMap.keySet()));
    }

    public InputDescription getInputDescriptor(String identifier) {
        return inputDescriptorMap.get(identifier);
    }

    public Collection<InputDescription> getInputDescriptors() {
        return inputDescriptorMap.values();
    }

    public List<String> getOutputIdentifiers() {
        return Collections.unmodifiableList(new ArrayList<OwsCodeType>(outputDescriptorMap.keySet()));
    }

    public OutputDescription getOutputDescriptor(String identifier) {
        return outputDescriptorMap.get(identifier);
    }

    public Collection<OutputDescription> getOutputDescriptors() {
        return outputDescriptorMap.values();
    }

    public static Builder<?> builder(String identifier) {
        return new BuilderTyped(identifier);
    }

    public static Builder<?> builder(Class<?> clazz) {
        Preconditions.checkNotNull(clazz, "clazz may not be null");
        return new BuilderTyped(clazz.getCanonicalName());
    }

    private static class BuilderTyped extends Builder<BuilderTyped> {
        public BuilderTyped(String identifier) {
            super(identifier);
        }

        @Override
        protected BuilderTyped self() {
            return this;
        }
    }

    public static abstract class Builder<B extends Builder<B>> extends Description.Builder<B> {

        private String version = "1.0.0";

        private boolean storeSupported = true;

        private boolean statusSupported = true;

        private String outputTransmissionMode = WPSConfig.OUTPUT_TRANSMISSION_VALUE;// TODO
                                                                                    // use
                                                                                    // WPS200Constants

        private String jobControlOption = WPSConfig.JOB_CONTROL_OPTION_SYNC_EXECUTE;// TODO
                                                                                    // use
                                                                                    // WPS200Constants

        private List<InputDescription<?>> inputDescriptors;

        private List<OutputDescription<?>> outputDescriptors;

        protected Builder(String identifier) {
            super(identifier);
            title(identifier);
            inputDescriptors = new ArrayList<>();
            outputDescriptors = new ArrayList<>();
        }

        public B version(String version) {
            this.version = version;
            return self();
        }

        public B storeSupported(boolean storeSupported) {
            this.storeSupported = storeSupported;
            return self();
        }

        public B statusSupported(boolean statusSupported) {
            this.statusSupported = statusSupported;
            return self();
        }

        public B outputTransmissionMode(String outputTransmissionMode) {
            this.outputTransmissionMode = outputTransmissionMode;
            return self();
        }

        public B jobControlOption(String jobControlOption) {
            this.jobControlOption = jobControlOption;
            return self();
        }

        public B addInputDescriptor(InputDescription.Builder inputDescriptorBuilder) {
            return addInputDescriptor(inputDescriptorBuilder.build());
        }

        public B addInputDescriptor(InputDescription inputDescriptor) {
            this.inputDescriptors.add(inputDescriptor);
            return self();
        }

        public B addInputDescriptors(List<? extends InputDescription> inputDescriptors) {
            this.inputDescriptors.addAll(inputDescriptors);
            return self();
        }

        public B addOutputDescriptor(OutputDescription.Builder outputDescriptorBuilder) {
            return addOutputDescriptor(outputDescriptorBuilder.build());
        }

        public B addOutputDescriptor(OutputDescription outputDescriptor) {
            this.outputDescriptors.add(outputDescriptor);
            return self();
        }

        public B addOutputDescriptors(List<? extends OutputDescription> outputDescriptors) {
            this.outputDescriptors.addAll(outputDescriptors);
            return self();
        }

        public ProcessDescription build() {
            return new ProcessDescription(this);
        }

    }

}
