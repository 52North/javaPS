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
package org.n52.javaps.description;

import java.math.BigInteger;
import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessInputDescriptionBuilder<T extends ProcessInputDescription, B extends ProcessInputDescriptionBuilder<T, B>>
        extends DataDescriptionBuilder<T, B> {

    B withMaximalOccurence(BigInteger max);

    default B withMaximalOccurence(long max) {
        return withMaximalOccurence(BigInteger.valueOf(max));
    }

    B withMinimalOccurence(BigInteger min);

    default B withMinimalOccurence(long min) {
        return withMinimalOccurence(BigInteger.valueOf(min));
    }

    default B withOccurence(InputOccurence occurence) {
        Objects.requireNonNull(occurence);
        return withMaximalOccurence(occurence.getMax().orElse(null))
                .withMinimalOccurence(occurence.getMin());
    }

}
