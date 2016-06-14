package org.n52.javaps.description;

/**
 *
 * @author Christian Autermann
 * @param <T> the object to build
 * @param <B> the concrete builder type
 */
public interface Builder<T, B extends Builder<T, B>> {
    T build();
}
