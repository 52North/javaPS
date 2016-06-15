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

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessDescriptionBuilder<T extends ProcessDescription, B extends ProcessDescriptionBuilder<T, B>>
        extends DescriptionBuilder<T, B> {

    B statusSupported(boolean statusSupported);

    B storeSupported(boolean storeSupported);

    B withInput(ProcessInputDescription input);

    default B withInput(ProcessInputDescriptionBuilder<?, ?> input) {
        return withInput(input.build());
    }

    @SuppressWarnings("unchecked")
    default B withInput(Stream<? extends ProcessInputDescription> input) {
        input.forEach(this::withInput);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    default B withInput(Iterable<ProcessInputDescription> inputs) {
        for (ProcessInputDescription input : inputs) {
            withInput(input);
        }
        return (B) this;
    }

    default B withInput(ProcessInputDescription... inputs) {
        return withInput(Arrays.asList(inputs));
    }

    B withOutput(ProcessOutputDescription output);

    default B withOutput(ProcessOutputDescriptionBuilder<?, ?> output) {
        return withOutput(output.build());
    }

    @SuppressWarnings("unchecked")
     default B withOutput(Stream<ProcessOutputDescription> outputs) {
        outputs.forEach(this::withOutput);
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    default B withOutput(Iterable<ProcessOutputDescription> outputs) {
        for (ProcessOutputDescription output : outputs) {
            withOutput(output);
        }
        return (B) this;
    }

    default B withOutput(ProcessOutputDescription... outputs) {
        return withOutput(Arrays.asList(outputs));
    }

    B withVersion(String version);

}
