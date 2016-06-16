package org.n52.javaps.description.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.GroupInputDescription;
import org.n52.javaps.description.GroupInputDescriptionBuilder;
import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.ReturningProcessInputVisitor;
import org.n52.javaps.description.ThrowingReturningProcessInputVisitor;

public class GroupInputDescriptionImpl extends AbstractProcessInputDescription
        implements GroupInputDescription {

    private final Map<OwsCodeType, ProcessInputDescription> inputs;

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
    public ProcessInputDescription getInput(OwsCodeType id) {
        return this.inputs.get(id);
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCodeType> getInputs() {
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
