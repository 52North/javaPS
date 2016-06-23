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
package org.n52.javaps.ogc.wps.description;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.stream.Collectors.groupingBy;
import static org.n52.javaps.utils.Streams.toSingleResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;
import org.n52.javaps.ogc.wps.description.Description;
import org.n52.javaps.ogc.wps.description.ProcessDescription;
import org.n52.javaps.ogc.wps.description.ProcessInputDescription;
import org.n52.javaps.ogc.wps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableSet;

import static java.util.stream.Collectors.groupingBy;
import static org.n52.javaps.utils.Streams.toSingleResult;
import static java.util.stream.Collectors.groupingBy;
import static org.n52.javaps.utils.Streams.toSingleResult;
import static java.util.stream.Collectors.groupingBy;
import static org.n52.javaps.utils.Streams.toSingleResult;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessDescriptionImpl
        extends AbstractDescription
        implements ProcessDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;
    private final Map<OwsCode, ProcessOutputDescription> outputs;
    private final boolean storeSupported;
    private final boolean statusSupported;
    private final String version;

    protected ProcessDescriptionImpl(
            AbstractBuilder<?, ?> builder) {

        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getInputs(),
             builder.getOutputs(),
             builder.getVersion(),
             builder.isStoreSupported(),
             builder.isStatusSupported());
    }

    public ProcessDescriptionImpl(OwsCode id, OwsLanguageString title,
                                  OwsLanguageString abstrakt,
                                  Set<OwsKeyword> keywords,
                                  Set<OwsMetadata> metadata,
                                  Set<ProcessInputDescription> inputs,
                                  Set<ProcessOutputDescription> outputs,
                                  String version,
                                  boolean storeSupported,
                                  boolean statusSupported) {
        super(id, title, abstrakt, keywords, metadata);
        Set<ProcessInputDescription> i = inputs == null ? Collections.emptySet()
                                         : inputs;
        this.inputs = i.stream()
                .collect(groupingBy(Description::getId, toSingleResult()));
        Set<ProcessOutputDescription> o = outputs == null ? Collections
                .emptySet() : outputs;
        this.outputs = o.stream()
                .collect(groupingBy(Description::getId, toSingleResult()));
        this.storeSupported = storeSupported;
        this.statusSupported = statusSupported;
        this.version = Objects.requireNonNull(version, "version");
    }

    @Override
    public ProcessInputDescription getInput(OwsCode id) {
        return this.inputs.get(id);
    }

    @Override
    public ProcessOutputDescription getOutput(OwsCode id) {
        return this.outputs.get(id);
    }

    @Override
    public Set<OwsCode> getInputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCode> getOutputs() {
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

    public static ProcessDescription.Builder<?, ?> builder() {
        return new BuilderImpl();
    }

    protected static abstract class AbstractBuilder<T extends ProcessDescription, B extends ProcessDescription.Builder<T, B>>
            extends AbstractDescription.AbstractBuilder<T, B>
            implements ProcessDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessInputDescription> inputs
                = ImmutableSet.builder();
        private final ImmutableSet.Builder<ProcessOutputDescription> outputs
                = ImmutableSet.builder();
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
                inputs.add(input);
            }
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withOutput(ProcessOutputDescription output) {
            if (output != null) {
                outputs.add(output);
            }
            return (B) this;
        }

        Set<ProcessInputDescription> getInputs() {
            return this.inputs.build();
        }

        Set<ProcessOutputDescription> getOutputs() {
            return this.outputs.build();
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

    private static class BuilderImpl extends AbstractBuilder<ProcessDescriptionImpl, BuilderImpl> {
        @Override
        public ProcessDescriptionImpl build() {
            return new ProcessDescriptionImpl(this);
        }
    }

}
