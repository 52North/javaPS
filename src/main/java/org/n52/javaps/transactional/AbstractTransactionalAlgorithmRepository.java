/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.transactional;

import org.n52.javaps.algorithm.IAlgorithm;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractTransactionalAlgorithmRepository implements TransactionalAlgorithmRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransactionalAlgorithmRepository.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<OwsCode, ApplicationPackage> applicationPackages = new HashMap<>();
    private Set<TransactionalAlgorithmRepositoryListener> listeners = Collections.emptySet();

    @Autowired(required = false)
    public void setListeners(Collection<? extends TransactionalAlgorithmRepositoryListener> listeners) {
        lock.writeLock().lock();
        try {
            this.listeners = listeners == null ? Collections.emptySet() : new HashSet<>(listeners);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addListener(TransactionalAlgorithmRepositoryListener listener) {
        lock.writeLock().lock();
        try {
            this.listeners.add(listener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeListener(TransactionalAlgorithmRepositoryListener listener) {
        lock.writeLock().lock();
        try {
            this.listeners.remove(listener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<OwsCode> getAlgorithmNames() {
        lock.readLock().lock();
        try {
            return new HashSet<>(applicationPackages.keySet());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<TypedProcessDescription> getProcessDescription(OwsCode id) {
        Objects.requireNonNull(id);
        return getApplicationPackage(id).map(this::createProcessDescription);
    }

    @Override
    public boolean containsAlgorithm(OwsCode id) {
        Objects.requireNonNull(id);
        lock.readLock().lock();
        try {
            return applicationPackages.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<IAlgorithm> getAlgorithm(OwsCode id) {
        Objects.requireNonNull(id);
        LOG.trace("resolving algorithm for id {}", id);
        return getApplicationPackage(id).map(this::createAlgorithm);
    }

    private Optional<ApplicationPackage> getApplicationPackage(OwsCode id) {
        Objects.requireNonNull(id);
        lock.readLock().lock();
        try {
            return Optional.ofNullable(applicationPackages.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public OwsCode register(ApplicationPackage applicationPackage)
            throws DuplicateProcessException, UnsupportedProcessException {
        Objects.requireNonNull(applicationPackage);
        if (!isSupported(applicationPackage)) {
            throw new UnsupportedProcessException(String.format("unsupported application package %s",
                                                                applicationPackage));
        }
        OwsCode id = applicationPackage.getProcessDescription().getProcessDescription().getId();
        lock.writeLock().lock();
        try {
            if (applicationPackages.containsKey(id)) {
                throw new DuplicateProcessException(id);
            }
            applicationPackages.put(id, applicationPackage);

        } finally {
            lock.writeLock().unlock();
        }
        lock.readLock().lock();
        try {
            this.listeners.forEach(l -> l.onRegister(applicationPackage));
        } finally {
            lock.readLock().unlock();
        }
        return id;
    }

    @Override
    public void unregister(OwsCode id) throws ProcessNotFoundException {
        Objects.requireNonNull(id);
        ApplicationPackage applicationPackage;
        lock.writeLock().lock();
        try {
            applicationPackage = applicationPackages.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
        if (applicationPackage == null) {
            throw new ProcessNotFoundException(id);
        }
        lock.readLock().lock();
        try {
            this.listeners.forEach(l -> l.onUnregister(applicationPackage));
        } finally {
            lock.readLock().unlock();
        }
    }

    protected abstract IAlgorithm createAlgorithm(ApplicationPackage applicationPackage);

    protected abstract TypedProcessDescription createProcessDescription(ApplicationPackage applicationPackage);
}
