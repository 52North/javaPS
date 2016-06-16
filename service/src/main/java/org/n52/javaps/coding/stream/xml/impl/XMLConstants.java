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
package org.n52.javaps.coding.stream.xml.impl;

import javax.xml.namespace.QName;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface XMLConstants {

    public interface QNames {
        QName OWS_ABSTRACT = ows(Entities.OWS_ABSTRACT);
        QName OWS_ALLOWED_VALUES = ows(Entities.OWS_ALLOWED_VALUES);
        QName OWS_ANY_VALUE = ows(Entities.OWS_ANY_VALUE);
        QName OWS_DATA_TYPE = ows(Entities.OWS_DATA_TYPE);
        QName OWS_DEFAULT_VALUE = ows(Entities.OWS_DEFAULT_VALUE);
        QName OWS_EXCEPTION = ows(Entities.OWS_EXCEPTION);
        QName OWS_EXCEPTION_REPORT = ows(Entities.OWS_EXCEPTION_REPORT);
        QName OWS_EXCEPTION_TEXT = ows(Entities.OWS_EXCEPTION_TEXT);
        QName OWS_IDENTIFIER = ows(Entities.OWS_IDENTIFIER);
        QName OWS_KEYWORD = ows(Entities.OWS_KEYWORD);
        QName OWS_KEYWORDS = ows(Entities.OWS_KEYWORDS);
        QName OWS_MAXIMUM_VALUE = ows(Entities.OWS_MAXIMUM_VALUE);
        QName OWS_METADATA = ows(Entities.OWS_METADATA);
        QName OWS_MINIMUM_VALUE = ows(Entities.OWS_MINIMUM_VALUE);
        QName OWS_RANGE = ows(Entities.OWS_RANGE);
        QName OWS_REFERENCE = ows(Attributes.OWS_REFERENCE);
        QName OWS_SPACING = ows(Entities.OWS_SPACING);
        QName OWS_TITLE = ows(Entities.OWS_TITLE);
        QName OWS_TYPE = ows(Entities.OWS_TYPE);
        QName OWS_UOM = ows(Entities.OWS_UOM);
        QName OWS_VALUE = ows(Entities.OWS_VALUE);
        QName OWS_VALUES_REFERENCE = ows(Entities.OWS_VALUES_REFERENCE);
        QName OWS_VERSION = ows(Entities.OWS_VERSION);
        QName WPS_BOUNDING_BOX_DATA = wps(Entities.WPS_BOUNDING_BOX_DATA);
        QName WPS_COMPLEX_DATA = wps(Entities.WPS_COMPLEX_DATA);
        QName WPS_DATA_DESCRIPTION = wps(Entities.WPS_DATA_DESCRIPTION);
        QName WPS_FORMAT = wps(Entities.WPS_FORMAT);
        QName WPS_INPUT = wps(Entities.WPS_INPUT);
        QName WPS_LITERAL_DATA = wps(Entities.WPS_LITERAL_DATA);
        QName WPS_LITERAL_DATA_DOMAIN = wps(Entities.WPS_LITERAL_DATA_DOMAIN);
        QName WPS_LITERAL_VALUE = wps(Entities.WPS_LITERAL_VALUE);
        QName WPS_OUTPUT = wps(Entities.WPS_OUTPUT);
        QName WPS_PROCESS = wps(Entities.WPS_PROCESS);
        QName WPS_PROCESS_OFFERING = wps(Entities.WPS_PROCESS_OFFERING);
        QName WPS_PROCESS_OFFERINGS = wps(Entities.WPS_PROCESS_OFFERINGS);
        QName WPS_SUPPORTED_CRS = wps(Entities.WPS_SUPPORTED_CRS);

        static QName wps(String element) {
            return new QName(Namespaces.WPS_20, element, Namespaces.WPS_PREFIX);
        }

        static QName ows(String element) {
            return new QName(Namespaces.OWS_20, element, Namespaces.OWS_PREFIX);
        }
    }

    public interface Namespaces {
        String OWS_20 = "http://www.opengis.net/ows/2.0";
        String OWS_PREFIX = "ows";//TODO setting
        String WPS_20 = "http://www.opengis.net/wps/2.0";
        String WPS_PREFIX = "wps";

    }

    public interface Attributes {
        String OWS_CODE_SPACE = "codeSpace";
        String OWS_DATA_TYPE = "dataType";
        String OWS_EXCEPTION_CODE = "exceptionCode";
        String OWS_LOCATOR = "locator";
        String OWS_RANGE_CLOSURE = "rangeClosure";
        String OWS_REFERENCE = "reference";
        String OWS_UOM = "UOM";
        String WPS_DEFAULT = "default";
        String WPS_ENCODING = "encoding";
        String WPS_JOB_CONTROL_OPTIONS = "jobControlOptions";
        String WPS_MAXIMUM_MEGABYTES = "maximumMegabytes";
        String WPS_MIME_TYPE = "mimeType";
        String WPS_OUTPUT_TRANSMISSION = "outputTransmission";
        String WPS_PROCESS_MODEL = "processModel";
        String WPS_PROCESS_VERSION = "processVersion";
        String WPS_SCHEMA = "schema";
        String XS_MAX_OCCURS = "maxOccurs";
        String XS_MIN_OCCURS = "minOccurs";
    }

    public interface Entities {
        String OWS_ABSTRACT = "Abstract";
        String OWS_ALLOWED_VALUES = "AllowedValues";
        String OWS_ANY_VALUE = "AnyValue";
        String OWS_DATA_TYPE = "DataType";
        String OWS_DEFAULT_VALUE = "DefaultValue";
        String OWS_EXCEPTION = "Exception";
        String OWS_EXCEPTION_REPORT = "ExceptionReport";
        String OWS_EXCEPTION_TEXT = "ExceptionText";
        String OWS_IDENTIFIER = "Identifier";
        String OWS_KEYWORD = "Keyword";
        String OWS_KEYWORDS = "Keywords";
        String OWS_MAXIMUM_VALUE = "MaximumValue";
        String OWS_METADATA = "Metadata";
        String OWS_MINIMUM_VALUE = "MinimumValue";
        String OWS_RANGE = "Range";
        String OWS_SPACING = "Spacing";
        String OWS_TITLE = "Title";
        String OWS_TYPE = "type";
        String OWS_UOM = "UOM";
        String OWS_VALUE = "Value";
        String OWS_VALUES_REFERENCE = "ValuesReference";
        String OWS_VERSION = "version";
        String WPS_BOUNDING_BOX_DATA = "BoundingBoxData";
        String WPS_COMPLEX_DATA = "ComplexData";
        String WPS_DATA_DESCRIPTION = "DataDescription";
        String WPS_FORMAT = "Format";
        String WPS_INPUT = "Input";
        String WPS_LITERAL_DATA = "LiteralData";
        String WPS_LITERAL_DATA_DOMAIN = "LiteralDataDomain";
        String WPS_LITERAL_VALUE = " LiteralValue";
        String WPS_OUTPUT = "Output";
        String WPS_PROCESS = "Process";
        String WPS_PROCESS_OFFERING = "ProcessOffering";
        String WPS_PROCESS_OFFERINGS = "ProcessOfferings";
        String WPS_SUPPORTED_CRS = "SupportedCRS";
    }
}
