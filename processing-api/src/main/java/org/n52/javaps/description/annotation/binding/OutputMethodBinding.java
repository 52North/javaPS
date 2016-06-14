package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 * @param <D>
 */
public class OutputMethodBinding<D extends ProcessOutputDescription> extends OutputBinding<Method, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputMethodBinding.class);

    public OutputMethodBinding(Method method) {
        super(method);
    }

    @Override
    public Type getMemberType() {
        return getMember().getGenericReturnType();
    }

    @Override
    public boolean validate() {
        Method method = getMember();
        if (method.getParameterTypes().length != 0) {
            LOGGER.error("Method {} with output annotation can't be used, parameter count != 0", getMember());
            return false;
        }
        if (!checkModifier()) {
            LOGGER.error("Method {} with output annotation can't be used, not public", getMember());
            return false;
        }
        if (!checkType()) {
            LOGGER.error("Method {} with output annotation can't be used, unable to safely construct binding using method return type", getMember());
            return false;
        }
        return true;
    }

    @Override
    public IData get(Object annotatedInstance) {
        Object value;
        try {
            value = getMember().invoke(annotatedInstance);
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException("Internal error processing inputs", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
        return value == null ? null : bindOutputValue(value);
    }

}
