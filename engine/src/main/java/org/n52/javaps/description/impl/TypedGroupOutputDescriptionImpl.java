/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import org.n52.javaps.description.TypedGroupOutputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.impl.GroupOutputDescriptionImpl;

import java.util.Collection;

public class TypedGroupOutputDescriptionImpl extends GroupOutputDescriptionImpl implements TypedGroupOutputDescription {

    protected TypedGroupOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        if (!builder.getOutputs().stream().allMatch(TypedProcessOutputDescription.class::isInstance)) {
            throw new IllegalArgumentException("not a typed description");
        }
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

    protected abstract static class AbstractBuilder<T extends TypedGroupOutputDescription,
                                                           B extends AbstractBuilder<T, B>>
            extends GroupOutputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedGroupOutputDescription.Builder<T, B> {

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  GroupOutputDescription entity) {
            super(factory, entity);
        }

        @Override
        public B withOutput(ProcessOutputDescription Output) {
            if (!(Output instanceof TypedProcessOutputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withOutput(Output);
        }
    }

    public static class Builder extends AbstractBuilder<TypedGroupOutputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          GroupOutputDescription entity) {
            super(factory, entity);
        }

        @Override
        public TypedGroupOutputDescription build() {
            return new TypedGroupOutputDescriptionImpl(this);
        }

    }

}
