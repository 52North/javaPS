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

import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.annotation.ComplexInput;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.impl.ComplexInputDescriptionImpl;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
public class ComplexDataInputAnnotationParser<M extends AccessibleObject & Member, B extends InputBinding<M, ComplexInputDescription>>
        extends InputAnnotationParser<ComplexInput, M, ComplexInputDescription, B> {

    public ComplexDataInputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @Override
    protected ComplexInputDescription createDescription(ComplexInput annotation, B binding) {
        return ComplexInputDescriptionImpl.builder()
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .withMinimalOccurence(annotation.minOccurs())
                .withMaximalOccurence(annotation.maxOccurs())
                .withMaximumMegabytes(annotation.maximumMegaBytes())
                .withBindingClass(annotation.binding())
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
