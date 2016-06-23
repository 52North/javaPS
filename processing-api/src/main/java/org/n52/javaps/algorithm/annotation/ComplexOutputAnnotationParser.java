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

import org.n52.javaps.ogc.wps.description.ComplexOutputDescription;
import org.n52.javaps.ogc.wps.Format;
import org.n52.javaps.ogc.wps.description.ComplexOutputDescriptionImpl;
import org.n52.javaps.io.GeneratorRepository;
import org.n52.javaps.io.data.IComplexData;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
class ComplexOutputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractOutputBinding<M, ComplexOutputDescription>>
        extends AbstractOutputAnnotationParser<ComplexOutput, M, ComplexOutputDescription, B> {

    private final GeneratorRepository generatorRepository;

    ComplexOutputAnnotationParser(Function<M, B> bindingFunction, GeneratorRepository generatorRepository) {
        super(bindingFunction);
        this.generatorRepository = Objects.requireNonNull(generatorRepository);
    }

    @Override
    protected ComplexOutputDescription createDescription(ComplexOutput annotation, B binding) {
        Class<? extends IComplexData> bindingClass = annotation.binding();
        Set<Format> supportedFormats = this.generatorRepository.getSupportedFormats(bindingClass);
        Format defaultFormat = this.generatorRepository.getDefaultFormat(bindingClass).orElse(null);
        return ComplexOutputDescriptionImpl.builder()
                .withBindingClass(bindingClass)
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

    @Override
    protected Class<? extends IData> getBindingType(ComplexOutput annotation, B binding) {
        return annotation.binding();
    }
}
