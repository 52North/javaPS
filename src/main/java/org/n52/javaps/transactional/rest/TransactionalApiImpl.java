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
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.rest.ProcessesApi;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.NotUndeployableProcessException;
import org.n52.javaps.transactional.TransactionalAlgorithmRepository;
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
import java.util.Collection;

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
    private Collection<TransactionalAlgorithmRepository> repositories;
    private Engine engine;
    private String serviceURL;

    /**
     * Set the {@link DecoderRepository}.
     *
     * @param decoderRepository The {@link DecoderRepository}.
     */
    @Autowired
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    /**
     * Set the {@linkplain TransactionalAlgorithmRepository transactional algorithm repositories}.
     *
     * @param repositories The {@linkplain TransactionalAlgorithmRepository transactional algorithm repositories}.
     */
    @Autowired(required = false)
    public void setRepositories(Collection<TransactionalAlgorithmRepository> repositories) {
        this.repositories = repositories;
    }

    /**
     * Set the {@link Engine}.
     *
     * @param engine The {@link Engine}.
     */
    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
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
    public void undeployProcess(String id) throws ProcessNotFoundException, NotUndeployableProcessException {
        LOG.debug("undeploy {}", id);
        OwsCode processId = checkProcessExists(id);
        getRepository(processId).unregister(processId);
    }

    @Override
    public ResponseEntity<?> deployProcess(JsonNode request)
            throws DuplicateProcessException, UnsupportedProcessException {

        ApplicationPackage applicationPackage = decode(request);
        OwsCode id = applicationPackage.getProcessDescription().getProcessDescription().getId();
        LOG.info("deploy {}", id.getValue());
        if (engine.getProcessDescription(id).isPresent()) {
            throw new DuplicateProcessException(id);
        }
        getRepository(applicationPackage).register(applicationPackage);
        return ResponseEntity.created(getProcessURL(id)).build();

    }

    @Override
    public void updateProcess(String id, JsonNode request) throws ProcessNotFoundException,
                                                                  UnsupportedProcessException,
                                                                  NotUndeployableProcessException {
        LOG.info("redeploy {}", id);
        OwsCode processIdFromPath = checkProcessExists(id);
        ApplicationPackage applicationPackage = decode(request);
        OwsCode processId = applicationPackage.getProcessDescription().getProcessDescription().getId();

        if (!processId.equals(processIdFromPath)) {
            throw new UnsupportedProcessException(String.format("mismatching process identifiers %s vs. %s",
                                                                processIdFromPath.getValue(),
                                                                processId.getValue()));
        }
        getRepository(processId).unregister(processId);
        try {
            getRepository(applicationPackage).register(applicationPackage);
        } catch (DuplicateProcessException e) {
            throw new RuntimeException(String.format("could not undeploy process %s", processId), e);
        }
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

    /**
     * Checks that the process with the identifier {@code id} exists.
     *
     * @param id The identifier of the process.
     * @return The identifier as an {@link OwsCode}.
     * @throws ProcessNotFoundException If the process does not exists.
     */
    private OwsCode checkProcessExists(String id) throws ProcessNotFoundException {
        OwsCode processId = new OwsCode(id);
        return engine.getProcessDescription(processId)
                     .orElseThrow(() -> new ProcessNotFoundException(processId))
                     .getId();
    }

    /**
     * Get the {@link TransactionalAlgorithmRepository} that contains the supplied identifier.
     *
     * @param processId The {@link ApplicationPackage} identifier.
     * @return The {@link TransactionalAlgorithmRepository}.
     * @throws NotUndeployableProcessException If no {@link TransactionalAlgorithmRepository} can be found that contains
     *                                         the {@link ApplicationPackage}.
     */
    private TransactionalAlgorithmRepository getRepository(OwsCode processId) throws NotUndeployableProcessException {
        return repositories.stream().filter(x -> x.containsAlgorithm(processId)).findFirst()
                           .orElseThrow(() -> new NotUndeployableProcessException(processId));
    }

    /**
     * Get the {@link TransactionalAlgorithmRepository} that supports the supplied {@link ApplicationPackage}.
     *
     * @param applicationPackage The {@link ApplicationPackage}.
     * @return The {@link TransactionalAlgorithmRepository}.
     * @throws UnsupportedProcessException If no {@link TransactionalAlgorithmRepository} can be found that supports the
     *                                     {@link ApplicationPackage}.
     */
    private TransactionalAlgorithmRepository getRepository(ApplicationPackage applicationPackage)
            throws UnsupportedProcessException {
        return repositories.stream().filter(x -> x.isSupported(applicationPackage)).findFirst()
                           .orElseThrow(UnsupportedProcessException::new);
    }

    @Override
    public void init() {
        decoder = decoderRepository
                          .<ApplicationPackage, JsonNode>tryGetDecoder(new JsonDecoderKey(ApplicationPackage.class))
                          .orElseThrow(() -> new RuntimeException("No application package decoder found"));
    }
}
