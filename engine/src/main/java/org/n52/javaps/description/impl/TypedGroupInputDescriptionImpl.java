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

import org.n52.javaps.description.TypedGroupInputDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.impl.GroupInputDescriptionImpl;

import java.util.Collection;

public class TypedGroupInputDescriptionImpl extends GroupInputDescriptionImpl implements TypedGroupInputDescription {

    protected TypedGroupInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        if (!builder.getInputs().stream().allMatch(TypedProcessInputDescription.class::isInstance)) {
            throw new IllegalArgumentException("not a typed description");
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

    protected abstract static class AbstractBuilder<T extends TypedGroupInputDescription,
                                                           B extends AbstractBuilder<T, B>>
            extends GroupInputDescriptionImpl.AbstractBuilder<T, B>
            implements TypedGroupInputDescription.Builder<T, B> {

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  GroupInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public B withInput(ProcessInputDescription input) {
            if (!(input instanceof TypedProcessInputDescription)) {
                throw new IllegalArgumentException();
            }
            return super.withInput(input);
        }
    }

    public static class Builder extends AbstractBuilder<TypedGroupInputDescription, Builder> {

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          GroupInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public TypedGroupInputDescription build() {
            return new TypedGroupInputDescriptionImpl(this);
        }

    }
}
