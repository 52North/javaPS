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
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;

/**
 *
 * @author tkunicki
 * @param <M> the accessible member type
 */
abstract class AnnotationBinding<M extends AccessibleObject & Member> {

    private final M member;

    // for example, a type reprecenting the <? extends Object> for types of
    // List<? extends Object> or List
    public final Type NOT_PARAMETERIZED_TYPE = new WildcardType() {
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { Object.class };
        }

        @Override
        public Type[] getLowerBounds() {
            return new Type[0];
        }
    };

    AnnotationBinding(M member) {
        this.member = Objects.requireNonNull(member);
    }

    public M getMember() {
        return member;
    }

    protected boolean checkModifier() {
        return Modifier.isPublic(getMember().getModifiers());
    }

    public abstract boolean validate();

}
