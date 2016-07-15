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
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.iceland.ogc.ows.OwsKeyword;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.iceland.ogc.ows.OwsMetadata;
import org.n52.iceland.ogc.wps.InputOccurence;
import org.n52.iceland.ogc.wps.description.ProcessInputDescription;
import org.n52.iceland.ogc.wps.description.impl.GroupInputDescriptionImpl;
import org.n52.javaps.description.TypedGroupInputDescription;
import org.n52.javaps.description.TypedProcessInputDescription;

public class TypedGroupInputDescriptionImpl extends GroupInputDescriptionImpl implements TypedGroupInputDescription {
    protected TypedGroupInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
    }

    public TypedGroupInputDescriptionImpl(OwsCode id, OwsLanguageString title, OwsLanguageString abstrakt,
                                          Set<OwsKeyword> keywords, Set<OwsMetadata> metadata, InputOccurence occurence,
                                          Set<TypedProcessInputDescription<?>> inputs) {
        super(id, title, abstrakt, keywords, metadata, occurence, inputs);
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

    public static class Builder extends AbstractBuilder<TypedGroupInputDescription, Builder> {
        @Override
        public TypedGroupInputDescription build() {
            return new TypedGroupInputDescriptionImpl(this);
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
