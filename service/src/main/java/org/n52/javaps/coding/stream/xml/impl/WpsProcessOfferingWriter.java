package org.n52.javaps.coding.stream.xml.impl;

import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.xml.AbstractXmlElementStreamWriter;
import org.n52.javaps.coding.stream.xml.XmlStreamWriterKey;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Attributes;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.Namespaces;
import org.n52.javaps.coding.stream.xml.impl.XMLConstants.QNames;
import org.n52.javaps.ogc.wps.ProcessOffering;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class WpsProcessOfferingWriter extends AbstractXmlElementStreamWriter<ProcessOffering> {
    private static final XmlStreamWriterKey KEY
            = new XmlStreamWriterKey(ProcessOffering.class);

    @Override
    protected void write(ProcessOffering offering)
            throws XMLStreamException {
        start(QNames.WPS_PROCESS_OFFERING);
        namespace(Namespaces.WPS_PREFIX, Namespaces.WPS_20);
        namespace(Namespaces.OWS_PREFIX, Namespaces.OWS_20);

        if (!offering.getJobControlOptions().isEmpty()) {
            attr(Attributes.WPS_JOB_CONTROL_OPTIONS, offering
                 .getJobControlOptions()
                 .stream().map(x -> x.getValue()).collect(joining(" ")));
        }
        if (!offering.getOutputTransmissionModes().isEmpty()) {
            attr(Attributes.WPS_OUTPUT_TRANSMISSION, offering
                 .getOutputTransmissionModes()
                 .stream().map(x -> x.getValue()).collect(joining(" ")));
        }

        if (offering.getProcessVersion().isPresent()) {
            attr(Attributes.WPS_PROCESS_VERSION, offering.getProcessVersion()
                 .get());
        }

        if (offering.getProcessModel().isPresent()) {
            attr(Attributes.WPS_PROCESS_MODEL, offering.getProcessModel().get());
        }

        delegate(offering.getProcessDescription());

        end(QNames.WPS_PROCESS_OFFERING);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.singleton(KEY);
    }
}
