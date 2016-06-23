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

import org.n52.javaps.io.data.IData;
import org.n52.javaps.ogc.wps.description.DataDescription;

/**
 * TODO JavaDoc
 *
 * @author Tom Kunicki, Christian Autermann
 * @param <A>
 * @param <M>
 * @param <D>
 * @param <B>
 */
abstract class AbstractDataAnnotationParser<A extends Annotation, M extends AccessibleObject & Member, D extends DataDescription, B extends AbstractDataBinding<M, D>>
        implements AnnotationParser<A, M, B> {
    private final Function<M, B> bindingFunction;

    AbstractDataAnnotationParser(Function<M, B> bindingFunction) {
        this.bindingFunction = bindingFunction;
    }

    @Override
    public B parse(A annotation, M member) {
        B binding = this.bindingFunction.apply(member);
        binding.setDescription(createDescription(annotation, binding));
        return binding.validate() ? binding : null;
    }

    protected abstract D createDescription(A annotation, B binding);

    protected abstract Class<? extends IData> getBindingType(A annotation, B binding);
}
