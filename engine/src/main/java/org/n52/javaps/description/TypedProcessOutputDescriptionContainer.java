/*
 * Copyright 2016-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.description;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescriptionContainer;

import java.util.Collection;

/**
 * @author Christian Autermann
 */
public interface TypedProcessOutputDescriptionContainer extends ProcessOutputDescriptionContainer {
    @Override
    TypedProcessOutputDescription<?> getOutput(OwsCode id);

    @Override
    default TypedProcessOutputDescription<?> getOutput(String id) {
        return getOutput(new OwsCode(id));
    }

    @Override
    Collection<? extends TypedProcessOutputDescription<?>> getOutputDescriptions();

    interface Builder<T extends TypedProcessOutputDescriptionContainer, B extends Builder<T, B>>
            extends ProcessOutputDescriptionContainer.Builder<T, B> {}
}
