package org.n52.javaps.coding.stream.xml.impl;

import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.algorithm.ProcessDescription2;
import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Attributes;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Namespaces;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.QNames;
import org.n52.javaps.ogc.wps.ProcessOffering;
import org.n52.javaps.response.DescribeProcessResponse;

import static java.util.stream.Collectors.joining;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class DescribeProcessResponseWriter extends AbstractXmlStreamWriter<DescribeProcessResponse> {

    private static final XmlStreamWriterKey KEY
            = new XmlStreamWriterKey(DescribeProcessResponse.class);

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected void write(DescribeProcessResponse object) throws XMLStreamException {
        start(QNames.WPS_PROCESS_OFFERINGS);
        namespace(Namespaces.WPS_20, Namespaces.WPS_PREFIX);
        namespace(Namespaces.OWS_20, Namespaces.OWS_PREFIX);

        for (ProcessOffering description : object.getProcessDescriptions()) {
            writeProcessOffering(description);
        }

        end(QNames.WPS_PROCESS_OFFERINGS);
    }

    private void writeProcessOffering(ProcessOffering offering)
            throws XMLStreamException {
        start(QNames.WPS_PROCESS_OFFERING);

        if (!offering.getJobControlOptions().isEmpty()) {
            attr(Attributes.WPS_JOB_CONTROL_OPTIONS, offering.getJobControlOptions()
                    .stream().map(x -> x.getValue()).collect(joining(" ")));
        }
        if (!offering.getOutputTransmissionModes().isEmpty()) {
            attr(Attributes.WPS_OUTPUT_TRANSMISSION, offering.getOutputTransmissionModes()
                    .stream().map(x -> x.getValue()).collect(joining(" ")));
        }

        if (offering.getProcessVersion().isPresent()) {
            attr(Attributes.WPS_PROCESS_VERSION, offering.getProcessVersion().get());
        }

        if (offering.getProcessModel().isPresent()) {
            attr(Attributes.WPS_PROCESS_MODEL, offering.getProcessModel().get());
        }

        writeProcess(offering.getProcessDescription());
        end(QNames.WPS_PROCESS_OFFERING);
    }

    private void writeProcess(ProcessDescription2 description) {
        /* TODO implement org.n52.javaps.coding.stream.xml.impl.DescribeProcessResponseWriter.write() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.stream.xml.impl.DescribeProcessResponseWriter.write() not yet implemented");
    }

}
