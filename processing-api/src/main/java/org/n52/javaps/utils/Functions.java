package org.n52.javaps.utils;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class Functions {
    private Functions() {
    }

    public static <T, R> Supplier<R> bind(Function<? super T, ? extends R> function, T t) {
        return () -> function.apply(t);
    }

    public static <T1, T2, R> Function<T2, R> bind1(BiFunction<T1, T2, R> bifunction, T1 t1) {
        return (t2) -> bifunction.apply(t1, t2);
    }

    public static <T1, T2, R> Function<T1, R> bind2(BiFunction<T1, T2, R> bifunction, T2 t2) {
        return (t1) -> bifunction.apply(t1, t2);
    }

}
