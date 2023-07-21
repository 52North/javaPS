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

import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class TransactionalAlgorithmRegistryImpl implements TransactionalAlgorithmConfiguration,
                                                           TransactionalAlgorithmRegistry,
                                                           Constructable, Destroyable {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionalAlgorithmRegistryImpl.class);
    private final Engine engine;
    private final ResourceAlgorithmProviderFactory resourceAlgorithmProviderFactory;
    private final Set<InitialTransactionalAlgorithmProvider> providers = new HashSet<>();
    private final Set<TransactionalAlgorithmRepositoryListener> listeners = new HashSet<>();
    private Set<TransactionalAlgorithmRepository> repositories = Collections.emptySet();
    private Set<TransactionalAlgorithmConfigurer> configurers = Collections.emptySet();

    @Autowired
    public TransactionalAlgorithmRegistryImpl(Engine engine,
                                              ResourceAlgorithmProviderFactory resourceAlgorithmProviderFactory) {
        this.engine = Objects.requireNonNull(engine);
        this.resourceAlgorithmProviderFactory = Objects.requireNonNull(resourceAlgorithmProviderFactory);
    }

    /**
     * The {@linkplain TransactionalAlgorithmRepositoryListener listeners} to register.
     *
     * @param listeners The {@linkplain TransactionalAlgorithmRepositoryListener listeners}
     */
    @Autowired(required = false)
    public void setListeners(Set<TransactionalAlgorithmRepositoryListener> listeners) {
        Optional.ofNullable(listeners).ifPresent(this.listeners::addAll);
    }

    /**
     * Set the {@linkplain TransactionalAlgorithmRepository transactional algorithm repositories}.
     *
     * @param repositories The {@linkplain TransactionalAlgorithmRepository transactional algorithm repositories}.
     */
    @Autowired(required = false)
    public void setRepositories(Set<TransactionalAlgorithmRepository> repositories) {
        this.repositories = Optional.ofNullable(repositories).orElseGet(Collections::emptySet);
    }

    /**
     * Set the {@linkplain TransactionalAlgorithmConfigurer configurers}.
     *
     * @param configurers The {@linkplain TransactionalAlgorithmConfigurer configurers}.
     */
    @Autowired(required = false)
    public void setConfigurers(Set<TransactionalAlgorithmConfigurer> configurers) {
        this.configurers = Optional.ofNullable(configurers).orElseGet(Collections::emptySet);
    }

    @Override
    public TransactionalAlgorithmConfiguration addAlgorithmFromResource(String resource, boolean deletable) {
        providers.add(resourceAlgorithmProviderFactory.create(resource, deletable));
        return this;
    }

    @Override
    public TransactionalAlgorithmConfiguration addListener(TransactionalAlgorithmRepositoryListener listener) {
        this.listeners.add(Objects.requireNonNull(listener));
        return this;
    }

    @Override
    public void init() {
        configurers.forEach(configurer -> configurer.configure(this));
        repositories.stream()
                    .filter(ListenableTransactionalAlgorithmRepository.class::isInstance)
                    .map(ListenableTransactionalAlgorithmRepository.class::cast)
                    .forEach(repository -> listeners.forEach(repository::addListener));
        for (InitialTransactionalAlgorithmProvider provider : providers) {
            ApplicationPackage applicationPackage = provider.get();
            try {
                OwsCode id = register(applicationPackage);
                LOG.info("Registered application package {}", id);
            } catch (DuplicateProcessException e) {
                LOG.warn("Duplicate application package: " + applicationPackage, e);
            } catch (UnsupportedProcessException e) {
                LOG.error("Unsupported application package: " + applicationPackage, e);
            }
        }
    }

    @Override
    public void destroy() {
        repositories.stream()
                    .filter(ListenableTransactionalAlgorithmRepository.class::isInstance)
                    .map(ListenableTransactionalAlgorithmRepository.class::cast)
                    .forEach(repository -> listeners.forEach(repository::removeListener));
    }

    @Override
    public void update(ApplicationPackage applicationPackage)
            throws UndeletableProcessException, ProcessNotFoundException, UnsupportedProcessException {
        OwsCode id = applicationPackage.getProcessDescription().getProcessDescription().getId();
        LOG.debug("updating {}", id);
        checkProcessExists(id);
        getRepository(id).unregister(id);
        try {
            getRepository(applicationPackage).register(applicationPackage);
        } catch (DuplicateProcessException e) {
            throw new RuntimeException(String.format("could not undeploy process %s", id), e);
        }
    }

    @Override
    public void unregister(OwsCode id) throws ProcessNotFoundException, UndeletableProcessException {
        LOG.debug("unregistering {}", id);
        checkProcessExists(id);
        getRepository(id).unregister(id);
    }

    @Override
    public OwsCode register(ApplicationPackage applicationPackage)
            throws DuplicateProcessException, UnsupportedProcessException {
        OwsCode id = applicationPackage.getProcessDescription().getProcessDescription().getId();
        LOG.info("registering {}", id.getValue());
        if (engine.getProcessDescription(id).isPresent()) {
            throw new DuplicateProcessException(id);
        }
        getRepository(applicationPackage).register(applicationPackage);
        return id;
    }

    /**
     * Checks that the process with the identifier {@code id} exists.
     *
     * @param id The identifier of the process.
     * @throws ProcessNotFoundException If the process does not exists.
     */
    private void checkProcessExists(OwsCode id) throws ProcessNotFoundException {
        engine.getProcessDescription(id).orElseThrow(() -> new ProcessNotFoundException(id));
    }

    /**
     * Get the {@link TransactionalAlgorithmRepository} that contains the supplied identifier.
     *
     * @param processId The {@link ApplicationPackage} identifier.
     * @return The {@link TransactionalAlgorithmRepository}.
     * @throws UndeletableProcessException If no {@link TransactionalAlgorithmRepository} can be found that contains the
     *                                     {@link ApplicationPackage}.
     */
    private TransactionalAlgorithmRepository getRepository(OwsCode processId) throws UndeletableProcessException {
        return repositories.stream().filter(x -> x.containsAlgorithm(processId)).findFirst()
                           .orElseThrow(() -> new UndeletableProcessException(processId));
    }

    /**
     * Get the {@link TransactionalAlgorithmRepository} that supports the supplied {@link ApplicationPackage}.
     *
     * @param applicationPackage The {@link ApplicationPackage}.
     * @return The {@link TransactionalAlgorithmRepository}.
     * @throws UnsupportedProcessException If no {@link TransactionalAlgorithmRepository} can be found that supports the
     *                                     {@link ApplicationPackage}.
     */
    private TransactionalAlgorithmRepository getRepository(ApplicationPackage applicationPackage)
            throws UnsupportedProcessException {
        return repositories.stream().filter(x -> x.isSupported(applicationPackage)).findFirst()
                           .orElseThrow(UnsupportedProcessException::new);
    }

}
