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
import java.util.Set;

import org.n52.javaps.description.GroupInputDescription;
import org.n52.javaps.description.GroupInputDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ReturningProcessInputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessInputVisitor;
import org.n52.javaps.ogc.ows.OwsCode;

public class GroupInputDescriptionImpl extends AbstractProcessInputDescription
        implements GroupInputDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;

    protected GroupInputDescriptionImpl(
            AbstractGroupInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.inputs = builder.getInputs();
    }

    @Override
    public <T> T visit(ReturningProcessInputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningProcessInputVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    @Override
    public ProcessInputDescription getInput(OwsCode id) {
        return this.inputs.get(id);
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCode> getInputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    public static GroupInputDescriptionBuilder<?, ?> builder() {
        return new Builder();
    }

    private static class Builder extends AbstractGroupInputDescriptionBuilder<GroupInputDescriptionImpl, Builder> {
        @Override
        public GroupInputDescriptionImpl build() {
            return new GroupInputDescriptionImpl(this);
        }
    }
}
