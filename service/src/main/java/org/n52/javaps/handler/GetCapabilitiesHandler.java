package org.n52.javaps.handler;

import org.n52.iceland.ds.OperationHandler;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.response.GetCapabilitiesResponse;

/**
 *
 * @author Christian Autermann
 */
public interface GetCapabilitiesHandler extends OperationHandler {

    GetCapabilitiesResponse getCapabilities(GetCapabilitiesRequest request)
            throws OwsExceptionReport;
}
