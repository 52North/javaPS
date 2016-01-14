package org.n52.javaps.operator;

import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;

public interface ParameterValidator<T extends AbstractServiceRequest<?>> {
    void validate(T request) throws OwsExceptionReport;
}
