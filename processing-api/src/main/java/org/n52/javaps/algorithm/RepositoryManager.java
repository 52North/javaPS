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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.javaps.commons.WPSConfig;
import org.n52.javaps.io.GeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.ParserFactory;

/**
 * @author Bastian Schaeffer, University of Muenster
 *
 */
public class RepositoryManager implements Constructable {

    private static RepositoryManager instance;

    private static Logger LOGGER = LoggerFactory.getLogger(RepositoryManager.class);

    private Map<String, IAlgorithmRepository> repositories;

    private ProcessIDRegistry globalProcessIDs = ProcessIDRegistry.getInstance();

    private UpdateThread updateThread;

    public void init() {

        // clear registry
        globalProcessIDs.clearRegistry();

        // initialize all Repositories
        loadAllRepositories();

    }

    private List<String> getRepositoryNames() {

        List<String> repositoryNames = new ArrayList<>();

        // TODO

        return repositoryNames;
    }

    private void loadAllRepositories() {
        repositories = new HashMap<String, IAlgorithmRepository>();
        LOGGER.debug("Loading all repositories: {} (doing a gc beforehand...)", repositories);

        // TODO

    }

    private void loadRepository(String repositoryName,
            String repositoryClassName) {
        LOGGER.debug("Loading repository: {}", repositoryName);

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

            LOGGER.info("Algorithm Repository {} initialized", repositoryClassName);
            repositories.put(repositoryClassName, algorithmRepository);
        } catch (Exception e) {
            LOGGER.warn("An error occured while registering AlgorithmRepository: {}", repositoryClassName, e.getMessage());
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
     * @param className
     *            The name of the algorithm class
     * @return IAlgorithm or null an instance of the algorithm class
     */
    public IAlgorithm getAlgorithm(String className) {

        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(className)) {
                return repository.getAlgorithm(className);
            }
        }
        return null;
    }

    /**
     *
     * @return allAlgorithms
     */
    public List<String> getAlgorithms() {
        List<String> allAlgorithmNamesCollection = new ArrayList<String>();
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            allAlgorithmNamesCollection.addAll(repository.getAlgorithmNames());
        }
        return allAlgorithmNamesCollection;

    }

    public boolean containsAlgorithm(String algorithmName) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(algorithmName)) {
                return true;
            }
        }
        return false;
    }

    public IAlgorithmRepository getRepositoryForAlgorithm(String algorithmName) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(algorithmName)) {
                return repository;
            }
        }
        return null;
    }

    public Class<?> getInputDataTypeForAlgorithm(String algorithmIdentifier,
            String inputIdentifier) {
        IAlgorithm algorithm = getAlgorithm(algorithmIdentifier);
        return algorithm.getInputDataType(inputIdentifier);

    }

    public Class<?> getOutputDataTypeForAlgorithm(String algorithmIdentifier,
            String inputIdentifier) {
        IAlgorithm algorithm = getAlgorithm(algorithmIdentifier);
        return algorithm.getOutputDataType(inputIdentifier);

    }

    public boolean registerAlgorithm(String id,
            IAlgorithmRepository repository) {
        if (globalProcessIDs.addID(id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean unregisterAlgorithm(String id) {
        if (globalProcessIDs.removeID(id)) {
            return true;
        } else {
            return false;
        }
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
        return new ProcessDescription();
    }

    static class UpdateThread extends Thread {

        private final long interval;

        private boolean firstrun = true;

        public UpdateThread(long interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            LOGGER.debug("UpdateThread started");

            try {
                // never terminate the run method
                while (true) {
                    // do not update on first run!
                    if (!firstrun) {
                        LOGGER.info("Reloading repositories - this might take a while ...");
                        long timestamp = System.currentTimeMillis();
                        RepositoryManager.getInstance().reloadRepositories();
                        LOGGER.info("Repositories reloaded - going to sleep. Took {} seconds.", (System.currentTimeMillis() - timestamp) / 1000);
                    } else {
                        firstrun = false;
                    }

                    // sleep for a given INTERVAL
                    sleep(interval);
                }
            } catch (InterruptedException e) {
                LOGGER.debug("Interrupt received - Terminating the UpdateThread.");
            }
        }

    }

    // shut down the update thread
    public void finalize() {
        if (updateThread != null) {
            updateThread.interrupt();
        }
    }

    public void shutdown() {
        LOGGER.debug("Shutting down all repositories..");
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            repository.shutdown();
        }
    }

}
