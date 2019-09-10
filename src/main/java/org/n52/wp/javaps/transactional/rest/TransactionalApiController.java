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
package org.n52.wp.javaps.transactional.rest;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.service.ServiceSettings;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.javaps.engine.Engine;
import org.n52.javaps.engine.ProcessNotFoundException;
import org.n52.javaps.transactional.DuplicateProcessException;
import org.n52.javaps.transactional.TransactionalAlgorithmRepository;
import org.n52.javaps.transactional.UnsupportedProcessException;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.JsonDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.Collection;

@Controller
@Configurable
public class TransactionalApiController implements TransactionalApi, Constructable {

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
        this.serviceURL = url.replace("/service", baseURL + "/processes/");
    }

    @Override
    public void undeployProcess(String id) throws ProcessNotFoundException, NotUndeployableProcessException {
        OwsCode processId = new OwsCode(id);
        engine.getProcessDescription(processId).orElseThrow(() -> new ProcessNotFoundException(processId));
        TransactionalAlgorithmRepository repository = getRepository(processId);
        repository.unregister(processId);
    }

    @Override
    public ResponseEntity<?> deployProcess(JsonNode request)
            throws DuplicateProcessException, UnsupportedProcessException {
        try {
            ApplicationPackage applicationPackage = decoder.decode(request);
            OwsCode id = applicationPackage.getProcessDescription().getProcessDescription().getId();
            if (engine.getProcessDescription(id).isPresent()) {
                throw new DuplicateProcessException(id);
            }
            TransactionalAlgorithmRepository repository = getRepository(applicationPackage);
            repository.register(applicationPackage);
            return ResponseEntity.created(getProcessURL(id)).build();
        } catch (DecodingException e) {
            throw new UnsupportedProcessException(e);
        }
    }

    private URI getProcessURL(OwsCode id) {
        return URI.create(serviceURL + id);
    }

    private TransactionalAlgorithmRepository getRepository(ApplicationPackage applicationPackage)
            throws UnsupportedProcessException {
        return repositories.stream()
                           .filter(x -> x.isSupported(applicationPackage))
                           .findFirst()
                           .orElseThrow(UnsupportedProcessException::new);
    }

    private TransactionalAlgorithmRepository getRepository(OwsCode processId)
            throws NotUndeployableProcessException {
        return repositories.stream()
                           .filter(x -> x.containsAlgorithm(processId))
                           .findFirst()
                           .orElseThrow(() -> new NotUndeployableProcessException(processId));
    }

    @Override
    public void init() {
        decoder = decoderRepository
                          .<ApplicationPackage, JsonNode>tryGetDecoder(new JsonDecoderKey(ApplicationPackage.class))
                          .orElseThrow(() -> new RuntimeException("No application package decoder found"));
    }
}
