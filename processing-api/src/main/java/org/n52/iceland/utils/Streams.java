package org.n52.iceland.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class Streams {
    private Streams() {
    }

    public static <T> BinaryOperator<T> throwingMerger(
            Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return throwingMerger((a, b) -> exceptionSupplier.get());
    }

    public static <T> BinaryOperator<T> throwingMerger(
            BiFunction<T, T, ? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        return (a, b) -> {
            throw exceptionSupplier.apply(a, b);
        };
    }

    public static <T> Collector<T, List<T>, T> toSingleResult() {
        return toSingleResult(IllegalStateException::new);
    }

    public static <T> Collector<T, List<T>, T> toSingleResult(
            Supplier<? extends RuntimeException> exceptionSupplier) {
        Objects.requireNonNull(exceptionSupplier);
        BinaryOperator<List<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        Function<List<T>, T> finisher = list -> {
            if (list.size() != 1) {
                throw exceptionSupplier.get();
            }
            return list.get(0);
        };
        BiConsumer<List<T>, T> accumulator = List::add;
        return Collector.of(LinkedList::new, accumulator, combiner, finisher);
    }
}
