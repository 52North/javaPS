/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.javaps.description.TypedProcessDescription;
import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.description.TypedProcessOutputDescription;


/**
 * @author Bastian Schaeffer, University of Muenster
 *
 */
public class RepositoryManager {
    private final Set<OwsCode> globalProcessIDs = Collections.synchronizedSet(new HashSet<>());
    private Set<AlgorithmRepository> repositories;

    private Stream<AlgorithmRepository> getRepositories() {
        return this.repositories.stream();
    }

    @Inject
    public void setRepositories(Set<AlgorithmRepository> repositories) {
        this.repositories = repositories;
    }

    /**
     * Methods looks for Algorithm in all Repositories. The first match is
     * returned. If no match could be found, null is returned
     *
     * @param id
     *            The name of the algorithm class
     * @return IAlgorithm or null an instance of the algorithm class
     */
    public Optional<IAlgorithm> getAlgorithm(OwsCode id) {
        return getRepositoryForAlgorithm(id).flatMap(r -> r.getAlgorithm(id));
    }

    public Set<OwsCode> getAlgorithms() {
        return getRepositories().flatMap(r -> r.getAlgorithmNames().stream()).collect(toSet());
    }

    public boolean containsAlgorithm(OwsCode id) {
        return getRepositoryForAlgorithm(id).isPresent();
    }

    public Optional<AlgorithmRepository> getRepositoryForAlgorithm(OwsCode id) {
        return getRepositories().filter(repo -> repo.containsAlgorithm(id)).findFirst();
    }

    public Optional<TypedProcessInputDescription<?>> getInputForAlgorithm(OwsCode process, OwsCode input) {
        return getProcessDescription(process).map(x -> x.getInput(input));

    }

    public Optional<TypedProcessOutputDescription<?>> getOutputForAlgorithm(OwsCode process, OwsCode output) {
        return getProcessDescription(process).map(x -> x.getOutput(output));
    }

    public boolean registerAlgorithm(OwsCode id, AlgorithmRepository repository) {
        return globalProcessIDs.add(id);
    }

    public boolean unregisterAlgorithm(OwsCode id) {
        return globalProcessIDs.remove(id);
    }

    public Optional<TypedProcessDescription> getProcessDescription(String id) {
        return getProcessDescription(new OwsCode(id));
    }

    public Optional<TypedProcessDescription> getProcessDescription(OwsCode id) {
        return getRepositoryForAlgorithm(id).flatMap(r -> r.getProcessDescription(id));
    }
}
