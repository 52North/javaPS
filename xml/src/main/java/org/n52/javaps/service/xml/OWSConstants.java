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
package org.n52.javaps.service.xml;

import javax.xml.namespace.QName;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface OWSConstants {
    String NS_OWS = "http://www.opengis.net/ows/2.0";

    String NS_OWS_PREFIX = "ows";

    static QName ows(String element) {
        return new QName(NS_OWS, element, NS_OWS_PREFIX);
    }

    interface Attr {
        String AN_ABOUT = "about";

        QName QN_ABOUT = ows(AN_ABOUT);

        String AN_CODE_SPACE = "codeSpace";

        QName QN_CODE_SPACE = ows(AN_CODE_SPACE);

        String AN_CRS = "crs";

        QName QN_CRS = ows(AN_CRS);

        String AN_DIMENSIONS = "dimensions";

        QName QN_DIMENSIONS = ows(AN_DIMENSIONS);

        String AN_EXCEPTION_CODE = "exceptionCode";

        QName QN_EXCEPTION_CODE = ows(AN_EXCEPTION_CODE);

        String AN_LOCATOR = "locator";

        QName QN_LOCATOR = ows(AN_LOCATOR);

        String AN_NAME = "name";

        QName QN_NAME = ows(AN_NAME);

        String AN_NIL_REASON = "nilReason";

        QName QN_NIL_REASON = ows(AN_NIL_REASON);

        String AN_RANGE_CLOSURE = "rangeClosure";

        QName QN_RANGE_CLOSURE = ows(AN_RANGE_CLOSURE);

        String AN_REFERENCE = "reference";

        QName QN_REFERENCE = ows(AN_REFERENCE);

        String AN_SERVICE = "service";

        QName QN_SERVICE = ows(AN_SERVICE);

        String AN_TYPE = "type";

        QName QN_TYPE = ows(AN_TYPE);

        String AN_UPDATE_SEQUENCE = "updateSequence";

        QName QN_UPDATE_SEQUENCE = ows(AN_UPDATE_SEQUENCE);

        String AN_VERSION = "version";

        QName QN_VERSION = ows(AN_VERSION);
    }

    interface Elem {
        String EN_ABSTRACT = "Abstract";

        QName QN_ABSTRACT = ows(EN_ABSTRACT);

        String EN_ABSTRACT_META_DATA = "AbstractMetaData";

        QName QN_ABSTRACT_META_DATA = ows(EN_ABSTRACT_META_DATA);

        String EN_ABSTRACT_REFERENCE_BASE = "AbstractReferenceBase";

        QName QN_ABSTRACT_REFERENCE_BASE = ows(EN_ABSTRACT_REFERENCE_BASE);

        String EN_ACCEPT_FORMATS = "AcceptFormats";

        QName QN_ACCEPT_FORMATS = ows(EN_ACCEPT_FORMATS);

        String EN_ACCEPT_LANGUAGES = "AcceptLanguages";

        QName QN_ACCEPT_LANGUAGES = ows(EN_ACCEPT_LANGUAGES);

        String EN_ACCEPT_VERSIONS = "AcceptVersions";

        QName QN_ACCEPT_VERSIONS = ows(EN_ACCEPT_VERSIONS);

        String EN_ACCESS_CONSTRAINTS = "AccessConstraints";

        QName QN_ACCESS_CONSTRAINTS = ows(EN_ACCESS_CONSTRAINTS);

        String EN_ADDITIONAL_PARAMETER = "AdditionalParameter";

        QName QN_ADDITIONAL_PARAMETER = ows(EN_ADDITIONAL_PARAMETER);

        String EN_ADDITIONAL_PARAMETERS = "AdditionalParameters";

        QName QN_ADDITIONAL_PARAMETERS = ows(EN_ADDITIONAL_PARAMETERS);

        String EN_ADDRESS = "Address";

        QName QN_ADDRESS = ows(EN_ADDRESS);

        String EN_ADMINISTRATIVE_AREA = "AdministrativeArea";

        QName QN_ADMINISTRATIVE_AREA = ows(EN_ADMINISTRATIVE_AREA);

        String EN_ALLOWED_VALUES = "AllowedValues";

        QName QN_ALLOWED_VALUES = ows(EN_ALLOWED_VALUES);

        String EN_ANY_VALUE = "AnyValue";

        QName QN_ANY_VALUE = ows(EN_ANY_VALUE);

        String EN_AVAILABLE_CRS = "AvailableCRS";

        QName QN_AVAILABLE_CRS = ows(EN_AVAILABLE_CRS);

        String EN_BOUNDING_BOX = "BoundingBox";

        QName QN_BOUNDING_BOX = ows(EN_BOUNDING_BOX);

        String EN_CITY = "City";

        QName QN_CITY = ows(EN_CITY);

        String EN_CONSTRAINT = "Constraint";

        QName QN_CONSTRAINT = ows(EN_CONSTRAINT);

        String EN_CONTACT_INFO = "ContactInfo";

        QName QN_CONTACT_INFO = ows(EN_CONTACT_INFO);

        String EN_CONTACT_INSTRUCTIONS = "ContactInstructions";

        QName QN_CONTACT_INSTRUCTIONS = ows(EN_CONTACT_INSTRUCTIONS);

        String EN_COUNTRY = "Country";

        QName QN_COUNTRY = ows(EN_COUNTRY);

        String EN_DATASET_DESCRIPTION_SUMMARY = "DatasetDescriptionSummary";

        QName QN_DATASET_DESCRIPTION_SUMMARY = ows(EN_DATASET_DESCRIPTION_SUMMARY);

        String EN_DATA_TYPE = "DataType";

        QName QN_DATA_TYPE = ows(EN_DATA_TYPE);

        String EN_DCP = "DCP";

        QName QN_DCP = ows(EN_DCP);

        String EN_DEFAULT_VALUE = "DefaultValue";

        QName QN_DEFAULT_VALUE = ows(EN_DEFAULT_VALUE);

        String EN_DELIVERY_POINT = "DeliveryPoint";

        QName QN_DELIVERY_POINT = ows(EN_DELIVERY_POINT);

        String EN_ELECTRONIC_MAIL_ADDRESS = "ElectronicMailAddress";

        QName QN_ELECTRONIC_MAIL_ADDRESS = ows(EN_ELECTRONIC_MAIL_ADDRESS);

        String EN_EXCEPTION = "Exception";

        QName QN_EXCEPTION = ows(EN_EXCEPTION);

        String EN_EXCEPTION_REPORT = "ExceptionReport";

        QName QN_EXCEPTION_REPORT = ows(EN_EXCEPTION_REPORT);

        String EN_EXCEPTION_TEXT = "ExceptionText";

        QName QN_EXCEPTION_TEXT = ows(EN_EXCEPTION_TEXT);

        String EN_EXTENDED_CAPABILITIES = "ExtendedCapabilities";

        QName QN_EXTENDED_CAPABILITIES = ows(EN_EXTENDED_CAPABILITIES);

        String EN_FACSIMILE = "Facsimile";

        QName QN_FACSIMILE = ows(EN_FACSIMILE);

        String EN_FEES = "Fees";

        QName QN_FEES = ows(EN_FEES);

        String EN_FORMAT = "Format";

        QName QN_FORMAT = ows(EN_FORMAT);

        String EN_GET = "Get";

        QName QN_GET = ows(EN_GET);

        String EN_GET_CAPABILITIES = "GetCapabilities";

        QName QN_GET_CAPABILITIES = ows(EN_GET_CAPABILITIES);

        String EN_GET_RESOURCE_BY_ID = "GetResourceByID";

        QName QN_GET_RESOURCE_BY_ID = ows(EN_GET_RESOURCE_BY_ID);

        String EN_HOURS_OF_SERVICE = "HoursOfService";

        QName QN_HOURS_OF_SERVICE = ows(EN_HOURS_OF_SERVICE);

        String EN_HTTP = "HTTP";

        QName QN_HTTP = ows(EN_HTTP);

        String EN_IDENTIFIER = "Identifier";

        QName QN_IDENTIFIER = ows(EN_IDENTIFIER);

        String EN_INDIVIDUAL_NAME = "IndividualName";

        QName QN_INDIVIDUAL_NAME = ows(EN_INDIVIDUAL_NAME);

        String EN_INPUT_DATA = "InputData";

        QName QN_INPUT_DATA = ows(EN_INPUT_DATA);

        String EN_KEYWORD = "Keyword";

        QName QN_KEYWORD = ows(EN_KEYWORD);

        String EN_KEYWORDS = "Keywords";

        QName QN_KEYWORDS = ows(EN_KEYWORDS);

        String EN_LANGUAGE = "Language";

        QName QN_LANGUAGE = ows(EN_LANGUAGE);

        String EN_LANGUAGES = "Languages";

        QName QN_LANGUAGES = ows(EN_LANGUAGES);

        String EN_LOWER_CORNER = "LowerCorner";

        QName QN_LOWER_CORNER = ows(EN_LOWER_CORNER);

        String EN_MANIFEST = "Manifest";

        QName QN_MANIFEST = ows(EN_MANIFEST);

        String EN_MAXIMUM_VALUE = "MaximumValue";

        QName QN_MAXIMUM_VALUE = ows(EN_MAXIMUM_VALUE);

        String EN_MEANING = "Meaning";

        QName QN_MEANING = ows(EN_MEANING);

        String EN_METADATA = "Metadata";

        QName QN_METADATA = ows(EN_METADATA);

        String EN_MINIMUM_VALUE = "MinimumValue";

        QName QN_MINIMUM_VALUE = ows(EN_MINIMUM_VALUE);

        String EN_NAME = "Name";

        QName QN_NAME = ows(EN_NAME);

        String EN_NIL_VALUE = "nilValue";

        QName QN_NIL_VALUE = ows(EN_NIL_VALUE);

        String EN_NO_VALUES = "NoValues";

        QName QN_NO_VALUES = ows(EN_NO_VALUES);

        String EN_ONLINE_RESOURCE = "OnlineResource";

        QName QN_ONLINE_RESOURCE = ows(EN_ONLINE_RESOURCE);

        String EN_OPERATION = "Operation";

        QName QN_OPERATION = ows(EN_OPERATION);

        String EN_OPERATION_RESPONSE = "OperationResponse";

        QName QN_OPERATION_RESPONSE = ows(EN_OPERATION_RESPONSE);

        String EN_OPERATIONS_METADATA = "OperationsMetadata";

        QName QN_OPERATIONS_METADATA = ows(EN_OPERATIONS_METADATA);

        String EN_ORGANISATION_NAME = "OrganisationName";

        QName QN_ORGANISATION_NAME = ows(EN_ORGANISATION_NAME);

        String EN_OTHER_SOURCE = "OtherSource";

        QName QN_OTHER_SOURCE = ows(EN_OTHER_SOURCE);

        String EN_OUTPUT_FORMAT = "OutputFormat";

        QName QN_OUTPUT_FORMAT = ows(EN_OUTPUT_FORMAT);

        String EN_PARAMETER = "Parameter";

        QName QN_PARAMETER = ows(EN_PARAMETER);

        String EN_PHONE = "Phone";

        QName QN_PHONE = ows(EN_PHONE);

        String EN_POINT_OF_CONTACT = "PointOfContact";

        QName QN_POINT_OF_CONTACT = ows(EN_POINT_OF_CONTACT);

        String EN_POSITION_NAME = "PositionName";

        QName QN_POSITION_NAME = ows(EN_POSITION_NAME);

        String EN_POST = "Post";

        QName QN_POST = ows(EN_POST);

        String EN_POSTAL_CODE = "PostalCode";

        QName QN_POSTAL_CODE = ows(EN_POSTAL_CODE);

        String EN_PROFILE = "Profile";

        QName QN_PROFILE = ows(EN_PROFILE);

        String EN_PROVIDER_NAME = "ProviderName";

        QName QN_PROVIDER_NAME = ows(EN_PROVIDER_NAME);

        String EN_PROVIDER_SITE = "ProviderSite";

        QName QN_PROVIDER_SITE = ows(EN_PROVIDER_SITE);

        String EN_RANGE = "Range";

        QName QN_RANGE = ows(EN_RANGE);

        String EN_REFERENCE = "Reference";

        QName QN_REFERENCE = ows(EN_REFERENCE);

        String EN_REFERENCE_GROUP = "ReferenceGroup";

        QName QN_REFERENCE_GROUP = ows(EN_REFERENCE_GROUP);

        String EN_REFERENCE_SYSTEM = "ReferenceSystem";

        QName QN_REFERENCE_SYSTEM = ows(EN_REFERENCE_SYSTEM);

        String EN_REQUEST_MESSAGE = "RequestMessage";

        QName QN_REQUEST_MESSAGE = ows(EN_REQUEST_MESSAGE);

        String EN_REQUEST_MESSAGE_REFERENCE = "RequestMessageReference";

        QName QN_REQUEST_MESSAGE_REFERENCE = ows(EN_REQUEST_MESSAGE_REFERENCE);

        String EN_RESOURCE = "Resource";

        QName QN_RESOURCE = ows(EN_RESOURCE);

        String EN_RESOURCE_ID = "ResourceID";

        QName QN_RESOURCE_ID = ows(EN_RESOURCE_ID);

        String EN_ROLE = "Role";

        QName QN_ROLE = ows(EN_ROLE);

        String EN_SECTION = "Section";

        QName QN_SECTION = ows(EN_SECTION);

        String EN_SECTIONS = "Sections";

        QName QN_SECTIONS = ows(EN_SECTIONS);

        String EN_SERVICE_CONTACT = "ServiceContact";

        QName QN_SERVICE_CONTACT = ows(EN_SERVICE_CONTACT);

        String EN_SERVICE_IDENTIFICATION = "ServiceIdentification";

        QName QN_SERVICE_IDENTIFICATION = ows(EN_SERVICE_IDENTIFICATION);

        String EN_SERVICE_PROVIDER = "ServiceProvider";

        QName QN_SERVICE_PROVIDER = ows(EN_SERVICE_PROVIDER);

        String EN_SERVICE_REFERENCE = "ServiceReference";

        QName QN_SERVICE_REFERENCE = ows(EN_SERVICE_REFERENCE);

        String EN_SERVICE_TYPE = "ServiceType";

        QName QN_SERVICE_TYPE = ows(EN_SERVICE_TYPE);

        String EN_SERVICE_TYPE_VERSION = "ServiceTypeVersion";

        QName QN_SERVICE_TYPE_VERSION = ows(EN_SERVICE_TYPE_VERSION);

        String EN_SPACING = "Spacing";

        QName QN_SPACING = ows(EN_SPACING);

        String EN_SUPPORTED_CRS = "SupportedCRS";

        QName QN_SUPPORTED_CRS = ows(EN_SUPPORTED_CRS);

        String EN_TITLE = "Title";

        QName QN_TITLE = ows(EN_TITLE);

        String EN_TYPE = "Type";

        QName QN_TYPE = ows(EN_TYPE);

        String EN_UOM = "UOM";

        QName QN_UOM = ows(EN_UOM);

        String EN_UPPER_CORNER = "UpperCorner";

        QName QN_UPPER_CORNER = ows(EN_UPPER_CORNER);

        String EN_VALUE = "Value";

        QName QN_VALUE = ows(EN_VALUE);

        String EN_VALUES_REFERENCE = "ValuesReference";

        QName QN_VALUES_REFERENCE = ows(EN_VALUES_REFERENCE);

        String EN_VERSION = "Version";

        QName QN_VERSION = ows(EN_VERSION);

        String EN_VOICE = "Voice";

        QName QN_VOICE = ows(EN_VOICE);

        String EN_WGS84_BOUNDING_BOX = "WGS84BoundingBox";

        QName QN_WGS84_BOUNDING_BOX = ows(EN_WGS84_BOUNDING_BOX);
    }

}
