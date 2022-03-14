/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Objects;

import org.n52.javaps.description.TypedDataDescription;

import com.google.common.primitives.Primitives;

/**
 * TODO JavaDoc
 *
 * @author Tom Kunicki, Christian Autermann
 */
abstract class AbstractDataBinding<M extends AccessibleObject & Member, D extends TypedDataDescription<?>> extends
        AnnotationBinding<M> {

    private D description;

    AbstractDataBinding(M member) {
        super(member);
    }

    public abstract Type getMemberType();

    public Type getType() {
        return getMemberType();
    }

    public Type getPayloadType() {
        Type type = getType();
        if (isEnum(type)) {
            return String.class;
        }
        if (type instanceof Class<?>) {
            return Primitives.wrap((Class<?>) type);
        }
        return type;
    }

    protected Object outputToPayload(Object outputValue) {
        Type type = getType();
        if (isEnum(type)) {
            return ((Enum<?>) outputValue).name();
        } else {
            return outputValue;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object payloadToInput(Object payload) {
        Type type = getType();
        if (isEnum(type)) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) type;
            return Enum.valueOf(enumClass, (String) payload);
        }
        return payload;
    }

    public boolean isEnum() {
        return isEnum(getType());
    }

    public static boolean isEnum(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isEnum();
    }

    public void setDescription(D description) {
        this.description = Objects.requireNonNull(description);
    }

    public D getDescription() {
        return description;
    }

}
