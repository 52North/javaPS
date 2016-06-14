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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.description.ProcessDescription;

/**
 * @author Bastian Schaeffer, University of Muenster
 *
 */
public class RepositoryManager implements Constructable {

    private static RepositoryManager instance;

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryManager.class);
    private final Set<OwsCodeType> globalProcessIDs = Collections.synchronizedSet(new HashSet<>());
    private Map<String, IAlgorithmRepository> repositories;
    private UpdateThread updateThread;

    @Override
    public void init() {

        // clear registry
        globalProcessIDs.clear();

        // initialize all Repositories
        loadAllRepositories();

    }

    private List<String> getRepositoryNames() {

        List<String> repositoryNames = new ArrayList<>();

        // TODO

        return repositoryNames;
    }

    private void loadAllRepositories() {
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
            IAlgorithmRepository algorithmRepository = null;

            Class<?> repositoryClass = RepositoryManager.class.getClassLoader().loadClass(repositoryClassName);

            algorithmRepository = (IAlgorithmRepository) repositoryClass.newInstance();

            algorithmRepository.init();

            LOG.info("Algorithm Repository {} initialized", repositoryClassName);
            repositories.put(repositoryClassName, algorithmRepository);
        } catch (Exception e) {
            LOG.warn("An error occured while registering AlgorithmRepository: {}", repositoryClassName, e.getMessage());
        }
    }

    public static RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }

    /**
     * Allows to reInitialize the RepositoryManager... This should not be called
     * to often.
     *
     */
    public static void reInitialize() {
        instance = new RepositoryManager();
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
     * @param identifier
     *            The name of the algorithm class
     * @return IAlgorithm or null an instance of the algorithm class
     */
    public IAlgorithm getAlgorithm(OwsCodeType identifier) {

        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(identifier)) {
                return repository.getAlgorithm(identifier);
            }
        }
        return null;
    }

    /**
     *
     * @return allAlgorithms
     */
    public List<OwsCodeType> getAlgorithms() {
        List<OwsCodeType> allAlgorithmNamesCollection = new ArrayList<>();
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            allAlgorithmNamesCollection.addAll(repository.getAlgorithmNames());
        }
        return allAlgorithmNamesCollection;

    }

    public boolean containsAlgorithm(OwsCodeType id) {
        return getRepositoryNames().stream()
                .map((repoId) -> repositories.get(repoId))
                .anyMatch((repo) -> (repo.containsAlgorithm(id)));
    }

    public IAlgorithmRepository getRepositoryForAlgorithm(OwsCodeType algorithmName) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(algorithmName)) {
                return repository;
            }
        }
        return null;
    }

    public Class<?> getInputDataTypeForAlgorithm(OwsCodeType process, OwsCodeType input) {
        return getAlgorithm(process).getInputDataType(input);

    }

    public Class<?> getOutputDataTypeForAlgorithm(OwsCodeType process, OwsCodeType output) {
        return getAlgorithm(process).getOutputDataType(output);

    }

    public boolean registerAlgorithm(OwsCodeType id, IAlgorithmRepository repository) {
        return globalProcessIDs.add(id);
    }

    public boolean unregisterAlgorithm(OwsCodeType id) {
        return globalProcessIDs.remove(id);
    }

    public IAlgorithmRepository getAlgorithmRepository(String name) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.getClass().getName().equals(name)) {
                return repository;
            }
        }
        return null;
    }

    public IAlgorithmRepository getRepositoryForClassName(String className) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.getClass().getName().equals(className)) {
                return repository;
            }
        }
        return null;
    }

    public ProcessDescription getProcessDescription(String processClassName) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(processClassName)) {
                return repository.getProcessDescription(processClassName);
            }
        }
        return null;
    }

    private static class UpdateThread extends Thread {

        private final long interval;

        private boolean firstrun = true;

        UpdateThread(long interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            LOG.debug("UpdateThread started");

            try {
                // never terminate the run method
                while (true) {
                    // do not update on first run!
                    if (!firstrun) {
                        LOG.info("Reloading repositories - this might take a while ...");
                        long timestamp = System.currentTimeMillis();
                        RepositoryManager.getInstance().reloadRepositories();
                        LOG.info("Repositories reloaded - going to sleep. Took {} seconds.", (System.currentTimeMillis() - timestamp) / 1000);
                    } else {
                        firstrun = false;
                    }

                    // sleep for a given INTERVAL
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                LOG.debug("Interrupt received - Terminating the UpdateThread.");
            }
        }

    }

    // shut down the update thread
    @Override
    protected void finalize() throws Throwable {
        try {
            if (updateThread != null) {
                updateThread.interrupt();
            }
        } finally {
            super.finalize();
        }
    }

    public void destroy() {
        LOG.debug("Shutting down all repositories..");
        getRepositoryNames().stream()
                .map(name -> repositories.get(name))
                .forEach(repo -> repo.destroy());
    }

}
