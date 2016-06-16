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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.function.Function;

import org.n52.javaps.description.ProcessOutputDescription;

abstract class OutputAnnotationParser<A extends Annotation, M extends AccessibleObject & Member, D extends ProcessOutputDescription, B extends OutputBinding<M, D>>
        extends DataAnnotationParser<A, M, D, B> {

    OutputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }
}
