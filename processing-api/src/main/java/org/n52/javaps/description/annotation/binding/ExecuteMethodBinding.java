package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ExecuteMethodBinding extends AnnotationBinding<Method> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteMethodBinding.class);

    public ExecuteMethodBinding(Method method) {
        super(method);
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Method {} with Execute annotation can't be used, not public.", getMember());
            return false;
        }
        // eh, do we really need to care about this?
        if (!getMember().getReturnType().equals(void.class)) {
            LOGGER.error("Method {} with Execute annotation can't be used, return type not void", getMember());
            return false;
        }
        if (getMember().getParameterTypes().length != 0) {
            LOGGER.error("Method {} with Execute annotation can't be used, method parameter count is > 0.", getMember());
            return false;
        }
        return true;
    }

    public void execute(Object annotatedInstance) {
        try {
            getMember().invoke(annotatedInstance);
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException("Internal error executing process", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

}
