/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
