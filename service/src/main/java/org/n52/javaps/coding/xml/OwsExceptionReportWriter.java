package org.n52.javaps.coding.xml;



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.ows.OwsExceptionCode;
import org.n52.iceland.exception.ows.OwsExceptionReport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsExceptionReportWriter extends AbstractXmlStreamWriter<OwsExceptionReport> {
    private static final String NS_OWS_20 = "http://www.opengis.net/ows/2.0";
    private static final String EN_EXCEPTION_REPORT = "ExceptionReport";
    private static final String NS_OWS_PREFIX = "ows";
    private static final QName QN_EXCEPTION_REPORT
            = new QName(NS_OWS_20, EN_EXCEPTION_REPORT, NS_OWS_PREFIX);
    private static final String EN_VERSION = "version";
    private static final String EN_EXCEPTION = "Exception";
    private static final QName QN_EXCEPTION
            = new QName(NS_OWS_20, EN_EXCEPTION, NS_OWS_PREFIX);
    private static final String AN_LOCATOR = "locator";
    private static final String AN_EXCEPTION_CODE = "exceptionCode";
    private static final QName QN_EXCEPTION_TEXT
            = new QName(NS_OWS_20, "ExceptionText", NS_OWS_PREFIX);
    //TODO setting
    private boolean includeStackTraceInExceptionReport = true;

    @Override
    protected void write(OwsExceptionReport report) throws XMLStreamException {
        start(QN_EXCEPTION_REPORT);
        attr(EN_VERSION, report.getVersion());
        for (CodedException exception : report.getExceptions()) {
            writeCodedException(exception);
        }
        end(QN_EXCEPTION_REPORT);
    }

    private void writeCodedException(CodedException exception) throws XMLStreamException {
        start(QN_EXCEPTION);
        if (exception.getLocator() != null) {
            attr(AN_LOCATOR, exception.getLocator());
        }
        if (exception.getCode() != null) {
            attr(AN_EXCEPTION_CODE, exception.getCode().toString());
        } else {
            attr(AN_EXCEPTION_CODE, OwsExceptionCode.NoApplicableCode.toString());
        }
        if (exception.getMessage() != null) {
            start(QN_EXCEPTION_TEXT);
            chars(exception.getMessage());
            end(QN_EXCEPTION_TEXT);
        }
        if (includeStackTraceInExceptionReport) {
            start(QN_EXCEPTION_TEXT);
            chars("EXEPTION]: \n");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exception.getCause().printStackTrace(new PrintStream(os));
            chars(os.toString());
            end(QN_EXCEPTION_TEXT);
        }
        end(QN_EXCEPTION);
    }

}
