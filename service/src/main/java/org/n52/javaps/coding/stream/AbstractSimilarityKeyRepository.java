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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.component.Component;
import org.n52.iceland.component.ComponentFactory;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;
import org.n52.iceland.util.ProxySimilarityComparator;
import org.n52.iceland.util.Similar;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

public class AbstractSimilarityKeyRepository<K extends Similar<K>, C extends Component<K>, F extends ComponentFactory<K, C>>
        extends AbstractComponentRepository<K, C, F> {

    private static final Logger LOG = LoggerFactory
            .getLogger(AbstractSimilarityKeyRepository.class);

    private final Set<Producer<C>> components = Sets.newHashSet();
    private final SetMultimap<K, Producer<C>> componentsByKey = HashMultimap
            .create();

    protected void setProducers(SetMultimap<K, Producer<C>> implementations) {
        this.componentsByKey.clear();
        this.componentsByKey.putAll(implementations);
        this.components.clear();
        this.components.addAll(implementations.values());
    }

    protected C get(K key) {
        return choose(findComponentForSingleKey(key), key);
    }

    protected C choose(Set<C> matches, K key) {
        if (matches == null || matches.isEmpty()) {
            LOG.debug("No implementation for {}", key);
            return null;
        } else if (matches.size() > 1) {
            return chooseFrom(matches, key);
        } else {
            return Iterables.getFirst(matches, null);
        }
    }

    private C chooseFrom(Set<C> matches, K key) {
        ComponentSimilarityComparator<K, C> comparator
                = new ComponentSimilarityComparator<>(key);
        C component = Collections.min(matches, comparator);
        LOG.debug("Requested ambiguous implementations for {}: Found {}; Choosing {}.",
                       key, Joiner.on(", ").join(matches), component);
        return component;
    }

    private static class ComponentSimilarityComparator<K extends Similar<K>, C extends Component<K>>
            extends ProxySimilarityComparator<C, K> {
        ComponentSimilarityComparator(K key) {
            super(key);
        }

        @Override
        protected Collection<K> getSimilars(C t) {
            return t.getKeys();
        }

    }

    protected Set<C> findComponentForSingleKey(K key) {
        if (!this.componentsByKey.containsKey(key)) {
            this.components.stream().forEach(producer -> {
                if (producer.get().getKeys().stream().anyMatch(k -> k
                        .getSimilarity(key) >= 0)) {
                    this.componentsByKey.put(key, producer);
                }
            });
        }
        return Producers.produce(componentsByKey.get(key));
    }

}
