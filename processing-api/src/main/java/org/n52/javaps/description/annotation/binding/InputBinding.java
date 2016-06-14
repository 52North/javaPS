package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 * @param <M>
 * @param <D>
 */
public abstract class InputBinding<M extends AccessibleObject & Member, D extends ProcessInputDescription> extends DataBinding<M, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputBinding.class);

    public InputBinding(M member) {
        super(member);
    }

    @Override
    public Type getType() {
        Type memberType = getMemberType();
        Type inputType = memberType;
        if (memberType instanceof Class<?>) {
            Class<?> memberClass = (Class<?>) memberType;
            if (List.class.isAssignableFrom(memberClass)) {
                // We treat List as List<? extends Object>
                inputType = NOT_PARAMETERIZED_TYPE;
            }
        } else if (memberType instanceof ParameterizedType) {
            ParameterizedType parameterizedMemberType
                    = (ParameterizedType) memberType;
            Class<?> rawClass = (Class<?>) parameterizedMemberType.getRawType();
            if (List.class.isAssignableFrom(rawClass)) {
                inputType = parameterizedMemberType.getActualTypeArguments()[0];
            }
        } else {
            LOGGER.error("Unable to infer concrete type information for " +
                         getMember());
        }
        return inputType;
    }

    public boolean isMemberTypeList() {
        Type memberType = getMemberType();
        if (memberType instanceof Class<?>) {
            return List.class.isAssignableFrom((Class<?>) memberType);
        } else if (memberType instanceof ParameterizedType) {
            Class<?> rawClass
                    = (Class<?>) ((ParameterizedType) memberType).getRawType();
            return List.class.isAssignableFrom(rawClass);
        } else {
            LOGGER.error("Unable to infer concrete type information for " +
                         getMember());
        }
        return false;
    }

    protected boolean checkType() {
        Type inputPayloadType = getPayloadType();
        try {
            Class<?> bindingPayloadClass
                    = getBindingType().getMethod("getPayload", (Class<?>[]) null)
                    .getReturnType();
            if (inputPayloadType instanceof Class<?>) {
                return ((Class<?>) inputPayloadType).isAssignableFrom(bindingPayloadClass);
            } else if (inputPayloadType instanceof ParameterizedType) {
                // i.e.
                // List<FeatureCollection<SimpleFeatureType,SimpleFeature>>
                return ((Class<?>) ((ParameterizedType) inputPayloadType).getRawType()).isAssignableFrom(bindingPayloadClass);
            } else if (inputPayloadType instanceof WildcardType) {
                // i.e. List<? extends String> or List<? super String>
                WildcardType inputTypeWildcardType
                        = (WildcardType) inputPayloadType;
                Type[] lowerBounds = inputTypeWildcardType.getLowerBounds();
                Type[] upperBounds = inputTypeWildcardType.getUpperBounds();
                Class<?> lowerBoundClass = null;
                Class<?> upperBoundClass = null;
                if (lowerBounds != null && lowerBounds.length > 0) {
                    if (lowerBounds[0] instanceof Class<?>) {
                        lowerBoundClass = (Class<?>) lowerBounds[0];
                    } else if (lowerBounds[0] instanceof ParameterizedType) {
                        lowerBoundClass
                                = (Class<?>) ((ParameterizedType) lowerBounds[0]).getRawType();
                    }
                }
                if (upperBounds != null && upperBounds.length > 0) {
                    if (upperBounds[0] instanceof Class<?>) {
                        upperBoundClass = (Class<?>) upperBounds[0];
                    } else if (upperBounds[0] instanceof ParameterizedType) {
                        upperBoundClass
                                = (Class<?>) ((ParameterizedType) upperBounds[0]).getRawType();
                    }
                }
                return (upperBoundClass == null ||
                        upperBoundClass.isAssignableFrom(bindingPayloadClass)) &&
                       (lowerBounds == null ||
                        bindingPayloadClass.isAssignableFrom(lowerBoundClass));
            } else {
                LOGGER.error("Unable to infer assignability from type for " +
                             getMember());
            }
        } catch (NoSuchMethodException e) {
            return false;
        }
        return false;
    }

    public Object unbindInput(List<IData> boundValueList) {
        Object value = null;
        if (boundValueList != null && boundValueList.size() > 0) {
            if (isMemberTypeList()) {
                List valueList = new ArrayList(boundValueList.size());
                for (IData bound : boundValueList) {
                    value = bound.getPayload();
                    if (isTypeEnum()) {
                        value = Enum.valueOf((Class<? extends Enum>) getType(), (String) value);
                    }
                    valueList.add(value);
                }
                value = valueList;
            } else if (boundValueList.size() == 1) {
                value = boundValueList.get(0).getPayload();
                if (isTypeEnum()) {
                    value = Enum.valueOf((Class<? extends Enum>) getType(), (String) value);
                }
            }
        }
        return value;
    }

    public abstract void set(Object annotatedObject, List<IData> boundInputList);

}
