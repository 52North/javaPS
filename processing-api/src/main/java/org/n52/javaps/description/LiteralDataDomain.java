package org.n52.javaps.description;

import java.util.Optional;

import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDomainMetadata;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface LiteralDataDomain {
    OwsAllowedValues getAllowedValues();

    OwsDomainMetadata getDataType();

    OwsDomainMetadata getUOM();

    Optional<String> getDefaultValue();
}
