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
package org.n52.javaps.handler;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.ds.OperationHandler;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsDCP;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsHttp;
import org.n52.iceland.ogc.ows.OwsMetadata;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsRequestMethod;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.iceland.service.ServiceSettings;
import org.n52.iceland.util.Validation;
import org.n52.iceland.util.http.HTTPHeaders;
import org.n52.iceland.util.http.HTTPMethods;
import org.n52.iceland.util.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Configurable
public abstract class AbstractHandler implements OperationHandler {
    private URI serviceURL;
    private BindingRepository bindingRepository;

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = Objects.requireNonNull(bindingRepository);
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(final URI serviceURL)
            throws ConfigurationError {
        this.serviceURL = Validation.notNull("Service URL", serviceURL);
    }

    private Set<OwsDCP> getDCP (String service, String version)
            throws OwsExceptionReport {
        return Collections.singleton(getDCP(new OperationKey(service, version, getOperationName())));
    }

    private OwsDCP getDCP(OperationKey decoderKey)
            throws OwsExceptionReport {
        try {
            Set<OwsRequestMethod> methods
                    = bindingRepository.getBindings().values().stream()
                            .flatMap(binding -> getRequestMethods(binding, decoderKey))
                            .collect(Collectors.toSet());

            return new OwsHttp(methods);
        } catch (Exception e) {
            // FIXME valid exception
            throw new NoApplicableCodeException().causedBy(e);
        }
    }

    private Stream<OwsRequestMethod> getRequestMethods(Binding binding, OperationKey decoderKey) {
        URI uri = URI.create(this.serviceURL + binding.getUrlPattern());
        Set<OwsDomain> constraints = getConstraints(binding);

        return Stream.of(HTTPMethods.GET,
                         HTTPMethods.POST,
                         HTTPMethods.PUT,
                         HTTPMethods.DELETE,
                         HTTPMethods.TRACE,
                         HTTPMethods.HEAD,
                         HTTPMethods.OPTIONS)
                .filter(isMethodSupported(binding, decoderKey))
                .map(method -> new OwsRequestMethod(uri, method, constraints));
    }

    private Predicate<String> isMethodSupported(Binding binding, OperationKey decoderKey) {
        return method -> {
            try {
                switch(method) {
                    case HTTPMethods.GET:
                        return binding.checkOperationHttpGetSupported(decoderKey);
                    case HTTPMethods.POST:
                        return binding.checkOperationHttpPostSupported(decoderKey);
                    case HTTPMethods.PUT:
                        return binding.checkOperationHttpPutSupported(decoderKey);
                    case HTTPMethods.DELETE:
                        return binding.checkOperationHttpDeleteSupported(decoderKey);
                    default:
                        return false;
                }
            } catch (HTTPException ex) {
                return false;
            }
        };
    }

    private Set<OwsDomain> getConstraints(Binding binding) {
        Set<OwsDomain> constraints = Optional
                .ofNullable(binding.getSupportedEncodings())
                .filter(x -> !x.isEmpty())
                .map(Collection::stream)
                .map(x -> x.map(MediaType::toString).map(OwsValue::new))
                .map(OwsAllowedValues::new)
                .map(x -> new OwsDomain(HTTPHeaders.CONTENT_TYPE, x))
                .map(Collections::singleton)
                .orElse(null);
        return constraints;
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
        String name = getOperationName();
        Set<OwsDomain> parameters = getOperationParameters(service, version);
        Set<OwsDomain> constraints = getOperationConstraints(service, version);
        Set<OwsMetadata> metadata = getOperationMetadata(service, version);
        Set<OwsDCP> dcp = getDCP(service, version);
         return new OwsOperation(name, parameters, constraints, metadata, dcp);
    }

    protected  Set<OwsDomain> getOperationParameters(String service, String version) {
        return null;
    }

    protected Set<OwsDomain> getOperationConstraints(String service, String version) {
        return null;
    }

    protected Set<OwsMetadata> getOperationMetadata(String service, String version) {
        return null;
    }

}
