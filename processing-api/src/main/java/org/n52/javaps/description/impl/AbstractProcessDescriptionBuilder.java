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

import static com.google.common.base.Strings.emptyToNull;

import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.description.ProcessDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableMap;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessDescriptionBuilder<T extends ProcessDescription, B extends ProcessDescriptionBuilder<T, B>>
        extends AbstractDescriptionBuilder<T, B> implements ProcessDescriptionBuilder<T, B> {

    private final ImmutableMap.Builder<OwsCodeType, ProcessInputDescription> inputs = ImmutableMap.builder();
    private final ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> outputs = ImmutableMap.builder();
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
            inputs.put(input.getId(), input);
        }
        return (B) this;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withOutput(ProcessOutputDescription output) {
        if (output != null) {
            outputs.put(output.getId(), output);
        }
        return (B) this;
    }

    ImmutableMap.Builder<OwsCodeType, ProcessInputDescription> getInputs() {
        return this.inputs;
    }

    ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> getOutputs() {
        return this.outputs;
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
