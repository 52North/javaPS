package org.n52.javaps.ogc.ows;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.n52.javaps.ogc.w3c.xlink.XLink;


/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OwsMetadata extends XLink {
    private Optional<URI> about = Optional.empty();

    public Optional<URI> getAbout() {
        return about;
    }

    public void setAbout(Optional<URI> about) {
        this.about = about;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsMetadata other = (OwsMetadata) obj;

        return super.equals(obj) && Objects.equals(getAbout(), other.getAbout());
    }

    @Override
    public int hashCode() {
        return 53 * super.hashCode() + Objects.hashCode(getAbout());
    }

}
