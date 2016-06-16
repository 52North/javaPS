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
package org.n52.javaps.description.impl;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;
import org.n52.iceland.ogc.ows.OwsKeyword;
import org.n52.iceland.ogc.ows.OwsLanguageString;
import org.n52.javaps.description.Description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractDescription implements Description {

    private final OwsCodeType id;
    private final OwsLanguageString title;
    private final OwsLanguageString abstrakt;
    private final Set<OwsKeyword> keywords;

    public AbstractDescription(AbstractDescriptionBuilder<?, ?> builder) {
        this.id = Objects.requireNonNull(builder.getId(), "id");
        this.title = builder.getTitle() != null ? builder.getTitle()
                     : new OwsLanguageString(builder.getId().getValue());
        this.abstrakt = builder.getAbstract();
        this.keywords = builder.getKeywords();
    }

    @Override
    public OwsCodeType getId() {
        return this.id;
    }

    @Override
    public OwsLanguageString getTitle() {
        return title;
    }

    @Override
    public Optional<OwsLanguageString> getAbstract() {
        return Optional.ofNullable(this.abstrakt);
    }

    @Override
    public Set<OwsKeyword> getKeywords() {
        return Collections.unmodifiableSet(this.keywords);
    }
}
