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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.n52.iceland.ogc.wps.Format;
import org.n52.javaps.annotation.ConfigurableClass;
import org.n52.javaps.io.Data;


/**
 * Extending subclasses of AbstractGenerator shall provide functionality to
 * generate serviceable output data for the processes offered by the 52N WPS
 * framework.
 *
 * @author Matthias Mueller
 *
 */
public abstract class AbstractIOHandler extends ConfigurableClass implements IOHandler {
    private Set<Class<? extends Data<?>>> supportedBindings = new LinkedHashSet<>();
    private Set<Format> supportedFormats = new LinkedHashSet<>();

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(this.supportedFormats);
    }

    public void setSupportedFormats(Set<Format> supportedFormats) {
        this.supportedFormats = Objects.requireNonNull(supportedFormats);
    }

    @Override
    public Set<Class<? extends Data<?>>> getSupportedBindings() {
        return Collections.unmodifiableSet(this.supportedBindings);
    }

    public void setSupportedBindings(Set<Class<? extends Data<?>>> supportedBindings) {
        this.supportedBindings = Objects.requireNonNull(supportedBindings);
    }

    public AbstractIOHandler() {
        getProperties().path("formats").forEach(format -> {
            String mimeType = format.path("mimeType").textValue();
            String schema = format.path("schema").textValue();
            String encoding = format.path("encoding").textValue();
            supportedFormats.add(new Format(mimeType, encoding, schema));
        });
    }
}
