package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class InputMethodBinding<D extends ProcessInputDescription> extends InputBinding<Method, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputMethodBinding.class);

    public InputMethodBinding(Method method) {
        super(method);
    }

    @Override
    public Type getMemberType() {
        Type[] genericParameterTypes = getMember().getGenericParameterTypes();
        return (genericParameterTypes.length == 0) ? Void.class : genericParameterTypes[0];
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Field {} with input annotation can't be used, not public.", getMember());
            return false;
        }
        if (getDescription().getOccurence().isMultiple() || !isMemberTypeList()) {
            LOGGER.error("Field {} with input annotation can't be used, maxOccurs > 1 and field is not of type List", getMember());
            return false;
        }
        if (!checkType()) {
            LOGGER.error("Field {} with input annotation can't be used, unable to safely assign field using binding payload type", getMember());
            return false;
        }
        return true;
    }

    @Override
    public void set(Object annotatedObject, List<IData> boundInputList) {
        try {
            getMember().invoke(annotatedObject, unbindInput(boundInputList));
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException("Internal error processing inputs", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

}
