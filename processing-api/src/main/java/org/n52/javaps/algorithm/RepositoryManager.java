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
package org.n52.javaps.algorithm;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.lifecycle.Destroyable;
import org.n52.javaps.description.ProcessDescription;
import org.n52.javaps.ogc.ows.OwsCode;

/**
 * @author Bastian Schaeffer, University of Muenster
 *
 */
public class RepositoryManager implements Constructable, Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryManager.class);
    private final Set<OwsCode> globalProcessIDs = Collections.synchronizedSet(new HashSet<>());
    private final Timer timer = new Timer("repository-manager", true);
    private final long updateInterval;
    private Map<String, AlgorithmRepository> repositories;

    public RepositoryManager(long updateInterval) {
        this.updateInterval = updateInterval;
    }


    @Override
    public void init() {
        // clear registry
        this.globalProcessIDs.clear();

        // initialize all Repositories
        loadAllRepositories();

        this.timer.scheduleAtFixedRate(new ReloadTask(), this.updateInterval, this.updateInterval);


    }

    @Override
    public void destroy() {
        this.timer.cancel();
        LOG.debug("Shutting down all repositories..");
        getRepositories().forEach(AlgorithmRepository::destroy);
    }

    private List<String> getRepositoryNames() {

        List<String> repositoryNames = new ArrayList<>();

        // TODO

        return repositoryNames;
    }

    private synchronized void loadAllRepositories() {
        repositories = new HashMap<>();
        LOG.debug("Loading all repositories: {} (doing a gc beforehand...)", repositories);

        // TODO

    }

    private void loadRepository(String repositoryName, String repositoryClassName) {
        LOG.debug("Loading repository: {}", repositoryName);

        // if (repository.isActive() == false) {
        // LOGGER.warn("Repository {} not active. Will not load it.",
        // repositoryName);
        // return;
        // }

        try {
            AlgorithmRepository algorithmRepository = null;

            Class<?> repositoryClass = RepositoryManager.class.getClassLoader().loadClass(repositoryClassName);

            algorithmRepository = (AlgorithmRepository) repositoryClass.newInstance();

            algorithmRepository.init();

            LOG.info("Algorithm Repository {} initialized", repositoryClassName);
            repositories.put(repositoryClassName, algorithmRepository);
        } catch (Exception e) {
            LOG.warn("An error occured while registering AlgorithmRepository: {}", repositoryClassName, e.getMessage());
        }
    }

    private Stream<AlgorithmRepository> getRepositories() {
        return getRepositoryNames().stream().map(repositories::get);
    }

    /**
     * Allows to reInitialize the Repositories
     *
     */
    protected void reloadRepositories() {
        loadAllRepositories();
    }

    /**
     * Methods looks for Algorithm in all Repositories. The first match is
     * returned. If no match could be found, null is returned
     *
     * @param id
     *            The name of the algorithm class
     * @return IAlgorithm or null an instance of the algorithm class
     */
    public IAlgorithm getAlgorithm(OwsCode id) {
        return getRepositoryForAlgorithm(id).flatMap(r -> r.getAlgorithm(id)).orElse(null);
    }

    /**
     *
     * @return allAlgorithms
     */
    public List<OwsCode> getAlgorithms() {
        return getRepositories().flatMap(r -> r.getAlgorithmNames().stream()).collect(toList());
    }

    public boolean containsAlgorithm(OwsCode id) {
        return getRepositoryForAlgorithm(id).isPresent();
    }

    public Optional<AlgorithmRepository> getRepositoryForAlgorithm(OwsCode id) {
        return getRepositories().filter(repo -> repo.containsAlgorithm(id)).findFirst();
    }

    public Class<?> getInputDataTypeForAlgorithm(OwsCode process, OwsCode input) {
        return getAlgorithm(process).getInputDataType(input);

    }

    public Class<?> getOutputDataTypeForAlgorithm(OwsCode process, OwsCode output) {
        return getAlgorithm(process).getOutputDataType(output);

    }

    public boolean registerAlgorithm(OwsCode id, AlgorithmRepository repository) {
        return globalProcessIDs.add(id);
    }

    public boolean unregisterAlgorithm(OwsCode id) {
        return globalProcessIDs.remove(id);
    }

    public AlgorithmRepository getAlgorithmRepository(String name) {
        for (String repositoryClassName : getRepositoryNames()) {
            AlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.getClass().getName().equals(name)) {
                return repository;
            }
        }
        return null;
    }

    public AlgorithmRepository getRepositoryForClassName(String className) {
        for (String repositoryClassName : getRepositoryNames()) {
            AlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.getClass().getName().equals(className)) {
                return repository;
            }
        }
        return null;
    }

    public Optional<ProcessDescription> getProcessDescription(String id) {
        return getProcessDescription(new OwsCode(id));
    }

    public Optional<ProcessDescription> getProcessDescription(OwsCode id) {
        return getRepositoryForAlgorithm(id).flatMap(r -> r.getProcessDescription(id));
    }

    private class ReloadTask extends TimerTask {
        @Override
        public void run() {
            LOG.info("Reloading repositories - this might take a while ...");
            reloadRepositories();
        }

    }
}
