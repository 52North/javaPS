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
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import org.n52.javaps.description.Format;
import org.n52.javaps.io.data.IComplexData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface FormatRepository {

    Set<Format> getSupportedFormats();

    default Optional<Format> getDefaultFormat(Class<? extends IComplexData> binding) {
        return getSupportedFormats(binding).stream().min(Comparator.naturalOrder());
    }

    Set<Format> getSupportedFormats(Class<? extends IComplexData> binding);

    default Set<Format> getSupportedFormats(IComplexData binding) {
        if (binding == null) {
            return Collections.emptySet();
        } else {
            return getSupportedFormats(binding.getClass());
        }
    }
}
