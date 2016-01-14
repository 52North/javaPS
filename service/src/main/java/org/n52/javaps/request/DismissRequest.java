package org.n52.javaps.request;


import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.DismissResponse;

/**
 * @author Christian Autermann
 */
public class DismissRequest extends AbstractServiceRequest<DismissResponse> {

    @Override
    public DismissResponse getResponse()
            throws OwsExceptionReport {
        return (DismissResponse) new DismissResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Dismiss.name();
    }

}
