package org.n52.javaps.ogc.wps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public enum DataTransmissionMode {
    VALUE,
    REFERENCE;

    @Override
    public String toString() {
        return getValue();
    }

    public String getValue() {
        return name().toLowerCase();
    }

}
