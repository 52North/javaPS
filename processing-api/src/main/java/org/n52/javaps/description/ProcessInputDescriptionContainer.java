package org.n52.javaps.description;

import java.util.Collection;
import java.util.Set;

import org.n52.iceland.ogc.ows.OwsCodeType;

/**
 *
 * @author Christian Autermann
 */
public interface ProcessInputDescriptionContainer {

    ProcessInputDescription getInput(OwsCodeType id);

    default ProcessInputDescription getInput(String id) {
        return getInput(new OwsCodeType(id));
    }

    Collection<? extends ProcessInputDescription> getInputDescriptions();

    Set<OwsCodeType> getInputs();

}
