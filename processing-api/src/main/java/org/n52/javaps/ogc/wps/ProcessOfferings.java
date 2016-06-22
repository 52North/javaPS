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
package org.n52.javaps.ogc.wps;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ProcessOfferings implements Iterable<ProcessOffering> {

    private final Set<ProcessOffering> offerings;

    public ProcessOfferings(Set<ProcessOffering> offerings) {
        this.offerings = offerings == null ? Collections.emptySet() : offerings;
    }

    @Override
    public Iterator<ProcessOffering> iterator() {
        return this.offerings.iterator();
    }

    public Set<ProcessOffering> getOfferings() {
        return Collections.unmodifiableSet(offerings);
    }

}
