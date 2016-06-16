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
package org.n52.javaps.io;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.javaps.description.Format;
import org.n52.javaps.io.data.IComplexData;

public class GeneratorRepositoryImpl implements Constructable,
                                                GeneratorRepository {

    public static final String PROPERTY_NAME_REGISTERED_GENERATORS
            = "registeredGenerators";
    private Set<IGenerator> generators;

    @Override
    public void init() {
        loadAllGenerators();
    }

    private void loadAllGenerators() {
        generators = new HashSet<>();
        // TODO
        // for (String currentGeneratorName : generatorMap.keySet()) {
        //
        // ConfigurationModule currentGenerator =
        // generatorMap.get(currentGeneratorName);
        //
        // String generatorClass = "";
        //
        // if (currentGenerator instanceof ClassKnowingModule) {
        // generatorClass = ((ClassKnowingModule)
        // currentGenerator).getClassName();
        // }
        //
        // IGenerator generator = null;
        // try {
        // generator = (IGenerator)
        // this.getClass().getClassLoader().loadClass(generatorClass).newInstance();
        // generator.init(wpsConfig);
        // } catch (ClassNotFoundException e) {
        // LOGGER.error("One of the generators could not be loaded: " +
        // generatorClass, e);
        // } catch (IllegalAccessException e) {
        // LOGGER.error("One of the generators could not be loaded: " +
        // generatorClass, e);
        // } catch (InstantiationException e) {
        // LOGGER.error("One of the generators could not be loaded: " +
        // generatorClass, e);
        // }
        // if (generator != null) {
        // LOGGER.info("Generator class registered: " + generatorClass);
        // registeredGenerators.add(generator);
        // }
        // }
    }

    @Override
    public Optional<IGenerator> getGenerator(Format format,
                                             Class<? extends IComplexData> binding) {
        // TODO: try a chaining approach, by calculation all permutations and look for matches.
        return generators.stream()
                .filter(g -> g.isSupportedBinding(binding))
                .filter(g -> g.isSupportedFormat(format))
                .findFirst();
    }

    @Override
    public Set<IGenerator> getGenerators() {
        return Collections.unmodifiableSet(generators);
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return generators.stream()
                .map(IGenerator::getSupportedFormats)
                .flatMap(Set::stream)
                .collect(toSet());
    }

    @Override
    public Set<Format> getSupportedFormats(Class<? extends IComplexData> binding) {
        return generators.stream()
                .filter(g -> g.isSupportedBinding(binding))
                .map(IGenerator::getSupportedFormats)
                .flatMap(Set::stream)
                .collect(toSet());
    }

}
