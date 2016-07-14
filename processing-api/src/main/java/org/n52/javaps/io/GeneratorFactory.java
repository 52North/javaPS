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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneratorFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(GeneratorFactory.class);

    @Autowired(required = false)
    private Collection<IGenerator> registeredGenerators;

    public IGenerator getGenerator(String schema,
            String format,
            String encoding,
            Class<?> outputInternalClass) {

        // dealing with NULL encoding
        if (encoding == null) {
            encoding = IOHandler.DEFAULT_ENCODING;
        }

        for (IGenerator generator : registeredGenerators) {
            Class<?>[] supportedBindings = generator.getSupportedDataBindings();
            for (Class<?> clazz : supportedBindings) {
                if (clazz.equals(outputInternalClass)) {
                    if (generator.isSupportedFormat(format)) {
                        return generator;
                    }
                }
            }
        }
        // TODO: try a chaining approach, by calculation all permutations and
        // look for matches.
        return null;
    }

    public Collection<IGenerator> getAllGenerators() {
        return registeredGenerators;
    }

}
