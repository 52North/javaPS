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
package org.n52.javaps.service.xml;

import javax.xml.namespace.QName;


/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public interface WPSConstants {
    String NS_WPS = "http://www.opengis.net/wps/2.0";
    String NS_WPS_PREFIX = "wps";

    public static interface Attr {
        String AN_DATA_TYPE = "dataType";
        QName QN_DATA_TYPE = wps(AN_DATA_TYPE);
        String AN_DEFAULT = "default";
        QName QN_DEFAULT = wps(AN_DEFAULT);
        String AN_ENCODING = "encoding";
        QName QN_ENCODING = wps(AN_ENCODING);
        String AN_ID = "id";
        QName QN_ID = wps(AN_ID);
        String AN_JOB_CONTROL_OPTIONS = "jobControlOptions";
        QName QN_JOB_CONTROL_OPTIONS = wps(AN_JOB_CONTROL_OPTIONS);
        String AN_MAXIMUM_MEGABYTES = "maximumMegabytes";
        QName QN_MAXIMUM_MEGABYTES = wps(AN_MAXIMUM_MEGABYTES);
        String AN_MIME_TYPE = "mimeType";
        QName QN_MIME_TYPE = wps(AN_MIME_TYPE);
        String AN_MODE = "mode";
        QName QN_MODE = wps(AN_MODE);
        String AN_OUTPUT_TRANSMISSION = "outputTransmission";
        QName QN_OUTPUT_TRANSMISSION = wps(AN_OUTPUT_TRANSMISSION);
        String AN_PROCESS_MODEL = "processModel";
        QName QN_PROCESS_MODEL = wps(AN_PROCESS_MODEL);
        String AN_PROCESS_VERSION = "processVersion";
        QName QN_PROCESS_VERSION = wps(AN_PROCESS_VERSION);
        String AN_RESPONSE = "response";
        QName QN_RESPONSE = wps(AN_RESPONSE);
        String AN_SCHEMA = "schema";
        QName QN_SCHEMA = wps(AN_SCHEMA);
        String AN_SERVICE = "service";
        QName QN_SERVICE = wps(AN_SERVICE);
        String AN_TRANSMISSION = "transmission";
        QName QN_TRANSMISSION = wps(AN_TRANSMISSION);
        String AN_UOM = "uom";
        QName QN_UOM = wps(AN_UOM);
        String AN_VERSION = "version";
        QName QN_VERSION = wps(AN_VERSION);
    }

    public static interface Elem {
        String EN_BODY = "Body";
        QName QN_BODY = wps(EN_BODY);
        String EN_BODY_REFERENCE = "BodyReference";
        QName QN_BODY_REFERENCE = wps(EN_BODY_REFERENCE);
        String EN_BOUNDING_BOX_DATA = "BoundingBoxData";
        QName QN_BOUNDING_BOX_DATA = wps(EN_BOUNDING_BOX_DATA);
        String EN_CAPABILITIES = "Capabilities";
        QName QN_CAPABILITIES = wps(EN_CAPABILITIES);
        String EN_COMPLEX_DATA = "ComplexData";
        QName QN_COMPLEX_DATA = wps(EN_COMPLEX_DATA);
        String EN_CONTENTS = "Contents";
        QName QN_CONTENTS = wps(EN_CONTENTS);
        String EN_DATA = "Data";
        QName QN_DATA = wps(EN_DATA);
        String EN_DATA_DESCRIPTION = "DataDescription";
        QName QN_DATA_DESCRIPTION = wps(EN_DATA_DESCRIPTION);
        String EN_DESCRIBE_PROCESS = "DescribeProcess";
        QName QN_DESCRIBE_PROCESS = wps(EN_DESCRIBE_PROCESS);
        String EN_DISMISS = "Dismiss";
        QName QN_DISMISS = wps(EN_DISMISS);
        String EN_ESTIMATED_COMPLETION = "EstimatedCompletion";
        QName QN_ESTIMATED_COMPLETION = wps(EN_ESTIMATED_COMPLETION);
        String EN_EXECUTE = "Execute";
        QName QN_EXECUTE = wps(EN_EXECUTE);
        String EN_EXPIRATION_DATE = "ExpirationDate";
        QName QN_EXPIRATION_DATE = wps(EN_EXPIRATION_DATE);
        String EN_EXTENSION = "Extension";
        QName QN_EXTENSION = wps(EN_EXTENSION);
        String EN_FORMAT = "Format";
        QName QN_FORMAT = wps(EN_FORMAT);
        String EN_GENERIC_PROCESS = "GenericProcess";
        QName QN_GENERIC_PROCESS = wps(EN_GENERIC_PROCESS);
        String EN_GET_CAPABILITIES = "GetCapabilities";
        QName QN_GET_CAPABILITIES = wps(EN_GET_CAPABILITIES);
        String EN_GET_RESULT = "GetResult";
        QName QN_GET_RESULT = wps(EN_GET_RESULT);
        String EN_GET_STATUS = "GetStatus";
        QName QN_GET_STATUS = wps(EN_GET_STATUS);
        String EN_INPUT = "Input";
        QName QN_INPUT = wps(EN_INPUT);
        String EN_JOB_ID = "JobID";
        QName QN_JOB_ID = wps(EN_JOB_ID);
        String EN_LITERAL_DATA = "LiteralData";
        QName QN_LITERAL_DATA = wps(EN_LITERAL_DATA);
        String EN_LITERAL_DATA_DOMAIN = "LiteralDataDomain";
        QName QN_LITERAL_DATA_DOMAIN = wps(EN_LITERAL_DATA_DOMAIN);
        String EN_LITERAL_VALUE = "LiteralValue";
        QName QN_LITERAL_VALUE = wps(EN_LITERAL_VALUE);
        String EN_NEXT_POLL = "NextPoll";
        QName QN_NEXT_POLL = wps(EN_NEXT_POLL);
        String EN_OUTPUT = "Output";
        QName QN_OUTPUT = wps(EN_OUTPUT);
        String EN_PERCENT_COMPLETED = "PercentCompleted";
        QName QN_PERCENT_COMPLETED = wps(EN_PERCENT_COMPLETED);
        String EN_PROCESS = "Process";
        QName QN_PROCESS = wps(EN_PROCESS);
        String EN_PROCESS_OFFERING = "ProcessOffering";
        QName QN_PROCESS_OFFERING = wps(EN_PROCESS_OFFERING);
        String EN_PROCESS_OFFERINGS = "ProcessOfferings";
        QName QN_PROCESS_OFFERINGS = wps(EN_PROCESS_OFFERINGS);
        String EN_PROCESS_SUMMARY = "ProcessSummary";
        QName QN_PROCESS_SUMMARY = wps(EN_PROCESS_SUMMARY);
        String EN_REFERENCE = "Reference";
        QName QN_REFERENCE = wps(EN_REFERENCE);
        String EN_RESULT = "Result";
        QName QN_RESULT = wps(EN_RESULT);
        String EN_STATUS = "Status";
        QName QN_STATUS = wps(EN_STATUS);
        String EN_STATUS_INFO = "StatusInfo";
        QName QN_STATUS_INFO = wps(EN_STATUS_INFO);
        String EN_SUPPORTED_CRS = "SupportedCRS";
        QName QN_SUPPORTED_CRS = wps(EN_SUPPORTED_CRS);
    }

    static QName wps(String element) {
        return new QName(NS_WPS, element, NS_WPS_PREFIX);
    }

}
