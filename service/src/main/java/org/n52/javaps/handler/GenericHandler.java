package org.n52.javaps.handler;

import org.n52.iceland.ds.OperationHandler;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;

public interface GenericHandler<
            Q extends AbstractServiceRequest<A>,
            A extends AbstractServiceResponse>
        extends OperationHandler {
    A handler(Q request);
}
