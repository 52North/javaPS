package org.n52.javaps.description;

import java.util.Collection;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;

/**
 *
 * @author Christian Autermann
 */
public interface ProcessOutputDescriptionContainer {

    ProcessOutputDescription getOutput(OwsCodeType id);

    default ProcessOutputDescription getOutput(String id) {
        return getOutput(new OwsCodeType(id));
    }

    Collection<? extends ProcessOutputDescription> getOutputDescriptions();

    Set<OwsCodeType> getOutputs();

}
