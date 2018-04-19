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
package org.n52.javaps.io;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;



/**
 * Extending subclasses of AbstractGenerator shall provide functionality to
 * generate serviceable output data for the processes offered by the 52N WPS
 * framework.
 *
 * @author Matthias Mueller
 *
 */
public abstract class AbstractInputOutputHandler implements InputOutputHandler {
    private final Set<Class<? extends Data<?>>> supportedBindings;

    public AbstractInputOutputHandler() {
        this.supportedBindings = new LinkedHashSet<>();
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return Collections.unmodifiableSet(this.supportedBindings);
    }

    public void addSupportedBindings(Iterable<Class<? extends Data<?>>> supportedBindings) {
        supportedBindings.forEach(this::addSupportedBinding);
    }

    public void addSupportedBinding(Class<? extends Data<?>> supportedBinding) {
        this.supportedBindings.add(Objects.requireNonNull(supportedBinding));
    }

}
