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

import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.GroupOutputDescriptionBuilder;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.ReturningProcessOutputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessOutputVisitor;
import org.n52.javaps.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GroupOutputDescriptionImpl extends AbstractProcessOutputDescription
        implements GroupOutputDescription {

    private final Map<OwsCode, ProcessOutputDescription> inputs;

    protected GroupOutputDescriptionImpl(
            AbstractGroupOutputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.inputs = builder.getOutputs();
    }

    @Override
    public <T> T visit(ReturningProcessOutputVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningProcessOutputVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    @Override
    public ProcessOutputDescription getOutput(OwsCode id) {
        return this.inputs.get(id);
    }

    @Override
    public Collection<? extends ProcessOutputDescription> getOutputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCode> getOutputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    public static GroupOutputDescriptionBuilder<?, ?> builder() {
        return new Builder();
    }

    private static class Builder extends AbstractGroupOutputDescriptionBuilder<GroupOutputDescriptionImpl, Builder> {
        @Override
        public GroupOutputDescriptionImpl build() {
            return new GroupOutputDescriptionImpl(this);
        }
    }

}
