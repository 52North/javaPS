package org.n52.javaps.coding.stream.xml.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.i18n.MultilingualString;
import org.n52.iceland.util.http.HTTPMethods;
import org.n52.javaps.coding.stream.xml.AbstractMultiElementXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.OWS;
import org.n52.javaps.ogc.ows.OwsAddress;
import org.n52.javaps.ogc.ows.OwsAllowedValues;
import org.n52.javaps.ogc.ows.OwsAnyValue;
import org.n52.javaps.ogc.ows.OwsCapabilities;
import org.n52.javaps.ogc.ows.OwsCode;
import org.n52.javaps.ogc.ows.OwsContact;
import org.n52.javaps.ogc.ows.OwsDCP;
import org.n52.javaps.ogc.ows.OwsDomain;
import org.n52.javaps.ogc.ows.OwsDomainMetadata;
import org.n52.javaps.ogc.ows.OwsKeyword;
import org.n52.javaps.ogc.ows.OwsLanguageString;
import org.n52.javaps.ogc.ows.OwsMetadata;
import org.n52.javaps.ogc.ows.OwsNoValues;
import org.n52.javaps.ogc.ows.OwsOperation;
import org.n52.javaps.ogc.ows.OwsOperationsMetadata;
import org.n52.javaps.ogc.ows.OwsPhone;
import org.n52.javaps.ogc.ows.OwsPossibleValues;
import org.n52.javaps.ogc.ows.OwsRequestMethod;
import org.n52.javaps.ogc.ows.OwsResponsibleParty;
import org.n52.javaps.ogc.ows.OwsServiceIdentification;
import org.n52.javaps.ogc.ows.OwsServiceProvider;
import org.n52.javaps.ogc.ows.OwsValue;
import org.n52.javaps.ogc.ows.OwsValueRestriction;
import org.n52.javaps.ogc.ows.OwsValuesReference;
import org.n52.javaps.ogc.ows.OwsValuesUnit;
import org.n52.javaps.ogc.wps.WPSCapabilities;

import com.google.common.base.Strings;

import static java.util.stream.Collectors.groupingBy;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractOWSWriter extends AbstractMultiElementXmlStreamWriter {

    public AbstractOWSWriter(Class<?>... supportedClasses) {
        super(supportedClasses);
    }

    protected void writeOperationsMetadata(WPSCapabilities capabilities)
            throws XMLStreamException {
        element(OWS.Elem.QN_OPERATIONS_METADATA, capabilities
                .getOperationsMetadata(), (OwsOperationsMetadata operationsMetadata) -> {
                    writeOperations(operationsMetadata);
                    writeParameters(operationsMetadata.getParameters());
                    writeConstraints(operationsMetadata.getConstraints());
                });
    }

    protected void writeKeywords(Set<OwsKeyword> set)
            throws XMLStreamException {
        if (!set.isEmpty()) {
            element(OWS.Elem.QN_KEYWORDS, set, x -> {
                Map<Optional<OwsCode>, Set<OwsLanguageString>> keywords = x.stream()
                        .collect(groupingBy(OwsKeyword::getType, TreeMap::new, mapping(OwsKeyword::getKeyword, toCollection(TreeSet::new))));
                for (Entry<Optional<OwsCode>, Set<OwsLanguageString>> entry : keywords.entrySet()) {
                    Optional<OwsCode> type = entry.getKey();
                    for (OwsLanguageString keyword : entry.getValue()) {
                        writeLanguageString(OWS.Elem.QN_KEYWORD, keyword);
                    }
                    if (type.isPresent()) {
                        writeCode(OWS.Elem.QN_TYPE, type.get());
                    }
                }
            });
        }
    }

     protected void writeLanguageString(QName name, OwsLanguageString value)
            throws XMLStreamException {
        element(name, value, x -> {
            if (x.getLang().isPresent()) {
                attr("xml:lang", x.getLang().get());
            }
            chars(x.getValue());
        });
    }

    protected void writeDomainMetadata(QName name, OwsDomainMetadata dmd)
                throws XMLStreamException {
        writeDomainMetadata(name, Optional.ofNullable(dmd));
    }
    protected void writeDomainMetadata(QName name, Optional<OwsDomainMetadata> metadata) throws XMLStreamException {
        element(name, metadata, (OwsDomainMetadata x) -> {
            attr(OWS.Attr.AN_REFERENCE, x.getReference().map(URI::toString));
            chars(x.getValue());
        });
    }

    protected void writeLanguageString(QName name, Optional<OwsLanguageString> value)
            throws XMLStreamException {
        if (value.isPresent()) {
            writeLanguageString(name, value.get());
        }
    }

    protected void writeMultilingualString(QName name, MultilingualString ms) throws XMLStreamException {
        forEach(name, ms, ls-> {
            langAttr(ls.getLang());
            chars(ls.getText());
        });
    }

    protected void langAttr(Locale lang) throws XMLStreamException {
        attr("xml:lang", Optional.of(Strings.emptyToNull(LocaleHelper.toString(lang))));
    }

    protected void writeCode(QName name, Optional<OwsCode> id)
            throws XMLStreamException {
        element(name, id, (OwsCode x) -> {
            attr(OWS.Attr.AN_CODE_SPACE, x.getCodeSpace().map(URI::toString));
            chars(x.getValue());
        });
    }
    protected void writeCode(QName name, OwsCode id)
            throws XMLStreamException {
        writeCode(name, Optional.ofNullable(id));
    }


    protected void writeHTTP(OwsDCP dcp)
            throws XMLStreamException {
        element(OWS.Elem.QN_HTTP, dcp.asHTTP(), http -> {
            for (OwsRequestMethod method : http.getRequestMethods()) {
                writeRequestMethod(method);
            }
        });
    }

    protected void writeDCP(OwsOperation operation)
            throws XMLStreamException {
        forEach(OWS.Elem.QN_DCP, operation.getDCP(), dcp -> {
            if (dcp.isHTTP()) {
                writeHTTP(dcp);
            }
        });
    }

    protected void writeParameters(List<OwsDomain> parameters)
            throws XMLStreamException {
        writeDomains(OWS.Elem.QN_PARAMETER, parameters);
    }

    protected void writeLanguages(OwsCapabilities capabilities)
            throws XMLStreamException {
        element(OWS.Elem.QN_LANGUAGES, capabilities.getLanguages(), (Set<String> languages) -> {
            forEach(OWS.Elem.QN_LANGUAGE, languages, this::chars);
        });
    }

    protected void writeAnyValue(OwsAnyValue anyValue)
            throws XMLStreamException {
        empty(OWS.Elem.QN_ANY_VALUE);
    }

    protected void writeNoValues(OwsNoValues noValues)
            throws XMLStreamException {
        empty(OWS.Elem.QN_NO_VALUES);
    }

    protected void writeValuesReference(OwsValuesReference valuesReference)
            throws XMLStreamException {
        element(OWS.Elem.QN_VALUES_REFERENCE, () -> {
            attr(OWS.Attr.AN_REFERENCE, valuesReference.getReference().toString());
            chars(valuesReference.getValue());
        });
    }

    protected void writeContactInfo(OwsResponsibleParty serviceContact)
            throws XMLStreamException {
        element(OWS.Elem.QN_CONTACT_INFO, serviceContact.getContactInfo(), (OwsContact contactInfo) -> {
            writePhone(contactInfo);
            writeAddress(contactInfo);
            element(OWS.Elem.QN_ONLINE_RESOURCE, contactInfo.getOnlineResource(), this::writeXLinkAttrs);
            element(OWS.Elem.QN_HOURS_OF_SERVICE, contactInfo
                    .getHoursOfService());
            element(OWS.Elem.QN_CONTACT_INSTRUCTIONS, contactInfo
                    .getContactInstructions());
            writeCode(OWS.Elem.QN_ROLE, serviceContact.getRole());
        });
    }

    protected void writeAddress(OwsContact contactInfo)
            throws XMLStreamException {
        element(OWS.Elem.QN_ADDRESS, contactInfo.getAddress(), (OwsAddress address) -> {
            forEach(OWS.Elem.QN_DELIVERY_POINT, address.getDeliveryPoint(), this::chars);
            element(OWS.Elem.QN_CITY, address.getCity());
            element(OWS.Elem.QN_ADMINISTRATIVE_AREA, address.getAdministrativeArea());
            element(OWS.Elem.QN_POSTAL_CODE, address.getPostalCode());
            element(OWS.Elem.QN_COUNTRY, address.getCountry());
            forEach(OWS.Elem.QN_ELECTRONIC_MAIL_ADDRESS, address.getElectronicMailAddress(), this::chars);
        });
    }

    protected void writePhone(OwsContact contactInfo)
            throws XMLStreamException {
        element(OWS.Elem.QN_PHONE, contactInfo.getPhone(), (OwsPhone phone) -> {
            forEach(OWS.Elem.QN_VOICE, phone.getVoice(), this::chars);
            forEach(OWS.Elem.QN_FACSIMILE, phone.getFacsimile(), this::chars);
        });
    }

    protected void writeServiceContact(OwsServiceProvider sp)
            throws XMLStreamException {
        element(OWS.Elem.QN_SERVICE_CONTACT, sp.getServiceContact(), serviceContact -> {
            element(OWS.Elem.QN_INDIVIDUAL_NAME, serviceContact
                    .getIndividualName());
            element(OWS.Elem.QN_POSITION_NAME, serviceContact.getPositionName());
            writeContactInfo(serviceContact);
        });
    }

    protected void writeServiceProvider(OwsCapabilities capabilities)
            throws XMLStreamException {
        element(OWS.Elem.QN_SERVICE_PROVIDER, capabilities.getServiceProvider(), (OwsServiceProvider sp) -> {
            element(OWS.Elem.QN_PROVIDER_NAME, sp.getProviderName());
            element(OWS.Elem.QN_PROVIDER_SITE, sp.getProviderSite(), this::writeXLinkAttrs);
            writeServiceContact(sp);
        });
    }

    protected void writeServiceIdentification(OwsCapabilities capabilities)
            throws XMLStreamException {
        element(OWS.Elem.QN_SERVICE_IDENTIFICATION, capabilities
                .getServiceIdentification(), (OwsServiceIdentification si) -> {
                    writeMultilingualString(OWS.Elem.QN_TITLE, si.getTitle().orElse(null));
                    writeMultilingualString(OWS.Elem.QN_ABSTRACT, si.getAbstract().orElse(null));
                    writeKeywords(si.getKeywords());
                    writeCode(OWS.Elem.QN_SERVICE_TYPE, si.getServiceType());
                    for (String version : si.getServiceTypeVersion()) {
                        element(OWS.Elem.QN_SERVICE_TYPE_VERSION, version);
                    }
                    for (URI profile : si.getProfiles()) {
                        element(OWS.Elem.QN_PROFILE, profile.toString());
                    }
                    for (String fee : si.getFees()) {
                        element(OWS.Elem.QN_FEES, fee);
                    }
                    for (String accessConstraints : si.getAccessConstraints()) {
                        element(OWS.Elem.QN_ACCESS_CONSTRAINTS, accessConstraints);
                    }
                });
    }

    protected void writeValueRestriction(OwsValueRestriction restriction)
            throws XMLStreamException {
        if (restriction.isRange()) {
            writeRange(restriction);
        } else if (restriction.isValue()) {
            writeValue(restriction);
        }
    }

    protected void writeAllowedValues(OwsAllowedValues allowedValues)
            throws XMLStreamException {
        element(OWS.Elem.QN_ALLOWED_VALUES, () -> {
            for (OwsValueRestriction restriction : allowedValues) {
                writeValueRestriction(restriction);
            }
        });
    }

    protected void writeRange(OwsValueRestriction restriction)
            throws XMLStreamException {
        element(OWS.Elem.QN_RANGE, restriction.asRange(), range -> {
            attr(OWS.Attr.AN_RANGE_CLOSURE, range.getType());
            element(OWS.Elem.QN_MINIMUM_VALUE, range.getLowerBound()
                    .map(OwsValue::getValue));
            element(OWS.Elem.QN_MAXIMUM_VALUE, range.getUpperBound()
                    .map(OwsValue::getValue));
            element(OWS.Elem.QN_SPACING, range.getSpacing()
                    .map(OwsValue::getValue));
        });
    }

    protected void writeValue(OwsValueRestriction restriction)
            throws XMLStreamException {
        element(OWS.Elem.QN_VALUE, restriction.asValue().getValue());
    }

    protected void writePossibleValues(OwsPossibleValues possibleValues)
            throws XMLStreamException {
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

    protected void writeMetadata(Iterable<OwsMetadata> m)
            throws XMLStreamException {
        forEach(OWS.Elem.QN_METADATA, m, metadata -> {
            writeXLinkAttrs(metadata);
            attr(OWS.Attr.AN_ABOUT, metadata.getAbout().map(URI::toString));
        });
    }

    protected void writeConstraints(Iterable<OwsDomain> constraints)
            throws XMLStreamException {
        writeDomains(OWS.Elem.QN_CONSTRAINT, constraints);
    }

    protected void writeRequestMethod(OwsRequestMethod method)
            throws XMLStreamException {
        if (method.getHttpMethod().equals(HTTPMethods.GET)) {
            element(OWS.Elem.QN_GET, () -> {
                writeXLinkAttrs(method);
                writeConstraints(method.getConstraints());
            });
        } else if (method.getHttpMethod().equals(HTTPMethods.POST)) {
            element(OWS.Elem.QN_POST, method, this::writeXLinkAttrs);
        }
    }

    protected void writeOperations(OwsOperationsMetadata operationsMetadata)
            throws XMLStreamException {
        forEach(OWS.Elem.QN_OPERATION, operationsMetadata.getOperations(), operation -> {
            attr(OWS.Attr.AN_NAME, operation.getName());
            writeDCP(operation);
            writeParameters(operation.getParameters());
            writeConstraints(operation.getConstraints());
            writeMetadata(operation.getMetadata());
        });
    }

    protected void writeDomains(QName name, Iterable<OwsDomain> domains)
            throws XMLStreamException {
        forEach(name, domains, domain -> {
            attr(OWS.Attr.AN_NAME, domain.getName());
            writePossibleValues(domain.getPossibleValues());
            element(OWS.Elem.QN_DEFAULT_VALUE, domain.getDefaultValue()
                    .map(OwsValue::getValue));
            writeDomainMetadata(OWS.Elem.QN_MEANING, domain.getMeaning());
            writeDomainMetadata(OWS.Elem.QN_DATA_TYPE, domain.getDataType());
            writeDomainMetadata(OWS.Elem.QN_REFERENCE_SYSTEM, domain
                                .getValuesUnit()
                                .filter(OwsValuesUnit::isReferenceSystem)
                                .map(OwsValuesUnit::asReferenceSystem));
            writeDomainMetadata(OWS.Elem.QN_UOM, domain.getValuesUnit()
                                .filter(OwsValuesUnit::isUOM)
                                .map(OwsValuesUnit::asUOM));
            writeMetadata(domain.getMetadata());
        });
    }

}
