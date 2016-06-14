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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.n52.iceland.lifecycle.Constructable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * XMLParserFactory. Will be initialized within each Framework.
 *
 * @author foerster
 *
 */

public class ParserFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(ParserFactory.class);

    @Autowired(required=false)
    private Collection<IParser> registeredParsers;

    public IParser getParser(String schema,
            String format,
            String encoding,
            Class<?> requiredInputClass) {

        // dealing with NULL encoding
        if (encoding == null) {
            encoding = IOHandler.DEFAULT_ENCODING;
        }

        // first, look if we can find a direct way
        for (IParser parser : registeredParsers) {
            Class<?>[] supportedClasses = parser.getSupportedDataBindings();
            for (Class<?> clazz : supportedClasses) {
                if (clazz.equals(requiredInputClass)) {
                    if (parser.isSupportedFormat(format)) {
                        LOGGER.info("Matching parser found: " + parser);
                        return parser;
                    }
                }
            }
        }

        // no parser could be found
        // try an indirect way by creating all permutations and look if one
        // matches
        // TODO
        return null;
    }

    public Collection<IParser> getAllParsers() {
        return registeredParsers;
    }
}
