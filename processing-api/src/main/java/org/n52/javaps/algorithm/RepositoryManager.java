/**
 * ﻿Copyright (C) 2007 - 2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *       • Apache License, version 2.0
 *       • Apache Software License, version 1.0
 *       • GNU Lesser General Public License, version 3
 *       • Mozilla Public License, versions 1.0, 1.1 and 2.0
 *       • Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.javaps.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.javaps.algorithm.descriptor.ProcessDescription;

/**
 * @author Bastian Schaeffer, University of Muenster
 *
 */
public class RepositoryManager implements Constructable {

    private static RepositoryManager instance;

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryManager.class);

    private Map<String, IAlgorithmRepository> repositories;

    private ProcessIDRegistry globalProcessIDs = ProcessIDRegistry.getInstance();

    private UpdateThread updateThread;

    @Override
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
        repositories = new HashMap<>();
        LOG.debug("Loading all repositories: {} (doing a gc beforehand...)", repositories);

        // TODO

    }

    private void loadRepository(String repositoryName,
            String repositoryClassName) {
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

    public boolean containsAlgorithm(OwsCodeType algorithmName) {
        for (String repositoryClassName : getRepositoryNames()) {
            IAlgorithmRepository repository = repositories.get(repositoryClassName);
            if (repository.containsAlgorithm(algorithmName)) {
                return true;
            }
        }
        return false;
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
        if (globalProcessIDs.addID(id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean unregisterAlgorithm(OwsCodeType id) {
        if (globalProcessIDs.removeID(id)) {
            return true;
        } else {
            return false;
        }
    }

    public IAlgorithmRepository getAlgorithmRepository(OwsCodeType name) {
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

    static class UpdateThread extends Thread {

        private final long interval;

        private boolean firstrun = true;

        public UpdateThread(long interval) {
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
                    sleep(interval);
                }
            } catch (InterruptedException e) {
                LOG.debug("Interrupt received - Terminating the UpdateThread.");
            }
        }

    }

    // shut down the update thread
    public void finalize() {
        if (updateThread != null) {
            updateThread.interrupt();
        }
    }

    public void destroy() {
        LOG.debug("Shutting down all repositories..");
        getRepositoryNames().stream()
                .map(name -> repositories.get(name))
                .forEach(repo -> repo.destroy());
    }

}
