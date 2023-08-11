/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstract implementation of {@link ListenableTransactionalAlgorithmRepository}.
 *
 * @author Christian Autermann
 */
public abstract class AbstractTransactionalAlgorithmRepository implements ListenableTransactionalAlgorithmRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransactionalAlgorithmRepository.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<OwsCode, ApplicationPackage> applicationPackages = new HashMap<>();
    private Set<TransactionalAlgorithmRepositoryListener> listeners = new HashSet<>();

    @Override
    public void addListener(TransactionalAlgorithmRepositoryListener listener) {
        lock.writeLock().lock();
        try {
            LOG.debug("Adding listener {} to {}", listener, this);
            this.listeners.add(listener);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeListener(TransactionalAlgorithmRepositoryListener listener) {
        lock.writeLock().lock();
        try {
            LOG.debug("Removing listener {} from {}", listener, this);
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
        return getApplicationPackage(id).map(this::createAlgorithm);
    }

    @Override
    public Optional<ApplicationPackage> getApplicationPackage(OwsCode id) {
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
        LOG.debug("Registering application package {} to {}", applicationPackage, this);
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
            LOG.debug("Notifying listeners for new application package {}", applicationPackage);
            this.listeners.forEach(l -> l.onRegister(applicationPackage));
        } finally {
            lock.readLock().unlock();
        }
        return id;
    }

    @Override
    public void unregister(OwsCode id) throws ProcessNotFoundException, UndeletableProcessException {
        Objects.requireNonNull(id);
        LOG.debug("Unregistering application package {} from {}", id, this);
        ApplicationPackage applicationPackage;
        lock.writeLock().lock();
        try {
            applicationPackage = applicationPackages.get(id);
            if (applicationPackage == null) {
                throw new ProcessNotFoundException(id);
            }
            if (applicationPackage instanceof UndeletableApplicationPackage) {
                throw new UndeletableProcessException(id);
            }
            applicationPackages.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
        lock.readLock().lock();
        try {
            LOG.debug("Notifying listeners for removed application package {}", applicationPackage);
            this.listeners.forEach(l -> l.onUnregister(applicationPackage));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Create the {@linkplain IAlgorithm algorithm} from the {@link ApplicationPackage}.
     *
     * @param applicationPackage The {@link ApplicationPackage}
     * @return The {@linkplain IAlgorithm algorithm}
     */
    protected abstract IAlgorithm createAlgorithm(ApplicationPackage applicationPackage);

    /**
     * Create the {@linkplain TypedProcessDescription typed process description} from the {@link ApplicationPackage}.
     *
     * @param applicationPackage The {@link ApplicationPackage}
     * @return The {@linkplain TypedProcessDescription typed process description}
     */
    protected abstract TypedProcessDescription createProcessDescription(ApplicationPackage applicationPackage);
}
