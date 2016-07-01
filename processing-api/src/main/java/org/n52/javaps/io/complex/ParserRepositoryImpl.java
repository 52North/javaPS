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
package org.n52.javaps.io.complex;

import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.ogc.wps.Format;


/**
 * XMLParserFactory. Will be initialized within each Framework.
 *
 * @author foerster
 *
 */
public class ParserRepositoryImpl implements ParserRepository, Constructable {

    public static String PROPERTY_NAME_REGISTERED_PARSERS = "registeredParsers";
    private Set<IParser> registeredParsers;

    @Override
    public void init() {
        loadAllParsers();
    }

    private void loadAllParsers() {
        registeredParsers = new HashSet<IParser>();
        // for(String currentParserName : parserMap.keySet()) {
        //
        // ConfigurationModule currentParser = parserMap.get(currentParserName);
        //
        // String parserClass = "";
        //
        // if(currentParser instanceof ClassKnowingModule){
        // parserClass = ((ClassKnowingModule)currentParser).getClassName();
        // }
        //
        // IParser parser = null;
        // try {
        // parser = (IParser)
        // this.getClass().getClassLoader().loadClass(parserClass).newInstance();
        // parser.init(wpsConfig);
        // }
        // catch (ClassNotFoundException e) {
        // LOGGER.error("One of the parsers could not be loaded: " +
        // parserClass, e);
        // }
        // catch(IllegalAccessException e) {
        // LOGGER.error("One of the parsers could not be loaded: " +
        // parserClass, e);
        // }
        // catch(InstantiationException e) {
        // LOGGER.error("One of the parsers could not be loaded: " +
        // parserClass, e);
        // }
        //
        // if(parser != null) {
        //
        // LOGGER.info("Parser class registered: " + parserClass);
        // registeredParsers.add(parser);
        // }
        // }
    }

    @Override
    public Optional<IParser> getParser(Format format, Class<? extends ComplexData<?>> binding) {
        // TODO: try a chaining approach, by calculation all permutations and look for matches.
        return registeredParsers.stream()
                .filter(g -> g.isSupportedBinding(binding))
                .filter(g -> g.isSupportedFormat(format))
                .findFirst();
    }

    @Override
    public Set<IParser> getParsers() {
        return Collections.unmodifiableSet(registeredParsers);
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return registeredParsers.stream()
                .map(IParser::getSupportedFormats)
                .flatMap(Set::stream)
                .collect(toSet());
    }

    @Override
    public Set<Format> getSupportedFormats(
            Class<? extends ComplexData<?>> binding) {
        return registeredParsers.stream()
                .filter(g -> g.isSupportedBinding(binding))
                .map(IParser::getSupportedFormats)
                .flatMap(Set::stream)
                .collect(toSet());
    }
}
