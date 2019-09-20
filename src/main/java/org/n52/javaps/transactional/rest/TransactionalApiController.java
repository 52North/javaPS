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
import org.n52.javaps.transactional.TransactionalEngineException;
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

@Controller
@Configurable
public class TransactionalApiController implements TransactionalApi, Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionalApiController.class);
    private DecoderRepository decoderRepository;
    private Decoder<ApplicationPackage, JsonNode> decoder;
    private Collection<TransactionalAlgorithmRepository> repositories;
    private Engine engine;
    private String serviceURL;

    @Autowired
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Autowired(required = false)
    public void setRepositories(Collection<TransactionalAlgorithmRepository> repositories) {
        this.repositories = repositories;
    }

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

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
        OwsCode processId = new OwsCode(id);
        engine.getProcessDescription(processId).orElseThrow(() -> new ProcessNotFoundException(processId));
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

    private ApplicationPackage decode(JsonNode request) throws UnsupportedProcessException {
        ApplicationPackage applicationPackage;
        try {
            applicationPackage = decoder.decode(request);
        } catch (DecodingException e) {
            throw new UnsupportedProcessException(e);
        }
        return applicationPackage;
    }

    @Override
    public void updateProcess(String id, JsonNode request) throws ProcessNotFoundException,
                                                                  TransactionalEngineException {
        LOG.info("redeploy {}", id);
        OwsCode processIdFromPath = new OwsCode(id);
        engine.getProcessDescription(processIdFromPath)
              .orElseThrow(() -> new ProcessNotFoundException(processIdFromPath));

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
            throw new TransactionalEngineException(String.format("could not undeploy process %s", processId), e);
        }
    }

    private URI getProcessURL(OwsCode id) {
        return URI.create(String.format("%s/%s", serviceURL, id.getValue()));
    }

    private TransactionalAlgorithmRepository getRepository(OwsCode processId) throws NotUndeployableProcessException {
        return repositories.stream()
                           .filter(x -> x.containsAlgorithm(processId))
                           .findFirst()
                           .orElseThrow(() -> new NotUndeployableProcessException(processId));
    }

    private TransactionalAlgorithmRepository getRepository(ApplicationPackage applicationPackage)
            throws UnsupportedProcessException {
        return repositories.stream()
                           .filter(x -> x.isSupported(applicationPackage))
                           .findFirst()
                           .orElseThrow(UnsupportedProcessException::new);
    }

    @Override
    public void init() {
        decoder = decoderRepository
                          .<ApplicationPackage, JsonNode>tryGetDecoder(new JsonDecoderKey(ApplicationPackage.class))
                          .orElseThrow(() -> new RuntimeException("No application package decoder found"));
    }
}
