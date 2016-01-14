/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.impl;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.CompositeOwsException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.iceland.ogc.ows.Constraint;
import org.n52.iceland.ogc.ows.DCP;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsOperationsMetadata;
import org.n52.iceland.ogc.ows.OwsParameterValuePossibleValues;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.iceland.service.ServiceSettings;
import org.n52.iceland.util.collections.MultiMaps;
import org.n52.iceland.util.collections.SetMultiMap;
import org.n52.iceland.util.http.HTTPHeaders;
import org.n52.iceland.util.http.HTTPMethods;
import org.n52.javaps.WPSCapabilities;
import org.n52.javaps.WPSConstants;
import org.n52.javaps.handler.GetCapabilitiesHandler;

import com.google.common.collect.Sets;

public class GetCapabilitiesHandlerImpl implements GetCapabilitiesHandler {

    private static final Set<OperationHandlerKey> OPERATION_HANDLER_KEY
            = Collections.singleton(new OperationHandlerKey(WPSConstants.SERVICE,
                            OWSConstants.Operations.GetCapabilities));

    private static final Logger log = LoggerFactory.getLogger(GetCapabilitiesHandlerImpl.class);

    private URI serviceURL;

    private String defaultLanguage;

    private RequestOperatorRepository requestOperatorRepository;

    private BindingRepository bindingRepository;

    @Inject
    public void setRequestOperatorRepository(RequestOperatorRepository requestOperatorRepository) {
        this.requestOperatorRepository = requestOperatorRepository;
    }

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = bindingRepository;
    }

    @Setting(ServiceSettings.SERVICE_URL)
    public void setServiceURL(URI serviceURL) {
        this.serviceURL = serviceURL;
    }

    @Setting(I18NSettings.I18N_DEFAULT_LANGUAGE)
    public void setDefaultLanguage(String language) {
        this.defaultLanguage = language;
    }

    @Override
    @SuppressWarnings("ThrowableResultIgnored")
    public GetCapabilitiesResponse getCapabilities(
            GetCapabilitiesRequest request)
            throws OwsExceptionReport {
        log.debug("Handling GetCapabilities request: {}", request);

        GetCapabilitiesResponse capabilitiesResponse = request.getResponse();
        String version = request.getVersion();
//        String service = request.getService();
        String language = request.getRequestedLanguage();

        if (!language.isEmpty() && !language.equals(this.defaultLanguage)) {
            log.debug("Requested language '{}' is different from default '{}'.", language, this.defaultLanguage);
            log.warn("Unsupported language was requested, parameter is ignored: {}", language);
        }

        WPSCapabilities capabilities = new WPSCapabilities(version);

        // TODO add section parameter handling
//        HashSet<ServiceMetadataSections> requestedSections = getRequestedSections(request);
//        log.debug("Returning {} sections: {}", requestedSections.size(), Arrays.toString(requestedSections.toArray()));
        capabilitiesResponse.setCapabilities(capabilities);
        return capabilitiesResponse;
    }

    @Override
    public String getOperationName() {
        return OWSConstants.Operations.GetCapabilities.name();
    }

    @SuppressWarnings("ThrowableResultIgnored")
    private OwsOperationsMetadata createOperationsMetadata(String service, String version) throws CompositeOwsException {
        OwsOperationsMetadata operationsMetadata = new OwsOperationsMetadata();
        CompositeOwsException exception = new CompositeOwsException();

        RequestOperatorRepository ror = this.requestOperatorRepository;
        ror.getKeys().stream()
                .map(ror::getRequestOperator)
                .map(op -> {
                    try {
                        return op.getOperationMetadata(service, version);
                    } catch (OwsExceptionReport ex) {
                        exception.add(ex);
                        return null;
                    }
                })
                .map(opMetadata -> {
                    try {
                        Map<String, Set<DCP>> dcp = getDCP(new OperationKey(service, version, opMetadata.getOperationName()));
                        opMetadata.setDcp(dcp);
                    } catch (OwsExceptionReport ex) {
                        exception.add(ex);
                    }

                    return opMetadata;
                })
                .filter(Objects::nonNull)
                .forEach(operationsMetadata::addOperation);

        // add common query parameters
        operationsMetadata.addCommonValue(WPSConstants.OperationParameter.service,
                new OwsParameterValuePossibleValues(WPSConstants.SERVICE));
        operationsMetadata.addCommonValue(WPSConstants.OperationParameter.version,
                new OwsParameterValuePossibleValues(WPSConstants.VERSION));
        operationsMetadata.addCommonValue(WPSConstants.OperationParameter.request,
                new OwsParameterValuePossibleValues(Sets.newHashSet(OWSConstants.Operations.GetCapabilities.name(),
                                WPSConstants.OPERATION_DEMO)));

        exception.throwIfNotEmpty();
        return operationsMetadata;
    }

    protected Map<String, Set<DCP>> getDCP(OperationKey operationKey)
            throws OwsExceptionReport {
        SetMultiMap<String, DCP> dcps = MultiMaps.newSetMultiMap();

        try {
            for (Entry<String, Binding> entry : this.bindingRepository.getBindingsByPath().entrySet()) {
                String url = this.serviceURL.toString(); // + entry.getKey();
                Binding binding = entry.getValue();

                // these are not the "common" parameters and constraints, but the ones that have values for every endpoint
                Set<Constraint> constraints = Sets.newTreeSet();

                // common constraints
                if (binding.getSupportedEncodings() != null
                        && !binding.getSupportedEncodings().isEmpty()) {
                    SortedSet<String> set = binding.getSupportedEncodings().stream()
                            .map(String::valueOf).collect(TreeSet::new, Set::add, Set::addAll);
                    Constraint constraint = new Constraint(HTTPHeaders.CONTENT_TYPE,
                            new OwsParameterValuePossibleValues(set));
                    constraints.add(constraint);
                }

                // common parameters (none yet)
                // create DCPs according to supported operations and specific parameters
                if (binding.checkOperationHttpGetSupported(operationKey)) {
                    DCP dcp = new DCP(url + "?", constraints);
                    dcps.add(HTTPMethods.GET, dcp);
                }
                if (binding.checkOperationHttpPostSupported(operationKey)) {
                    Set<Constraint> postConstraints = Sets.newHashSet(constraints);
                    DCP dcp = new DCP(url, postConstraints);
                    dcps.add(HTTPMethods.POST, dcp);
                }
                if (binding.checkOperationHttpPutSupported(operationKey)) {
                    DCP dcp = new DCP(url, constraints);
                    dcps.add(HTTPMethods.PUT, dcp);
                }
                if (binding.checkOperationHttpDeleteSupported(operationKey)) {
                    DCP dcp = new DCP(url, constraints);
                    dcps.add(HTTPMethods.DELETE, dcp);
                }
            }

            log.trace("Created DCPs for {}: {}", operationKey, dcps);
        } catch (HTTPException e) {
            throw new NoApplicableCodeException().withMessage("Encoder for {} does not support a method", operationKey).causedBy(e);
        }

        return dcps;
    }

    @Override
    public OwsOperation getOperationsMetadata(String service, String version)
            throws OwsExceptionReport {
        OwsOperation op = new OwsOperation();
        op.setOperationName(getOperationName());

        op.addPossibleValuesParameter(WPSConstants.GetCapabilitiesParameter.acceptversions, WPSConstants.VERSION);
        // TODO add sections

        return op;
    }

    @Override
    public Set<OperationHandlerKey> getKeys() {
        return OPERATION_HANDLER_KEY;
    }

}
