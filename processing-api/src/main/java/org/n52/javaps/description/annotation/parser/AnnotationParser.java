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
package org.n52.javaps.description.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

import org.n52.javaps.description.annotation.binding.AnnotationBinding;

/**
 *
 * @author tkunicki
 * @param <A>
 * @param <M>
 * @param <B>
 */
public interface AnnotationParser<A extends Annotation, M extends AccessibleObject & Member, B extends AnnotationBinding<M>> {

    default B parse(M member) {
        A annotation = member.getAnnotation(getSupportedAnnotation());
        return annotation == null ? null : parse(annotation, member);
    }

    B parse(A annotation, M member);

    Class<? extends A> getSupportedAnnotation();
}
