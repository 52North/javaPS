package org.n52.javaps.coding.stream;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;

import org.n52.iceland.util.Similar;
import org.n52.iceland.util.SimilarityComparator;

// TODO replace the iceland version
public class ProxySimilarityComparator<T, K extends Similar<K>>
        implements Comparator<T> {

    private final Comparator<T> comparator;

    public ProxySimilarityComparator(K ref,
                                     Function<T, Collection<K>> similars) {
        this.comparator = createComparator(ref, similars);
    }

    @Override
    public int compare(T o1, T o2) {
        return this.comparator.compare(o1, o2);
    }

    private Comparator<T> createComparator(K ref,
                                           Function<T, Collection<K>> similars) {
        Comparator<K> keyComparator = new SimilarityComparator<>(ref);
        Comparator<Class<?>> classComparator
                = (a, b) -> a.isAssignableFrom(b) ? 1 : b.isAssignableFrom(a) ? -1 : 0;
        Comparator<T> componentComparator
                = Comparator.comparing((T x) -> Collections.min(similars.apply(x), keyComparator), keyComparator);
        return componentComparator.thenComparing(Comparator.comparing(Object::getClass, classComparator));
    }

}
