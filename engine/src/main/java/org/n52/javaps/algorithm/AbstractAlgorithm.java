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
package org.n52.javaps.algorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.javaps.description.TypedProcessDescription;

public abstract class AbstractAlgorithm extends AbstractObservable implements IAlgorithm {

    private final List<String> errorList = Collections.synchronizedList(new LinkedList<>());

    private TypedProcessDescription description;

    @Override
    @Deprecated
    public List<String> getErrors() {
        return Collections.unmodifiableList(this.errorList);
    }

    @Deprecated
    protected void addError(String error) {
        this.errorList.add(error);
    }

    @Override
    public synchronized TypedProcessDescription getDescription() {
        if (this.description == null) {
            this.description = createDescription();
        }
        return this.description;
    }

    protected abstract TypedProcessDescription createDescription();
}
