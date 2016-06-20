package org.n52.javaps.ogc.w3c.xlink;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class XLink {
    private Optional<URI> href = Optional.empty();
    private Optional<URI> role = Optional.empty();
    private Optional<URI> arcrole = Optional.empty();
    private Optional<String> title = Optional.empty();
    private Optional<Show> show = Optional.empty();
    private Optional<Actuate> actuate = Optional.empty();

    public Optional<URI> getHref() {
        return this.href;
    }

    public void setHref(URI href) {
        this.href = Optional.ofNullable(href);
    }

    public Optional<URI> getRole() {
        return role;
    }

    public void setRole(URI role) {
        this.role = Optional.ofNullable(role);
    }

    public Optional<URI> getArcrole() {
        return arcrole;
    }

    public void setArcrole(URI arcrole) {
        this.arcrole = Optional.ofNullable(arcrole);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Optional.ofNullable(Strings.emptyToNull(title));
    }

    public Optional<Show> getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = Optional.ofNullable(show);
    }

    public Optional<Actuate> getActuate() {
        return actuate;
    }

    public void setActuate(Actuate actuate) {
        this.actuate = Optional.ofNullable(actuate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHref(), getRole(), getArcrole(),
                            getTitle(), getShow(), getActuate());
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
        final XLink other = (XLink) obj;
        return Objects.equals(this.getHref(), other.getHref()) &&
               Objects.equals(this.getRole(), other.getRole()) &&
               Objects.equals(this.getArcrole(), other.getArcrole()) &&
               Objects.equals(this.getTitle(), other.getTitle()) &&
               Objects.equals(this.getShow(), other.getShow()) &&
               Objects.equals(this.getActuate(), other.getActuate());
    }

    public enum Type {
        SIMPLE,
        EXTENDED,
        LOCATOR,
        ARC,
        RESOURCE,
        TITLE,
        NONE
    }

    public enum Show {
        NEW,
        REPLACE,
        EMBED,
        OTHER,
        NONE;
    }

    public enum Actuate {
        ON_LOAD,
        ON_REQUEST,
        OTHER,
        NONE;
    }
}
