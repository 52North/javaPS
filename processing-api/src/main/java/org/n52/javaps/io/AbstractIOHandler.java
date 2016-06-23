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

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.n52.javaps.io.data.IComplexData;
import org.n52.javaps.ogc.wps.Format;

/**
 * Extending subclasses of AbstractGenerator shall provide functionality to
 * generate serviceable output data for the processes offered by the 52N WPS
 * framework.
 *
 * @author Matthias Mueller
 *
 */
public abstract class AbstractIOHandler implements IOHandler {
    private Set<Class<? extends IComplexData>> supportedBindings = new HashSet<>();
    private Set<Format> supportedFormats = new HashSet<>();

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(this.supportedFormats);
    }

    public void setSupportedFormats(Set<Format> supportedFormats) {
        this.supportedFormats = Objects.requireNonNull(supportedFormats);
    }

    @Override
    public boolean isSupportedFormat(Format format) {
        return getSupportedFormats().stream().anyMatch(f -> f.isCompatible(format));
    }

    @Override
    public Set<Class<? extends IComplexData>> getSupportedBindings() {
        return Collections.unmodifiableSet(this.supportedBindings);
    }

    public void setSupportedBindings(Set<Class<? extends IComplexData>> supportedBindings) {
        this.supportedBindings = Objects.requireNonNull(supportedBindings);
    }

    @Override
    public boolean isSupportedBinding(Class<? extends IComplexData> binding) {
        return getSupportedBindings().stream()
                .anyMatch(binding::isAssignableFrom);
    }
}
