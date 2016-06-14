package org.n52.iceland.ogc.ows;

import static com.google.common.base.Strings.emptyToNull;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsDomainMetadata {

    private final String reference;
    private final String value;

    public OwsDomainMetadata(String reference, String value) {
        this.reference = emptyToNull(reference);
        this.value = Objects.requireNonNull(emptyToNull(value));
    }

    public OwsDomainMetadata(String value) {
        this(null, value);
    }

    public Optional<String> getReference() {
        return Optional.ofNullable(this.reference);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reference, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsDomainMetadata that = (OwsDomainMetadata) obj;
        return Objects.equals(this.getValue(), that.getValue()) &&
               Objects.equals(this.getReference(), that.getReference());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("reference", getReference().orElse(null))
                .add("value", getValue())
                .toString();
    }
}
