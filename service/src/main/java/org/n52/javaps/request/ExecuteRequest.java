package org.n52.javaps.request;


import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.javaps.ogc.wps.WPSConstants;
import org.n52.javaps.response.ExecuteResponse;

/**
 * @author Christian Autermann
 */
public class ExecuteRequest extends AbstractServiceRequest<ExecuteResponse> {

    @Override
    public ExecuteResponse getResponse()
            throws OwsExceptionReport {
        return (ExecuteResponse) new ExecuteResponse().set(this);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Execute.name();
    }

}
