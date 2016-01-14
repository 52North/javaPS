package org.n52.javaps.request;


import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.GetResultResponse;

/**
 * @author Christian Autermann
 */
public class GetResultRequest extends AbstractServiceRequest<GetResultResponse> {

    @Override
    public GetResultResponse getResponse()
            throws OwsExceptionReport {
        return (GetResultResponse) new GetResultResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetResult.toString();
    }

}
