package org.n52.javaps.coding.stream.xml.impl;

import javax.xml.namespace.QName;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface XMLConstants {

    public interface QNames {

        QName WPS_PROCESS_OFFERING = wps(Entities.WPS_PROCESS_OFFERING);
        QName WPS_PROCESS_OFFERINGS = wps(Entities.WPS_PROCESS_OFFERINGS);
        QName OWS_EXCEPTION = ows(Entities.OWS_EXCEPTION);
        QName OWS_EXCEPTION_TEXT = ows(Entities.OWS_EXCEPTION_TEXT);
        QName OWS_EXCEPTION_REPORT = ows(Entities.OWS_EXCEPTION_REPORT);

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
        static final String WPS_PROCESS_MODEL = "processModel";
        static final String WPS_PROCESS_VERSION = "processVersion";
        static final String WPS_JOB_CONTROL_OPTIONS = "jobControlOptions";
        static final String WPS_OUTPUT_TRANSMISSION = "outputTransmission";
        String OWS_EXCEPTION_CODE = "exceptionCode";
        String OWS_LOCATOR = "locator";
    }

    public interface Entities {
        String WPS_PROCESS_OFFERING = "ProcessOffering";
        String WPS_PROCESS_OFFERINGS = "ProcessOfferings";
        String OWS_EXCEPTION = "Exception";
        String OWS_EXCEPTION_REPORT = "ExceptionReport";
        String OWS_EXCEPTION_TEXT = "ExceptionText";
        String OWS_VERSION = "version";
    }
}
