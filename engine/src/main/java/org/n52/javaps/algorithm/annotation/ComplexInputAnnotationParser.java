/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.wps.Format;
import org.n52.javaps.description.TypedComplexInputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.InputHandlerRepository;
import org.n52.javaps.io.complex.ComplexData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 *            the accessible member type
 * @param <B>
 *            the binding type
 */
class ComplexInputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractInputBinding<M>>
        extends AbstractInputAnnotationParser<ComplexInput, M, B> {

    private final InputHandlerRepository parserRepository;

    ComplexInputAnnotationParser(Function<M, B> bindingFunction, InputHandlerRepository parserRepository) {
        super(bindingFunction);
        this.parserRepository = Objects.requireNonNull(parserRepository);
    }

    @Override
    protected TypedComplexInputDescription createDescription(ComplexInput annotation,
            B binding) {
        @SuppressWarnings("unchecked") Class<? extends ComplexData<?>> bindingClass =
                (Class<? extends ComplexData<?>>) annotation.binding();
        Set<Format> supportedFormats = this.parserRepository.getSupportedFormats(bindingClass);
        Format defaultFormat = this.parserRepository.getDefaultFormat(bindingClass).orElse(null);
        return new TypedProcessDescriptionFactory().complexInput().withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt()).withTitle(annotation.title())
                .withMinimalOccurence(annotation.minOccurs()).withMaximalOccurence(annotation.maxOccurs())
                .withMaximumMegabytes(annotation.maximumMegaBytes()).withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats).withType(bindingClass).build();
    }

    @Override
    public Class<? extends ComplexInput> getSupportedAnnotation() {
        return ComplexInput.class;
    }
}
