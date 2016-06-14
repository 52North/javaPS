package org.n52.javaps.description;

/**
 *
 * @author Christian Autermann
 */
public interface DataDescription extends Description {
    default boolean isBoundingBox() {
        return false;
    }

    default boolean isComplex() {
        return false;
    }

    default boolean isLiteral() {
        return false;
    }
}
