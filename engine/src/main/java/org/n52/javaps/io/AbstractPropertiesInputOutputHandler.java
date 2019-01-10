/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toCollection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.n52.shetland.ogc.wps.Format;
import org.n52.javaps.annotation.ConfigurableClass;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AbstractPropertiesInputOutputHandler extends AbstractInputOutputHandler implements ConfigurableClass {
    private final Set<Format> supportedFormats;

    public AbstractPropertiesInputOutputHandler() {
        this.supportedFormats = getFormatsFromProperties();
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(this.supportedFormats);
    }

    public void addSupportedFormat(Format supportedFormat) {
        this.supportedFormats.add(Objects.requireNonNull(supportedFormat));
    }

    public void addSupportedFormats(Iterable<Format> supportedFormats) {
        supportedFormats.forEach(this::addSupportedFormat);
    }

    private Set<Format> getFormatsFromProperties() {
        JsonNode node = getProperties().path("formats");
        return StreamSupport.stream(node.spliterator(), false).map(this::parseFormat).filter(x -> !x.isEmpty()).collect(
                toCollection(LinkedHashSet::new));
    }

    private Format parseFormat(JsonNode node) {
        String mimeType = node.path("mimeType").textValue();
        String schema = node.path("schema").textValue();
        String encoding = node.path("encoding").textValue();
        return new Format(mimeType, encoding, schema);
    }
}
