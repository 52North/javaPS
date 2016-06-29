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



import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.ds.GenericOperationHandler;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OWSConstants.CapabilitiesSection;
import org.n52.iceland.ogc.ows.OWSConstants.GetCapabilitiesParams;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.iceland.ogc.ows.OwsCapabilities;
import org.n52.iceland.ogc.ows.OwsDomain;
import org.n52.iceland.ogc.ows.OwsNoValues;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.ogc.ows.OwsOperationsMetadata;
import org.n52.iceland.ogc.ows.OwsPossibleValues;
import org.n52.iceland.ogc.ows.OwsServiceIdentification;
import org.n52.iceland.ogc.ows.OwsServiceProvider;
import org.n52.iceland.ogc.ows.OwsValue;
import org.n52.iceland.ogc.ows.ServiceMetadataRepository;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.iceland.response.GetCapabilitiesResponse;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.Comparables;
import org.n52.iceland.util.http.MediaTypes;
import org.n52.javaps.Engine;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.ogc.wps.ProcessOfferings;
import org.n52.javaps.ogc.wps.WPSCapabilities;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.ogc.wps.description.ProcessDescription;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetCapabilitiesHandler extends AbstractHandler
        implements GenericOperationHandler<GetCapabilitiesRequest, GetCapabilitiesResponse> {
    private static final OperationHandlerKey KEY
            = new OperationHandlerKey(WPSConstants.SERVICE, WPSConstants.Operations.GetCapabilities);
    private ServiceMetadataRepository serviceMetadataRepository;
    private RequestOperatorRepository operatorRepository;
    private ServiceOperatorRepository serviceOperatorRepository;
    private Engine engine;


    @Inject
    public void setServiceMetadataRepository(ServiceMetadataRepository serviceMetadataRepository) {
        this.serviceMetadataRepository = serviceMetadataRepository;
    }

    @Inject
    public void setOperatorRepository(RequestOperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository serviceOperatorRepository) {
        this.serviceOperatorRepository = serviceOperatorRepository;
    }

    @Inject
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetCapabilities.toString();
    }


    @Override
    public Set<OperationHandlerKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    public GetCapabilitiesResponse handle(GetCapabilitiesRequest request)
            throws OwsExceptionReport {
        GetCapabilitiesResponse response = request.getResponse();
        String service = request.getService();
        String version = response.getVersion();
        response.setCapabilities(createCapabilities(request, service, version));
        return response;
    }

    private OwsCapabilities createCapabilities(GetCapabilitiesRequest request,
                                               String service, String version)
            throws OwsExceptionReport {

        Set<CapabilitiesSection> sections = getRequestedSections(request);
        Locale requestedLocale = request.getRequestedLocale();

        String updateSequence = null;

        OwsServiceIdentification serviceIdentification = null;
        if (sections.contains(CapabilitiesSection.ServiceIdentification)) {
            serviceIdentification = getServiceIdentification(service, requestedLocale);
        }

        OwsServiceProvider serviceProvider = null;
        if (sections.contains(CapabilitiesSection.ServiceProvider)) {
            serviceProvider = getServiceProvider(service, requestedLocale);
        }

        OwsOperationsMetadata operationsMetadata = null;
        if (sections.contains(CapabilitiesSection.OperationsMetadata)) {
            operationsMetadata = getOperations(service, version);
        }

        Set<String> languages = null;
        if (sections.contains(CapabilitiesSection.Languages)) {
            languages = getLanguages();
        }

        ProcessOfferings processOfferings = null;
        if (sections.contains(CapabilitiesSection.Contents)) {
            processOfferings = getProcessOfferings();
        }

        return new WPSCapabilities(service, version, updateSequence,
                serviceIdentification, serviceProvider, operationsMetadata,
                languages, processOfferings);
    }

    @Override
    protected Set<OwsDomain> getOperationParameters(String service, String version) {
        OwsDomain acceptFormats = getAcceptFormatsDomain();
        OwsDomain acceptVersions = getAcceptVersionsDomain(service);
        OwsDomain sections = getSectionsDomain();
        OwsDomain updateSequence = getUpdateSequenceDomain();
        OwsDomain acceptLanguages = getAcceptLanguagesDomain();
        return ImmutableSet.of(acceptFormats, acceptVersions, acceptLanguages, sections, updateSequence);
    }

    private OwsOperationsMetadata getOperations(String service, String version) throws OwsExceptionReport {
        Collection<OwsDomain> parameters = getCommonParameters(service);
        Collection<OwsDomain> constraints = null;
        Collection<OwsOperation> operations = new LinkedList<>();

        for (RequestOperatorKey operatorKey : operatorRepository.getActiveRequestOperatorKeys(new ServiceOperatorKey(service, version))) {
            Optional.ofNullable(operatorRepository.getRequestOperator(operatorKey).getOperationMetadata(service, version)).ifPresent(operations::add);
        }

        return new OwsOperationsMetadata(operations, parameters, constraints);
    }

    private Collection<OwsDomain> getCommonParameters(String service) {
        OwsDomain serviceParameter = new OwsDomain(OWSConstants.RequestParams.service, new OwsAllowedValues(new OwsValue(service)));
        OwsDomain versionParameter = new OwsDomain(OWSConstants.RequestParams.version, getSupportedVersions(service));
        return Arrays.asList(serviceParameter, versionParameter);
    }

    private OwsAllowedValues getSupportedVersions(String service) {
        Set<String> supportedVersions = this.serviceOperatorRepository.getSupportedVersions(service);
        return new OwsAllowedValues(supportedVersions.stream().map(OwsValue::new));
    }

    private OwsServiceProvider getServiceProvider(String service, Locale locale) {
        return this.serviceMetadataRepository.getServiceProviderFactory(service).get(locale);
    }

    private OwsServiceIdentification getServiceIdentification(String service, Locale locale) {
        return this.serviceMetadataRepository.getServiceIdentificationFactory(service).get(locale);
    }

    private Set<CapabilitiesSection> getRequestedSections(GetCapabilitiesRequest request) {
        Set<CapabilitiesSection> sections = request.getSections().stream()
                .map(OWSConstants.CapabilitiesSection::fromString)
                .filter(Optional::isPresent).map(Optional::get)
                .collect(toSet());
        if (sections.contains(CapabilitiesSection.All) || sections.isEmpty()) {
            sections.addAll(Arrays.asList(CapabilitiesSection.values()));
        }
        return sections;
    }

    private ProcessOfferings getProcessOfferings() {
        Set<ProcessDescription> processDescriptions = engine.getProcessDescriptions();
        Set<ProcessOffering> offerings = processDescriptions.stream().map(ProcessOffering::new).collect(toSet());
        return new ProcessOfferings(offerings);
    }

    private OwsDomain getAcceptFormatsDomain() {
        OwsValue defaultValue = new OwsValue(MediaTypes.APPLICATION_XML.toString());
        OwsPossibleValues possibleValues = new OwsAllowedValues(defaultValue);
        return new OwsDomain(GetCapabilitiesParams.AcceptFormats, possibleValues, defaultValue);
    }

    private OwsDomain getAcceptVersionsDomain(String service) {
        Set<String> supportedVersions = this.serviceOperatorRepository.getSupportedVersions(service);
        OwsValue defaultValue = new OwsValue(Comparables.version().max(supportedVersions));
        OwsPossibleValues possibleValues = new OwsAllowedValues(supportedVersions.stream().map(OwsValue::new));
        return new OwsDomain(GetCapabilitiesParams.AcceptVersions, possibleValues, defaultValue);
    }

    private OwsDomain getSectionsDomain() {
        OwsPossibleValues possibleValues = new OwsAllowedValues(Arrays.stream(OWSConstants.CapabilitiesSection.values()).map(Object::toString).map(OwsValue::new));
        OwsValue defaultValue = new OwsValue(OWSConstants.CapabilitiesSection.All.toString());
        return new OwsDomain(GetCapabilitiesParams.Sections, possibleValues, defaultValue);
    }

    private OwsDomain getUpdateSequenceDomain() {
        OwsPossibleValues possibleValues = OwsNoValues.instance();
        return new OwsDomain(GetCapabilitiesParams.updateSequence, possibleValues);
    }

    private OwsDomain getAcceptLanguagesDomain() {
        Set<Locale> availableLocales = serviceMetadataRepository.getAvailableLocales();
        OwsPossibleValues possibleValues = new OwsAllowedValues(availableLocales.stream().map(LocaleHelper::toString).map(OwsValue::new));
        return new OwsDomain(GetCapabilitiesParams.AcceptLanguages, possibleValues);
    }

    private Set<String> getLanguages() {
        Set<Locale> availableLocales = serviceMetadataRepository.getAvailableLocales();
        return availableLocales.stream().map(LocaleHelper::toString).collect(toSet());
    }
}
