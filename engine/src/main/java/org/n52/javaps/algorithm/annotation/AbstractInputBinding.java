/*
 * Copyright 2016-2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.javaps.algorithm.annotation;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.TypedProcessInputDescription;
import org.n52.javaps.io.Data;

/**
 * TODO JavaDoc
 *
 * @author Tom Kunicki, Christian Autermann
 * @param <M>
 *            the accessible member type
 */
abstract class AbstractInputBinding<M extends AccessibleObject & Member> extends AbstractDataBinding<M,
        TypedProcessInputDescription<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInputBinding.class);
    private static final String INTERNAL_ERROR_PROCESSING_INPUTS = "Internal error processing inputs";
    private final String unableToInferConcreteType = "Unable to infer concrete type information for {}";

    AbstractInputBinding(M member) {
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
                inputType = getNotParameterizedType();
            }
        } else if (memberType instanceof ParameterizedType) {
            ParameterizedType parameterizedMemberType = (ParameterizedType) memberType;
            Class<?> rawClass = (Class<?>) parameterizedMemberType.getRawType();
            if (List.class.isAssignableFrom(rawClass)) {
                inputType = parameterizedMemberType.getActualTypeArguments()[0];
            }
        } else {
            LOGGER.error(unableToInferConcreteType , getMember());
        }
        return inputType;
    }

    public boolean isMemberTypeList() {
        Type memberType = getMemberType();
        if (memberType instanceof Class<?>) {
            return List.class.isAssignableFrom((Class<?>) memberType);
        } else if (memberType instanceof ParameterizedType) {
            Class<?> rawClass = (Class<?>) ((ParameterizedType) memberType).getRawType();
            return List.class.isAssignableFrom(rawClass);
        } else {
            LOGGER.error(unableToInferConcreteType, getMember());
        }
        return false;
    }

    protected boolean checkType() {
        Type inputPayloadType = getPayloadType();
        Class<?> bindingPayloadClass = getDescription().getPayloadType();

        if (inputPayloadType instanceof Class<?>) {
            return ((Class<?>) inputPayloadType).isAssignableFrom(bindingPayloadClass);
        } else if (inputPayloadType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) inputPayloadType;
            // i.e.
            // List<FeatureCollection<SimpleFeatureType,SimpleFeature>>
            return ((Class<?>) parameterizedType.getRawType()).isAssignableFrom(bindingPayloadClass);
        } else if (inputPayloadType instanceof WildcardType) {
            // i.e. List<? extends String> or List<? super String>
            WildcardType inputTypeWildcardType = (WildcardType) inputPayloadType;
            Type[] lowerBounds = inputTypeWildcardType.getLowerBounds();
            Type[] upperBounds = inputTypeWildcardType.getUpperBounds();
            Class<?> lowerBoundClass = null;
            Class<?> upperBoundClass = null;
            if (lowerBounds != null && lowerBounds.length > 0) {
                if (lowerBounds[0] instanceof Class<?>) {
                    lowerBoundClass = (Class<?>) lowerBounds[0];
                } else if (lowerBounds[0] instanceof ParameterizedType) {
                    lowerBoundClass = (Class<?>) ((ParameterizedType) lowerBounds[0]).getRawType();
                }
            }
            if (upperBounds != null && upperBounds.length > 0) {
                if (upperBounds[0] instanceof Class<?>) {
                    upperBoundClass = (Class<?>) upperBounds[0];
                } else if (upperBounds[0] instanceof ParameterizedType) {
                    upperBoundClass = (Class<?>) ((ParameterizedType) upperBounds[0]).getRawType();
                }
            }
            return (upperBoundClass == null || upperBoundClass.isAssignableFrom(bindingPayloadClass))
                    && (lowerBounds == null || bindingPayloadClass.isAssignableFrom(lowerBoundClass));
        } else {
            LOGGER.error("Unable to infer assignability from type for {}", getMember());
        }

        return false;
    }

    public Object unbindInput(List<Data<?>> inputs) {
        if (inputs != null && inputs.size() > 0) {
            if (isMemberTypeList()) {
                return inputs.stream().map(bound -> payloadToInput(bound.getPayload())).collect(toList());
            } else if (inputs.size() == 1) {
                return payloadToInput(inputs.get(0).getPayload());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Field {} with input annotation can't be used, not public.", getMember());
            return false;
        }
        if (getDescription().getOccurence().isMultiple() && !isMemberTypeList()) {
            LOGGER.error("Field {} with input annotation can't be used, occurence is {} and field is not of type List",
                    getMember(), getDescription().getOccurence());
            return false;
        }
        if (!checkType()) {
            LOGGER.error(
                    "Field {} with input annotation can't be used, "
                    + "unable to safely assign field using binding payload type",
                    getMember());
            return false;
        }
        return true;
    }

    public abstract void set(Object annotatedObject,
            List<Data<?>> boundInputList);

    public static AbstractInputBinding<Field> field(Field field) {
        return new InputFieldBinding(field);
    }

    public static AbstractInputBinding<Method> method(Method method) {
        return new InputMethodBinding(method);
    }

    private static class InputFieldBinding extends AbstractInputBinding<Field> {


        InputFieldBinding(Field field) {
            super(field);
        }

        @Override
        public Type getMemberType() {
            return getMember().getGenericType();
        }

        @Override
        public void set(Object instance,
                List<Data<?>> inputs) {
            try {
                getMember().set(instance, unbindInput(inputs));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(INTERNAL_ERROR_PROCESSING_INPUTS , ex);
            }
        }

    }

    private static class InputMethodBinding extends AbstractInputBinding<Method> {

        InputMethodBinding(Method method) {
            super(method);
        }

        @Override
        public Type getMemberType() {
            Type[] genericParameterTypes = getMember().getGenericParameterTypes();
            return (genericParameterTypes.length == 0) ? Void.class : genericParameterTypes[0];
        }

        @Override
        public void set(Object instance,
                List<Data<?>> inputs) {
            try {
                if (inputs != null) {
                    getMember().invoke(instance, unbindInput(inputs));
                }
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                throw new RuntimeException(INTERNAL_ERROR_PROCESSING_INPUTS, ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                throw new RuntimeException(cause.getMessage(), cause);
            }
        }

    }

}
