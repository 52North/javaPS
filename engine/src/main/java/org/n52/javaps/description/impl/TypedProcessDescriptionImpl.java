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
package org.n52.javaps.description.impl;

import java.util.Collection;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.impl.ProcessDescriptionImpl;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;

public class TypedProcessDescriptionImpl extends ProcessDescriptionImpl implements TypedProcessDescription {

    public TypedProcessDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    public TypedProcessDescriptionImpl(OwsCode id, OwsLanguageString title, OwsLanguageString abstrakt, Set<
            OwsKeyword> keywords, Set<OwsMetadata> metadata, Set<TypedProcessInputDescription<?>> inputs, Set<
                    TypedProcessOutputDescription<?>> outputs, String version, boolean storeSupported,
            boolean statusSupported) {
        super(id, title, abstrakt, keywords, metadata, inputs, outputs, version, storeSupported, statusSupported);
    }

    @Override
    public TypedProcessInputDescription<?> getInput(OwsCode id) {
        return (TypedProcessInputDescription<?>) super.getInput(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends TypedProcessInputDescription<?>> getInputDescriptions() {
        return (Collection<? extends TypedProcessInputDescription<?>>) super.getInputDescriptions();
    }

    @Override
    public TypedProcessOutputDescription<?> getOutput(OwsCode id) {
        return (TypedProcessOutputDescription<?>) super.getOutput(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends TypedProcessOutputDescription<?>> getOutputDescriptions() {
        return (Collection<? extends TypedProcessOutputDescription<?>>) super.getOutputDescriptions();
    }

    public static class Builder extends AbstractBuilder<TypedProcessDescription, Builder> {
        @Override
        public TypedProcessDescription build() {
            return new TypedProcessDescriptionImpl(this);
        }

        @Override
        public Builder withOutput(ProcessOutputDescription output) {
            if (!(output instanceof TypedProcessOutputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withOutput(output);
        }

        @Override
        public Builder withInput(ProcessInputDescription input) {
            if (!(input instanceof TypedProcessInputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withInput(input);
        }
    }
}
