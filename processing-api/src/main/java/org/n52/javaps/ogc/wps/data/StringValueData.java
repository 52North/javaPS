package org.n52.javaps.ogc.wps.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.n52.iceland.ogc.ows.OwsCode;
import org.n52.javaps.ogc.wps.Format;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StringValueData extends ValueData {

    private final String string;

    public StringValueData(OwsCode id, String string) {
        this(id, null, string);
    }

    public StringValueData(OwsCode id, Format format, String string) {
        super(id, format);
        this.string = Objects.requireNonNull(string);
    }

    public StringValueData(String string) {
        this(null, null, string);
    }

    public StringValueData() {
        this(null, null, null);
    }

    @Override
    public InputStream getData() {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

      @Override
    public int hashCode() {
        return Objects.hash(getId(), getFormat(), this.string);
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
        final StringValueData other = (StringValueData) obj;
        return Objects.equals(getId(), other.getId()) &&
               Objects.equals(getFormat(), other.getFormat()) &&
               Objects.equals(this.string, other.string);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", getId())
                .add("format", getFormat())
                .add("value", this.string)
                .toString();
    }

}
