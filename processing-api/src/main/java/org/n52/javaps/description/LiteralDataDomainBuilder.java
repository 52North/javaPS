package org.n52.javaps.description;


import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDomainMetadata;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface LiteralDataDomainBuilder<T extends LiteralDataDomain, B extends LiteralDataDomainBuilder<T, B>> extends Builder<T, B> {
    B withDefaultValue(String value);

    B withDataType(OwsDomainMetadata dataType);

    default B withDataType(String reference, String value) {
        return withDataType(new OwsDomainMetadata(reference, value));
    }

    default B withDataType(String value) {
        return withDataType(new OwsDomainMetadata(value));
    }

    B withUOM(OwsDomainMetadata uom);

    default B withUOM(String reference, String value) {
        return withUOM(new OwsDomainMetadata(reference, value));
    }

    default B withUOM(String value) {
        return withUOM(new OwsDomainMetadata(value));
    }

    B withAllowedValues(OwsAllowedValues allowedValues);

}
