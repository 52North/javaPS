/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.wps.description.typed.TypedProcessOutputDescription;
import org.n52.javaps.io.Data;
import org.n52.javaps.io.literal.LiteralData;

abstract class AbstractOutputBinding<M extends AccessibleObject & Member, D extends TypedProcessOutputDescription<?>> extends AbstractDataBinding<M, D> {

    private Function<Object, ? extends Data<?>> bindingConstructor;

    public AbstractOutputBinding(M member) {
        super(member);
    }

    protected boolean checkType() {
        return getConstructor() != null;
    }

    protected Data<?> bindOutputValue(Object outputValue) {
        return getConstructor().apply(outputToPayload(outputValue));
    }

    public abstract Data<?> get(Object annotatedInstance);

    private synchronized Function<Object, ? extends Data<?>> getConstructor() {
        if (bindingConstructor == null) {
            bindingConstructor = findConstructor();
        }
        return bindingConstructor;
    }

    private Function<Object, ? extends Data<?>> findConstructor() {

        if (getDescription().isLiteral()) {
            return LiteralData::new;
        }

        Class<? extends Data<?>> bindingClass = getDescription().getBindingType();
        Class<?> outputPayloadClass = getDescription().getPayloadType();
        Type bindingPayloadType = getPayloadType();
        if (!(bindingPayloadType instanceof Class<?>)) {
            return null;
        }
        Class<?> bindingPayloadClass = (Class<?>) bindingPayloadType;
        if (!bindingPayloadClass.isAssignableFrom(outputPayloadClass)
            || bindingPayloadClass.isInterface()
            || Modifier.isAbstract(bindingClass.getModifiers())) {
            return null;
        }
        try {
            Constructor<? extends Data<?>> constructor = bindingClass.getConstructor(bindingPayloadClass);

            if (constructor == null || !Modifier.isPublic(constructor.getModifiers())) {
                return null;
            }

            return arg -> {
                try {
                    return (Data<?>) constructor.newInstance(arg);
                } catch (InstantiationException | SecurityException | IllegalAccessException ex) {
                    throw new RuntimeException("Internal error processing outputs", ex);
                } catch (InvocationTargetException ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    throw new RuntimeException(cause.getMessage(), cause);
                }
            };
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static <D extends TypedProcessOutputDescription<?>> AbstractOutputBinding<Field, D> field(Field field) {
        return new OutputFieldBinding<>(field);
    }

    public static <D extends TypedProcessOutputDescription<?>> AbstractOutputBinding<Method, D> method(Method method) {
        return new OutputMethodBinding<>(method);
    }

    private static class OutputMethodBinding<D extends TypedProcessOutputDescription<?>> extends AbstractOutputBinding<Method, D> {
        private static final Logger LOGGER = LoggerFactory.getLogger(OutputMethodBinding.class);

        OutputMethodBinding(Method method) {
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
        public Data<?> get(Object instance) {
            Object value;
            try {
                value = getMember().invoke(instance);
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                throw new RuntimeException("Internal error processing inputs", ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                throw new RuntimeException(cause.getMessage(), cause);
            }
            return value == null ? null : bindOutputValue(value);
        }

    }

    private static class OutputFieldBinding<D extends TypedProcessOutputDescription<?>> extends AbstractOutputBinding<Field, D> {

        private static final Logger LOGGER = LoggerFactory.getLogger(OutputFieldBinding.class);

        OutputFieldBinding(Field field) {
            super(field);
        }

        @Override
        public Type getMemberType() {
            return getMember().getGenericType();
        }

        @Override
        public boolean validate() {
            if (!checkModifier()) {
                LOGGER.error("Field {} with output annotation can't be used, not public.", getMember());
                return false;
            }
            if (!checkType()) {
                LOGGER.error("Field {} with output annotation can't be used, unable to safely construct binding using field type", getMember());
                return false;
            }
            return true;
        }

        @Override
        public Data<?> get(Object instance) {
            Object value;
            try {
                value = getMember().get(instance);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException("Internal error processing inputs", ex);
            }
            return value == null ? null : bindOutputValue(value);
        }

    }

}
