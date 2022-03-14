/*
 * Copyright 2016-2022 52°North Spatial Information Research GmbH
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
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

import org.n52.javaps.description.TypedLiteralOutputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.literal.LiteralType;
import org.n52.javaps.io.literal.LiteralTypeRepository;

class LiteralOutputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractOutputBinding<M>> extends
        AbstractOutputAnnotationParser<LiteralOutput, M, B> {

    private final LiteralTypeRepository literalTypeRepository;

    LiteralOutputAnnotationParser(Function<M, B> bindingFunction, LiteralTypeRepository literalTypeRepository) {
        super(bindingFunction);
        this.literalTypeRepository = Objects.requireNonNull(literalTypeRepository, "literalDataManager");
    }

    @Override
    public Class<? extends LiteralOutput> getSupportedAnnotation() {
        return LiteralOutput.class;
    }

    @SuppressWarnings("unchecked")
    public LiteralType<?> getLiteralType(LiteralOutput annotation,
            B binding) {
        Type payloadType = binding.getPayloadType();
        Class<? extends LiteralType<?>> bindingType = (Class<? extends LiteralType<?>>) annotation.binding();
        if (payloadType instanceof Class<?>) {
            return literalTypeRepository.getLiteralType(bindingType, (Class<?>) payloadType);
        } else {
            return literalTypeRepository.getLiteralType(bindingType);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedLiteralOutputDescription createDescription(LiteralOutput annotation,
            B binding) {
        LiteralType<?> bindingType = getLiteralType(annotation, binding);
        TypedProcessDescriptionFactory descriptionFactory = new TypedProcessDescriptionFactory();
        return descriptionFactory.literalOutput().withTitle(annotation.title()).withAbstract(annotation.abstrakt())
                .withIdentifier(annotation.identifier()).withDefaultLiteralDataDomain(descriptionFactory
                        .literalDataDomain().withDataType(bindingType.getDataType()).withUOM(annotation.uom()))
                .withType(bindingType).build();

    }
}
