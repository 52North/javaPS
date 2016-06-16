package org.n52.javaps.description.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.GroupOutputDescription;
import org.n52.javaps.description.GroupOutputDescriptionBuilder;
import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.ReturningProcessOutputVisitor;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GroupOutputDescriptionImpl extends AbstractProcessOutputDescription
        implements GroupOutputDescription {

    private final Map<OwsCodeType, ProcessOutputDescription> inputs;

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
    public ProcessOutputDescription getOutput(OwsCodeType id) {
        return this.inputs.get(id);
    }

    @Override
    public Collection<? extends ProcessOutputDescription> getOutputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCodeType> getOutputs() {
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
