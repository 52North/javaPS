package org.n52.javaps.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.javaps.ogc.wps.WPSConstants;

/**
 * @author Christian Autermann
 */
public class ExecuteResponse extends AbstractServiceResponse {

    private static final Logger log = LoggerFactory.getLogger(ExecuteResponse.class);

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Execute.toString();
    }

}
