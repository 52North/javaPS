/*
 * Copyright 2016-2023 52°North Spatial Information Research GmbH
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import org.n52.faroe.ConfigurationError;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ResourceAlgorithmProvider implements InitialTransactionalAlgorithmProvider {
    private final ResourceLoader resourceLoader;
    private final DecoderRepository decoderRepository;
    private final String location;
    private final ObjectReader objectReader;
    private boolean deletable;

    public ResourceAlgorithmProvider(ResourceLoader resourceLoader,
                                     DecoderRepository decoderRepository,
                                     ObjectReader objectReader,
                                     String location,
                                     boolean deletable) {
        this.resourceLoader = Objects.requireNonNull(resourceLoader);
        this.decoderRepository = Objects.requireNonNull(decoderRepository);
        this.location = Objects.requireNonNull(location);
        this.objectReader = Objects.requireNonNull(objectReader);
        this.deletable = deletable;
    }

    @Override
    public ApplicationPackage get() {
        Decoder<ApplicationPackage, JsonNode> decoder;
        decoder = decoderRepository
                          .<ApplicationPackage, JsonNode>tryGetDecoder(new JsonDecoderKey(ApplicationPackage.class))
                          .orElseThrow(() -> new ConfigurationError("No application package decoder found"));
        Resource resource = resourceLoader.getResource(location);
        if (!resource.isReadable()) {
            throw new ConfigurationError("Resource location %s is not readable", location);
        }
        ApplicationPackage applicationPackage;
        try (InputStream inputStream = resource.getInputStream()) {
            applicationPackage = decoder.decode(objectReader.readTree(inputStream));
        } catch (IOException | DecodingException e) {
            throw new ConfigurationError(String.format("Cannot read resource %s", location), e);
        }
        if (!deletable) {
            applicationPackage = new UndeletableApplicationPackage(applicationPackage);
        }
        return applicationPackage;
    }
}
