/*
 * Copyright 2016-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.impl.ProcessDescriptionImpl;

import java.util.Collection;

public class TypedProcessDescriptionImpl extends ProcessDescriptionImpl implements TypedProcessDescription {

    public TypedProcessDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        if (!builder.getInputs().stream().allMatch(TypedProcessInputDescription.class::isInstance)) {
            throw new IllegalArgumentException("not a typed input description");
        }
        if (!builder.getOutputs().stream().allMatch(TypedProcessOutputDescription.class::isInstance)) {
            throw new IllegalArgumentException("not a typed output description");
        }
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

    protected abstract static class AbstractBuilder<T extends TypedProcessDescription, B extends AbstractBuilder<T, B>>
            extends ProcessDescriptionImpl.AbstractBuilder<T, B>
            implements TypedProcessDescription.Builder<T, B> {
        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  ProcessDescription entity) {
            super(factory, entity);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withOutput(ProcessOutputDescription output) {
            if (!(output instanceof TypedProcessOutputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withOutput(output);
        }

        @Override
        public B withInput(ProcessInputDescription input) {
            if (!(input instanceof TypedProcessInputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withInput(input);
        }
    }

    public static class Builder extends AbstractBuilder<TypedProcessDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          ProcessDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public TypedProcessDescription build() {
            return new TypedProcessDescriptionImpl(this);
        }

    }
}
