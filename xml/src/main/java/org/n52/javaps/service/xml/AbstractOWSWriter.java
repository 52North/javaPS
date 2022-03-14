/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
package org.n52.javaps.service.xml;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.janmayen.Optionals;
import org.n52.janmayen.http.HTTPMethods;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.janmayen.i18n.MultilingualString;
import org.n52.shetland.ogc.ows.OwsAddress;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsContact;
import org.n52.shetland.ogc.ows.OwsDCP;
import org.n52.shetland.ogc.ows.OwsDomain;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsNoValues;
import org.n52.shetland.ogc.ows.OwsOperation;
import org.n52.shetland.ogc.ows.OwsOperationsMetadata;
import org.n52.shetland.ogc.ows.OwsPhone;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsRequestMethod;
import org.n52.shetland.ogc.ows.OwsResponsibleParty;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.ogc.ows.OwsServiceProvider;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.OwsValueRestriction;
import org.n52.shetland.ogc.ows.OwsValuesReference;
import org.n52.shetland.ogc.ows.OwsValuesUnit;
import org.n52.shetland.w3c.xlink.Link;
import org.n52.svalbard.encode.stream.xml.AbstractMultiElementXmlStreamWriter;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractOWSWriter extends AbstractMultiElementXmlStreamWriter {

    private static final String XML_LANG = "xml:lang";

    public AbstractOWSWriter(Class<?>... supportedClasses) {
        super(supportedClasses);
    }

    protected void writeOperationsMetadata(OwsCapabilities capabilities) throws XMLStreamException {
        element(OWSConstants.Elem.QN_OPERATIONS_METADATA, capabilities.getOperationsMetadata(), (
                OwsOperationsMetadata operationsMetadata) -> {
            writeOperations(operationsMetadata);
            writeParameters(operationsMetadata.getParameters());
            writeConstraints(operationsMetadata.getConstraints());
        });
    }

    protected void writeKeywords(Set<OwsKeyword> set) throws XMLStreamException {
        if (!set.isEmpty()) {
            element(OWSConstants.Elem.QN_KEYWORDS, set, x -> {
                Map<Optional<OwsCode>, Set<OwsLanguageString>> keywords = x.stream().collect(groupingBy(
                        OwsKeyword::getType, () -> new TreeMap<Optional<OwsCode>, Set<OwsLanguageString>>(Optionals
                                .nullsLast()), mapping(OwsKeyword::getKeyword, toCollection(TreeSet::new))));
                for (Entry<Optional<OwsCode>, Set<OwsLanguageString>> entry : keywords.entrySet()) {
                    Optional<OwsCode> type = entry.getKey();
                    for (OwsLanguageString keyword : entry.getValue()) {
                        writeLanguageString(OWSConstants.Elem.QN_KEYWORD, keyword);
                    }
                    if (type.isPresent()) {
                        writeCode(OWSConstants.Elem.QN_TYPE, type.get());
                    }
                }
            });
        }
    }

    protected void writeLanguageString(QName name,
            OwsLanguageString value) throws XMLStreamException {
        element(name, value, x -> {
            if (x.getLang().isPresent()) {
                attr(XML_LANG, x.getLang().get());
            }
            chars(x.getValue());
        });
    }

    protected void writeLanguageString(QName name,
            Optional<OwsLanguageString> value) throws XMLStreamException {
        if (value.isPresent()) {
            writeLanguageString(name, value.get());
        }
    }

    protected void writeDomainMetadata(QName name,
            OwsDomainMetadata dmd) throws XMLStreamException {
        writeDomainMetadata(name, Optional.ofNullable(dmd));
    }

    protected void writeDomainMetadata(QName name,
            Optional<OwsDomainMetadata> metadata) throws XMLStreamException {
        if (metadata.isPresent()) {
            OwsDomainMetadata m = metadata.get();
            if (Optionals.any(m.getValue(), metadata.get().getReference())) {
                element(name, m, (OwsDomainMetadata x) -> {
                    attr(OWSConstants.Attr.QN_REFERENCE, x.getReference().map(URI::toString));
                    if (x.getValue().isPresent()) {
                        chars(x.getValue().get());
                    }
                });
            } else {
                empty(name);
            }
        }
    }

    protected void writeMultilingualString(QName name,
            MultilingualString ms) throws XMLStreamException {
        forEach(name, ms, ls -> {
            langAttr(ls.getLang());
            chars(ls.getText());
        });
    }

    protected void langAttr(Locale lang) throws XMLStreamException {
        attr(XML_LANG, Optional.of(Strings.emptyToNull(LocaleHelper.encode(lang))));
    }

    protected void writeCode(QName name,
            Optional<OwsCode> id) throws XMLStreamException {
        element(name, id, (OwsCode x) -> {
            attr(OWSConstants.Attr.AN_CODE_SPACE, x.getCodeSpace().map(URI::toString));
            chars(x.getValue());
        });
    }

    protected void writeCode(QName name,
            OwsCode id) throws XMLStreamException {
        writeCode(name, Optional.ofNullable(id));
    }

    protected void writeHTTP(OwsDCP dcp) throws XMLStreamException {
        element(OWSConstants.Elem.QN_HTTP, dcp.asHTTP(), http -> {
            for (OwsRequestMethod method : http.getRequestMethods()) {
                writeRequestMethod(method);
            }
        });
    }

    protected void writeDCP(OwsOperation operation) throws XMLStreamException {
        forEach(OWSConstants.Elem.QN_DCP, operation.getDCP(), dcp -> {
            if (dcp.isHTTP()) {
                writeHTTP(dcp);
            }
        });
    }

    protected void writeParameters(Iterable<OwsDomain> parameters) throws XMLStreamException {
        writeDomains(OWSConstants.Elem.QN_PARAMETER, parameters);
    }

    protected void writeLanguages(OwsCapabilities capabilities) throws XMLStreamException {
        element(OWSConstants.Elem.QN_LANGUAGES, capabilities.getLanguages(), (Iterable<String> languages) -> {
            forEach(OWSConstants.Elem.QN_LANGUAGE, languages, this::chars);
        });
    }

    protected void writeAnyValue(OwsAnyValue anyValue) throws XMLStreamException {
        empty(OWSConstants.Elem.QN_ANY_VALUE);
    }

    protected void writeNoValues(OwsNoValues noValues) throws XMLStreamException {
        empty(OWSConstants.Elem.QN_NO_VALUES);
    }

    protected void writeValuesReference(OwsValuesReference valuesReference) throws XMLStreamException {
        element(OWSConstants.Elem.QN_VALUES_REFERENCE, () -> {
            attr(OWSConstants.Attr.AN_REFERENCE, valuesReference.getReference().toString());
            chars(valuesReference.getValue());
        });
    }

    protected void writeContactInfo(OwsResponsibleParty serviceContact) throws XMLStreamException {
        element(OWSConstants.Elem.QN_CONTACT_INFO, serviceContact.getContactInfo(), (OwsContact contactInfo) -> {
            writePhone(contactInfo);
            writeAddress(contactInfo);
            element(OWSConstants.Elem.QN_ONLINE_RESOURCE, contactInfo.getOnlineResource(),
                    (ElementWriter<Link>) this::writeXLinkAttrs);
            element(OWSConstants.Elem.QN_HOURS_OF_SERVICE, contactInfo.getHoursOfService());
            element(OWSConstants.Elem.QN_CONTACT_INSTRUCTIONS, contactInfo.getContactInstructions());
        });
    }

    protected void writeAddress(OwsContact contactInfo) throws XMLStreamException {
        element(OWSConstants.Elem.QN_ADDRESS, contactInfo.getAddress(), (OwsAddress address) -> {
            forEach(OWSConstants.Elem.QN_DELIVERY_POINT, address.getDeliveryPoint(), this::chars);
            element(OWSConstants.Elem.QN_CITY, address.getCity());
            element(OWSConstants.Elem.QN_ADMINISTRATIVE_AREA, address.getAdministrativeArea());
            element(OWSConstants.Elem.QN_POSTAL_CODE, address.getPostalCode());
            element(OWSConstants.Elem.QN_COUNTRY, address.getCountry());
            forEach(OWSConstants.Elem.QN_ELECTRONIC_MAIL_ADDRESS, address.getElectronicMailAddress(), this::chars);
        });
    }

    protected void writePhone(OwsContact contactInfo) throws XMLStreamException {
        element(OWSConstants.Elem.QN_PHONE, contactInfo.getPhone(), (OwsPhone phone) -> {
            forEach(OWSConstants.Elem.QN_VOICE, phone.getVoice(), this::chars);
            forEach(OWSConstants.Elem.QN_FACSIMILE, phone.getFacsimile(), this::chars);
        });
    }

    protected void writeServiceContact(OwsServiceProvider sp) throws XMLStreamException {
        element(OWSConstants.Elem.QN_SERVICE_CONTACT, sp.getServiceContact(), serviceContact -> {
            element(OWSConstants.Elem.QN_INDIVIDUAL_NAME, serviceContact.getIndividualName());
            element(OWSConstants.Elem.QN_POSITION_NAME, serviceContact.getPositionName());
            writeContactInfo(serviceContact);
            writeCode(OWSConstants.Elem.QN_ROLE, serviceContact.getRole());
        });
    }

    protected void writeServiceProvider(OwsCapabilities capabilities) throws XMLStreamException {
        element(OWSConstants.Elem.QN_SERVICE_PROVIDER, capabilities.getServiceProvider(), (OwsServiceProvider sp) -> {
            element(OWSConstants.Elem.QN_PROVIDER_NAME, sp.getProviderName());
            element(OWSConstants.Elem.QN_PROVIDER_SITE, sp.getProviderSite(),
                    (ElementWriter<Link>) this::writeXLinkAttrs);
            writeServiceContact(sp);
        });
    }

    protected void writeServiceIdentification(OwsCapabilities capabilities) throws XMLStreamException {
        element(OWSConstants.Elem.QN_SERVICE_IDENTIFICATION, capabilities.getServiceIdentification(), (
                OwsServiceIdentification si) -> {
            writeMultilingualString(OWSConstants.Elem.QN_TITLE, si.getTitle().orElse(null));
            writeMultilingualString(OWSConstants.Elem.QN_ABSTRACT, si.getAbstract().orElse(null));
            writeKeywords(si.getKeywords());
            writeCode(OWSConstants.Elem.QN_SERVICE_TYPE, si.getServiceType());
            for (String version : si.getServiceTypeVersion()) {
                element(OWSConstants.Elem.QN_SERVICE_TYPE_VERSION, version);
            }
            for (URI profile : si.getProfiles()) {
                element(OWSConstants.Elem.QN_PROFILE, profile.toString());
            }
            for (String fee : si.getFees()) {
                element(OWSConstants.Elem.QN_FEES, fee);
            }
            for (String accessConstraints : si.getAccessConstraints()) {
                element(OWSConstants.Elem.QN_ACCESS_CONSTRAINTS, accessConstraints);
            }
        });
    }

    protected void writeValueRestriction(OwsValueRestriction restriction) throws XMLStreamException {
        if (restriction.isRange()) {
            writeRange(restriction);
        } else if (restriction.isValue()) {
            writeValue(restriction);
        }
    }

    protected void writeAllowedValues(OwsAllowedValues allowedValues) throws XMLStreamException {
        element(OWSConstants.Elem.QN_ALLOWED_VALUES, () -> {
            for (OwsValueRestriction restriction : allowedValues) {
                writeValueRestriction(restriction);
            }
        });
    }

    protected void writeRange(OwsValueRestriction restriction) throws XMLStreamException {
        element(OWSConstants.Elem.QN_RANGE, restriction.asRange(), range -> {
            attr(OWSConstants.Attr.AN_RANGE_CLOSURE, range.getType());
            element(OWSConstants.Elem.QN_MINIMUM_VALUE, range.getLowerBound().map(OwsValue::getValue));
            element(OWSConstants.Elem.QN_MAXIMUM_VALUE, range.getUpperBound().map(OwsValue::getValue));
            element(OWSConstants.Elem.QN_SPACING, range.getSpacing().map(OwsValue::getValue));
        });
    }

    protected void writeValue(OwsValueRestriction restriction) throws XMLStreamException {
        element(OWSConstants.Elem.QN_VALUE, restriction.asValue().getValue());
    }

    protected void writePossibleValues(OwsPossibleValues possibleValues) throws XMLStreamException {
        if (possibleValues.isAllowedValues()) {
            writeAllowedValues(possibleValues.asAllowedValues());
        } else if (possibleValues.isAnyValue()) {
            writeAnyValue(possibleValues.asAnyValues());
        } else if (possibleValues.isNoValues()) {
            writeNoValues(possibleValues.asNoValues());
        } else if (possibleValues.isValuesReference()) {
            writeValuesReference(possibleValues.asValuesReference());
        }
    }

    protected void writeMetadata(Iterable<OwsMetadata> m) throws XMLStreamException {
        forEach(OWSConstants.Elem.QN_METADATA, m, metadata -> {
            writeXLinkAttrs(metadata);
            attr(OWSConstants.Attr.AN_ABOUT, metadata.getAbout().map(URI::toString));
        });
    }

    protected void writeConstraints(Iterable<OwsDomain> constraints) throws XMLStreamException {
        writeDomains(OWSConstants.Elem.QN_CONSTRAINT, constraints);
    }

    protected void writeRequestMethod(OwsRequestMethod method) throws XMLStreamException {
        QName name = null;
        if (method.getHttpMethod().equals(HTTPMethods.GET)) {
            name = OWSConstants.Elem.QN_GET;
        } else if (method.getHttpMethod().equals(HTTPMethods.POST)) {
            name = OWSConstants.Elem.QN_POST;
        }
        if (name != null) {
            element(name, () -> {
                writeXLinkAttrs(method);
                writeConstraints(method.getConstraints());
            });
        }
    }

    protected void writeOperations(OwsOperationsMetadata operationsMetadata) throws XMLStreamException {
        forEach(OWSConstants.Elem.QN_OPERATION, operationsMetadata.getOperations(), operation -> {
            attr(OWSConstants.Attr.AN_NAME, operation.getName());
            writeDCP(operation);
            writeParameters(operation.getParameters());
            writeConstraints(operation.getConstraints());
            writeMetadata(operation.getMetadata());
        });
    }

    protected void writeDomains(QName name,
            Iterable<OwsDomain> domains) throws XMLStreamException {
        forEach(name, domains, domain -> {
            attr(OWSConstants.Attr.AN_NAME, domain.getName());
            writePossibleValues(domain.getPossibleValues());
            element(OWSConstants.Elem.QN_DEFAULT_VALUE, domain.getDefaultValue().map(OwsValue::getValue));
            writeDomainMetadata(OWSConstants.Elem.QN_MEANING, domain.getMeaning());
            writeDomainMetadata(OWSConstants.Elem.QN_DATA_TYPE, domain.getDataType());
            writeDomainMetadata(OWSConstants.Elem.QN_REFERENCE_SYSTEM, domain.getValuesUnit().filter(
                    OwsValuesUnit::isReferenceSystem).map(OwsValuesUnit::asReferenceSystem));
            writeDomainMetadata(OWSConstants.Elem.QN_UOM, domain.getValuesUnit().filter(OwsValuesUnit::isUOM).map(
                    OwsValuesUnit::asUOM));
            writeMetadata(domain.getMetadata());
        });
    }

}
