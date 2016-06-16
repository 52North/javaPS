package org.n52.iceland.utils;

import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Optionals {

    private Optionals() {
    }

    public static <U> Optional<U> or(Optional<U> a, Optional<U> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        if (a.isPresent()) {
            return a;
        } else if (b.isPresent()) {
            return b;
        } else {
            return Optional.empty();
        }
    }
}
