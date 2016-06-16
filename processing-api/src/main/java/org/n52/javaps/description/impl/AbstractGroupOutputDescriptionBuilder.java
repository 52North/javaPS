package org.n52.javaps.description.impl;

import java.util.Map;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.GroupOutputDescriptionBuilder;
import org.n52.javaps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractGroupOutputDescriptionBuilder<T extends GroupOutputDescription, B extends AbstractGroupOutputDescriptionBuilder<T, B>>
        extends AbstractProcessOutputDescriptionBuilder<T, B>
        implements GroupOutputDescriptionBuilder<T, B> {

    private final ImmutableMap.Builder<OwsCodeType, ProcessOutputDescription> inputs
            = ImmutableMap.builder();

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withOutput(ProcessOutputDescription input) {
        if (input != null) {
            this.inputs.put(input.getId(), input);
        }
        return (B) this;
    }

    Map<OwsCodeType, ProcessOutputDescription> getOutputs() {
        return this.inputs.build();
    }

}
