package org.n52.iceland.request.operator.ows2;

import org.n52.iceland.ds.GenericOperationHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.ds.OperationHandlerKey;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OperationHandlerRepository
        extends AbstractComponentRepository<OperationHandlerKey, GenericOperationHandler<?,?>, OperationHandlerFactory> implements Constructable {

    private final Map<OperationHandlerKey, Producer<GenericOperationHandler<?,?>>> operationHandlers = new HashMap<>();
    private Collection<GenericOperationHandler<?,?>> components;
    private Collection<OperationHandlerFactory> componentFactories;

    @Autowired(required = false)
    public void setComponents(Collection<GenericOperationHandler<?,?>> components) {
        this.components = components;
    }

    @Autowired(required = false)
    public void setComponentFactories(Collection<OperationHandlerFactory> componentFactories) {
        this.componentFactories = componentFactories;
    }

    @Override
    public void init() {
        this.operationHandlers.clear();
        this.operationHandlers.putAll(getUniqueProviders(this.components, this.componentFactories));
    }

    public Map<OperationHandlerKey, GenericOperationHandler<?,?>> getOperationHandlers() {
        return Producers.produce(this.operationHandlers);
    }

    public <Q extends AbstractServiceRequest<A>, A extends AbstractServiceResponse> GenericOperationHandler<Q,A> getOperationHandler(String service, String operationName) {
        return getOperationHandler(new OperationHandlerKey(service, operationName));
    }

    @SuppressWarnings("unchecked")
    public <Q extends AbstractServiceRequest<A>, A extends AbstractServiceResponse> GenericOperationHandler<Q,A> getOperationHandler(OperationHandlerKey key) {
        return (GenericOperationHandler<Q,A>)Producers.produce(operationHandlers.get(key));
    }
}
