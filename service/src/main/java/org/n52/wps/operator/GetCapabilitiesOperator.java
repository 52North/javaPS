package org.n52.wps.operator;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.ds.OperationHandlerRepository;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OwsOperation;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.GetCapabilitiesRequest;
import org.n52.iceland.request.operator.RequestOperator;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.wps.SkeletonConstants;
import org.n52.wps.handler.GetCapabilitiesHandler;

/**
 * @author Christian Autermann
 */
public class GetCapabilitiesOperator implements RequestOperator {

    private static final RequestOperatorKey KEY
            = new RequestOperatorKey(SkeletonConstants.SERVICE,
                                     SkeletonConstants.VERSION,
                                     OWSConstants.Operations.GetCapabilities);

    private OperationHandlerRepository operationHandlerRepository;

    @Inject
    public void setOperationHandlerRepository(OperationHandlerRepository repo) {
        this.operationHandlerRepository = repo;
    }

    @Override
    public AbstractServiceResponse receiveRequest(AbstractServiceRequest<?> request) throws OwsExceptionReport {
        return getHandler(request).getCapabilities((GetCapabilitiesRequest) request);
    }

    private GetCapabilitiesHandler getHandler(AbstractServiceRequest<?> request) {
        String service = request.getService();
        String operationName = request.getOperationName();
        return getHandler(service, operationName);
    }

    private GetCapabilitiesHandler getHandler(String service, String operationName) {
        return (GetCapabilitiesHandler) operationHandlerRepository.getOperationHandler(service, operationName);
    }

    @Override
    public OwsOperation getOperationMetadata(String service, String version) throws OwsExceptionReport {
        return getHandler(service, KEY.getOperationName()).getOperationsMetadata(service, version);
    }

    @Override
    public Set<RequestOperatorKey> getKeys() {
        return Collections.singleton(KEY);
    }

}
