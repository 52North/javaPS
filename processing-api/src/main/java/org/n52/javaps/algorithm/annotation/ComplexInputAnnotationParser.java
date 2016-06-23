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

import org.n52.javaps.io.ParserRepository;
import org.n52.javaps.io.data.IComplexData;
import org.n52.javaps.io.data.IData;
import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.wps.description.ComplexInputDescription;
import org.n52.javaps.ogc.wps.description.ComplexInputDescriptionImpl;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
class ComplexInputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractInputBinding<M, ComplexInputDescription>>
        extends AbstractInputAnnotationParser<ComplexInput, M, ComplexInputDescription, B> {

    private final ParserRepository parserRepository;

    ComplexInputAnnotationParser(Function<M, B> bindingFunction, ParserRepository parserRepository) {
        super(bindingFunction);
        this.parserRepository = Objects.requireNonNull(parserRepository);
    }

    @Override
    protected ComplexInputDescription createDescription(ComplexInput annotation, B binding) {
        Class<? extends IComplexData> bindingClass = annotation.binding();
        Set<Format> supportedFormats = this.parserRepository.getSupportedFormats(bindingClass);
        Format defaultFormat = this.parserRepository.getDefaultFormat(bindingClass).orElse(null);
        return ComplexInputDescriptionImpl.builder()
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .withMinimalOccurence(annotation.minOccurs())
                .withMaximalOccurence(annotation.maxOccurs())
                .withMaximumMegabytes(annotation.maximumMegaBytes())
                .withDefaultFormat(defaultFormat)
                .withSupportedFormat(supportedFormats)
                .withBindingClass(bindingClass)
                .build();
    }

    @Override
    public Class<? extends ComplexInput> getSupportedAnnotation() {
        return ComplexInput.class;
    }

    @Override
    public Class<? extends IData> getBindingType(ComplexInput annotation, B binding) {
        return annotation.binding();
    }
}
