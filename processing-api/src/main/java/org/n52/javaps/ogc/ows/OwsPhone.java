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
package org.n52.javaps.ogc.ows;

import java.util.Collections;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsPhone {

    private final Set<String> voice;
    private final Set<String> facsimile;

    public OwsPhone(Set<String> voice, Set<String> facsimile) {
        this.voice = voice == null ? Collections.emptySet() : voice;
        this.facsimile = facsimile == null ? Collections.emptySet() : facsimile;
    }

    public Set<String> getVoice() {
        return Collections.unmodifiableSet(voice);
    }

    public Set<String> getFacsimile() {
        return Collections.unmodifiableSet(facsimile);
    }

}
