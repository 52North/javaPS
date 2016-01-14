package org.n52.javaps.handler;

import org.n52.iceland.ds.OperationHandler;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.javaps.request.DemoRequest;
import org.n52.javaps.response.DemoResponse;

/**
 * Handlers are a second level layer under the operations and allow to combine business logic across several operators
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
*/
public interface DemoHandler extends OperationHandler {

    DemoResponse demo(DemoRequest request) throws OwsExceptionReport;
}
