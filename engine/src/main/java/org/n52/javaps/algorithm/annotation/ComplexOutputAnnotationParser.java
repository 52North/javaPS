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
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.n52.iceland.ogc.wps.Format;
import org.n52.javaps.description.TypedComplexOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.complex.ComplexData;
import org.n52.javaps.io.OutputHandlerRepository;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M> the accessible member type
 * @param <B> the binding type
 */
class ComplexOutputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractOutputBinding<M, TypedComplexOutputDescription>>
        extends AbstractOutputAnnotationParser<ComplexOutput, M, TypedComplexOutputDescription, B> {

    private final OutputHandlerRepository generatorRepository;

    ComplexOutputAnnotationParser(Function<M, B> bindingFunction, OutputHandlerRepository generatorRepository) {
        super(bindingFunction);
        this.generatorRepository = Objects.requireNonNull(generatorRepository);
    }

    @Override
    protected TypedComplexOutputDescription createDescription(ComplexOutput annotation, B binding) {
        Class<? extends ComplexData<?>> bindingClass = annotation.binding();
        Set<Format> supportedFormats = this.generatorRepository.getSupportedFormats(bindingClass);
        Format defaultFormat = this.generatorRepository.getDefaultFormat(bindingClass).orElse(null);
        return new TypedProcessDescriptionFactory().complexOutput()
                .withType(bindingClass)
                .withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats)
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .build();
    }

    @Override
    public Class<? extends ComplexOutput> getSupportedAnnotation() {
        return ComplexOutput.class;
    }
}
