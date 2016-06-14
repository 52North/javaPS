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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import javax.inject.Provider;

import org.n52.iceland.component.Component;
import org.n52.iceland.util.ProxySimilarityComparator;
import org.n52.iceland.util.Similar;

import com.google.common.collect.Maps;

/**
 *
 * @author Christian Autermann
 * @param <K> the key type
 * @param <C> the component type
 */
public abstract class AbstractSimilarityKeyRepository<K extends Similar<K>, C extends Component<K>> {

    private Set<Provider<C>> components = Collections.emptySet();
    private Map<K, Set<Provider<C>>> componentsByKey = new HashMap<>(0);

    protected void setProducers(Collection<Provider<C>> providers) {
        this.components = new HashSet<>(providers);
        this.componentsByKey = providers.stream()
                .flatMap(p -> p.get().getKeys().stream().map(k -> Maps.immutableEntry(k, p)))
                .collect(groupingBy(Entry::getKey, HashMap::new, mapping(Entry::getValue, toSet())));
    }

    protected C get(K k) {
        return this.componentsByKey.computeIfAbsent(k, this::findProviders)
                .stream().map(Provider::get).min(comparator(k)).orElse(null);
    }

    private Set<Provider<C>> findProviders(K k) {
        return this.components.stream().filter(isSimilarTo(k)).collect(toSet());
    }

    private static <K extends Similar<K>, C extends Component<K>> Predicate<Provider<C>> isSimilarTo(K key) {
        Predicate<K> keyIsSimilarTo = k -> key.getSimilarity(k) >= 0;
        return p -> p.get().getKeys().stream().anyMatch(keyIsSimilarTo);
    }

    private static <K extends Similar<K>, C extends Component<K>> Comparator<C> comparator(K key) {
        return new ProxySimilarityComparator<C, K>(key) {
            @Override protected Set<K> getSimilars(C c) { return c.getKeys(); }
        };
    }
}
