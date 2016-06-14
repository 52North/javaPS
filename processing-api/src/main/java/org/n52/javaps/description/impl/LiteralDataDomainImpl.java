package org.n52.javaps.description.impl;

import java.util.Optional;

import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.description.LiteralDataDomain;
import org.n52.javaps.description.LiteralDataDomainBuilder;


public class LiteralDataDomainImpl implements LiteralDataDomain {

    private final OwsAllowedValues allowedValues;
    private final OwsDomainMetadata dataType;
    private final OwsDomainMetadata uom;
    private final Optional<String> defaultValue;

    public LiteralDataDomainImpl(AbstractLiteralDataDomainBuilder<?, ?> builder) {
        this.allowedValues = builder.getAllowedValues();
        this.dataType = builder.getDataType();
        this.uom = builder.getUom();
        this.defaultValue = Optional.ofNullable(builder.getDefaultValue());
    }

    @Override
    public OwsAllowedValues getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public OwsDomainMetadata getDataType() {
        return this.dataType;
    }

    @Override
    public OwsDomainMetadata getUOM() {
        return this.uom;
    }

    @Override
    public Optional<String> getDefaultValue() {
        return this.defaultValue;
    }

    public static LiteralDataDomainBuilder<?,?> builder() {
        return new BuilderImpl();
    }

    private static class BuilderImpl extends AbstractLiteralDataDomainBuilder<LiteralDataDomain, BuilderImpl> {
        @Override
        public LiteralDataDomainImpl build() {
            return new LiteralDataDomainImpl(this);
        }
    }
}
