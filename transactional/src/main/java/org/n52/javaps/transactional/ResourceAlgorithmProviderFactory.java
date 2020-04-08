/*
 * Copyright 2019-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.transactional;

import com.fasterxml.jackson.databind.ObjectReader;
import org.n52.svalbard.decode.DecoderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ResourceAlgorithmProviderFactory {
    private final DecoderRepository decoderRepository;
    private final ObjectReader objectReader;
    private final ResourceLoader resourceLoader;

    @Autowired
    public ResourceAlgorithmProviderFactory(DecoderRepository decoderRepository,
                                            ObjectReader objectReader,
                                            ResourceLoader resourceLoader) {
        this.decoderRepository = Objects.requireNonNull(decoderRepository);
        this.objectReader = Objects.requireNonNull(objectReader);
        this.resourceLoader = Objects.requireNonNull(resourceLoader);
    }

    public ResourceAlgorithmProvider create(String resource, boolean deletable) {
        return new ResourceAlgorithmProvider(resourceLoader, decoderRepository, objectReader, resource, deletable);
    }
}
