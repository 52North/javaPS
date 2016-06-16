package org.n52.javaps.description.impl;

import java.util.Map;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.GroupInputDescription;
import org.n52.javaps.description.GroupInputDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractGroupInputDescriptionBuilder<T extends GroupInputDescription, B extends AbstractGroupInputDescriptionBuilder<T, B>>
        extends AbstractProcessInputDescriptionBuilder<T, B>
        implements GroupInputDescriptionBuilder<T, B> {

    private final ImmutableMap.Builder<OwsCodeType, ProcessInputDescription> inputs
            = ImmutableMap.builder();

    @SuppressWarnings(value = "unchecked")
    @Override
    public B withInput(ProcessInputDescription input) {
        if (input != null) {
            this.inputs.put(input.getId(), input);
        }
        return (B) this;
    }

    Map<OwsCodeType, ProcessInputDescription> getInputs() {
        return this.inputs.build();
    }

}
