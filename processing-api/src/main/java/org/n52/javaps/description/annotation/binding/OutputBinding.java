package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public abstract class OutputBinding<M extends AccessibleObject & Member, D extends ProcessOutputDescription> extends DataBinding<M, D> {

    private Constructor<? extends IData> bindingConstructor;

    public OutputBinding(M member) {
        super(member);
    }

    protected boolean checkType() {
        return getConstructor() != null;
    }

    public IData bindOutputValue(Object outputValue) {
        try {
            if (isTypeEnum()) {
                outputValue = ((Enum<?>) outputValue).name();
            }
            return getConstructor().newInstance(outputValue);
        } catch (InstantiationException | SecurityException | IllegalAccessException ex) {
            throw new RuntimeException("Internal error processing outputs", ex);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            throw new RuntimeException(cause.getMessage(), cause);
        }
    }

    public abstract IData get(Object annotatedInstance);

    private synchronized Constructor<? extends IData> getConstructor() {
        if (bindingConstructor == null) {
            try {
                Class<? extends IData> bindingClass = getBindingType();
                Class<?> outputPayloadClass = bindingClass.getMethod("getPayload", (Class<?>[]) null).getReturnType();
                Type bindingPayloadType = getPayloadType();
                if (bindingPayloadType instanceof Class<?>) {
                    Class<?> bindingPayloadClass = (Class<?>) bindingPayloadType;
                    if (bindingPayloadClass.isAssignableFrom(outputPayloadClass)) {
                        bindingConstructor = bindingClass.getConstructor(bindingPayloadClass);
                    }
                }
            } catch (NoSuchMethodException e) {
                // error handling on fall-through
            }
        }
        return bindingConstructor;
    }

}
