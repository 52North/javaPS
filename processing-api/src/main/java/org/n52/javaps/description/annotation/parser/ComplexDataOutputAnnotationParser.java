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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.util.function.Function;

import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.annotation.ComplexOutput;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.impl.ComplexOutputDescriptionImpl;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
public class ComplexDataOutputAnnotationParser<M extends AccessibleObject & Member, B extends OutputBinding<M, ComplexOutputDescription>>
        extends OutputAnnotationParser<ComplexOutput, M, ComplexOutputDescription, B> {

    public ComplexDataOutputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @Override
    protected ComplexOutputDescription createDescription(ComplexOutput annotation, B binding) {
        return ComplexOutputDescriptionImpl.builder()
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .build();
    }

    @Override
    public Class<? extends ComplexOutput> getSupportedAnnotation() {
        return ComplexOutput.class;
    }

    @Override
    protected Class<? extends IData> getBindingType(ComplexOutput annotation, B binding) {
        return annotation.binding();
    }
}
