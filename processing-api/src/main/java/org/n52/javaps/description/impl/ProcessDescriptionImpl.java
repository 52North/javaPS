/*
 * Copyright (C) 2013-2015 Christian Autermann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
        this.inputs = builder.getInputs().build();
        this.outputs = builder.getOutputs().build();
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
        return new AbstractProcessDescriptionBuilderImpl();
    }

    private static class AbstractProcessDescriptionBuilderImpl
            extends AbstractProcessDescriptionBuilder<ProcessDescriptionImpl, AbstractProcessDescriptionBuilderImpl> {
        @Override
        public ProcessDescriptionImpl build() {
            return new ProcessDescriptionImpl(this);
        }
    }

}
