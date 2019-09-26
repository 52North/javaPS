/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
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
package org.n52.javaps.transactional.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.rest.ProcessesApi;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.TransactionalAlgorithmRegistry;
import org.n52.javaps.transactional.UndeletableProcessException;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.Objects;

/**
 * Implementation of {@link TransactionalApi}.
 *
 * @author Christian Autermann
 */
@Controller
@Configurable
public class TransactionalApiImpl implements TransactionalApi, Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionalApiImpl.class);
    private DecoderRepository decoderRepository;
    private Decoder<ApplicationPackage, JsonNode> decoder;
    private String serviceURL;
    private TransactionalAlgorithmRegistry registration;

    /**
     * Set the {@link TransactionalAlgorithmRegistry}.
     *
     * @param registration The {@link TransactionalAlgorithmRegistry}.
     */
    @Autowired
    public void setRegistration(TransactionalAlgorithmRegistry registration) {
        this.registration = Objects.requireNonNull(registration);
    }

    /**
     * Set the {@link DecoderRepository}.
     *
     * @param decoderRepository The {@link DecoderRepository}.
     */
    @Autowired
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = Objects.requireNonNull(decoderRepository);
    }

    /**
     * Set the service URL.
     *
     * @param serviceURL The service URL.
     */
    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        Validation.notNull("serviceURL", serviceURL);
        String url = serviceURL.toString();
        if (url.contains("?")) {
            url = url.split("[?]")[0];
        }
        this.serviceURL = url.replace("/service", ProcessesApi.BASE_URL);
    }

    @Override
    public void undeployProcess(String id) throws ProcessNotFoundException, UndeletableProcessException {
        registration.unregister(id);
    }

    @Override
    public ResponseEntity<?> deployProcess(JsonNode request)
            throws DuplicateProcessException, UnsupportedProcessException {
        return ResponseEntity.created(getProcessURL(registration.register(decode(request)))).build();

    }

    @Override
    public void updateProcess(String id, JsonNode request) throws ProcessNotFoundException,
                                                                  UnsupportedProcessException,
                                                                  UndeletableProcessException {
        OwsCode processIdFromPath = new OwsCode(id);
        ApplicationPackage applicationPackage = decode(request);
        OwsCode processId = applicationPackage.getProcessDescription().getProcessDescription().getId();

        if (!processId.equals(processIdFromPath)) {
            throw new UnsupportedProcessException(String.format("mismatching process identifiers %s vs. %s",
                                                                processIdFromPath.getValue(),
                                                                processId.getValue()));
        }
        registration.update(applicationPackage);
    }

    /**
     * Decodes the JSON request as an {@link ApplicationPackage}.
     *
     * @param request The JSON payload to the POST or PUT methods.
     * @return The {@link ApplicationPackage}.
     * @throws UnsupportedProcessException If the request can not be decoded.
     */
    private ApplicationPackage decode(JsonNode request) throws UnsupportedProcessException {
        try {
            return decoder.decode(request);
        } catch (DecodingException e) {
            throw new UnsupportedProcessException(e);
        }
    }

    /**
     * Get the URL of the process with the identifier {@code id}.
     *
     * @param id The identifier.
     * @return The URL.
     */
    private URI getProcessURL(OwsCode id) {
        return URI.create(String.format("%s/%s", serviceURL, id.getValue()));
    }

    @Override
    public void init() {
        decoder = decoderRepository
                          .<ApplicationPackage, JsonNode>tryGetDecoder(new JsonDecoderKey(ApplicationPackage.class))
                          .orElseThrow(() -> new ConfigurationError("No application package decoder found"));
    }
}
