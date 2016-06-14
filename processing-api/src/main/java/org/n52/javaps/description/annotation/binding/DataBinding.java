package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Objects;

import org.n52.javaps.algorithm.util.ClassUtil;
import org.n52.javaps.description.DataDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public abstract class DataBinding<M extends AccessibleObject & Member, D extends DataDescription> extends AnnotationBinding<M> {

    private D description;
    private Class<? extends IData> bindingType;

    public DataBinding(M member) {
        super(member);
    }

    public abstract Type getMemberType();

    public Type getType() {
        return getMemberType();
    }

    public Type getPayloadType() {
        Type type = getType();
        if (isTypeEnum()) {
            return String.class;
        }
        if (type instanceof Class<?>) {
            Class<?> inputClass = (Class<?>) type;
            if (inputClass.isPrimitive()) {
                return ClassUtil.wrap(inputClass);
            }
        }
        return type;
    }

    public boolean isTypeEnum() {
        Type inputType = getType();
        return (inputType instanceof Class<?>) &&
               ((Class<?>) inputType).isEnum();
    }

    public Class<? extends IData> getBindingType() {
        return bindingType;
    }

    public void setBindingType(Class<? extends IData> bindingType) {
        this.bindingType = Objects.requireNonNull(bindingType);
    }

    public void setDescription(D description) {
        this.description = Objects.requireNonNull(description);
    }

    public D getDescription() {
        return description;
    }

}
