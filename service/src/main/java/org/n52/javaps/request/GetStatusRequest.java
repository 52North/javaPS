package org.n52.javaps.request;


import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.GetStatusResponse;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class GetStatusRequest extends AbstractServiceRequest<GetStatusResponse>{

    @Override
    public GetStatusResponse getResponse() throws OwsExceptionReport {
        return (GetStatusResponse) new GetStatusResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetStatus.toString();
    }

}
