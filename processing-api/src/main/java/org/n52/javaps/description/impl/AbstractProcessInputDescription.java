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

import org.n52.javaps.description.InputOccurence;
import org.n52.javaps.description.ProcessInputDescription;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessInputDescription extends AbstractDataDescription
        implements ProcessInputDescription {

    private final InputOccurence occurence;

    protected AbstractProcessInputDescription(AbstractProcessInputDescriptionBuilder<?, ?> builder) {
        super(builder);
        this.occurence = new InputOccurence(builder.getMinimalOccurence(),
                                            builder.getMaximalOccurence());
    }

    @Override
    public InputOccurence getOccurence() {
        return this.occurence;
    }
}
