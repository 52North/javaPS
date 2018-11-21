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

import static java.util.stream.Collectors.toList;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.javaps.description.TypedLiteralInputDescription;
import org.n52.javaps.description.impl.TypedProcessDescriptionFactory;
import org.n52.javaps.io.literal.LiteralType;
import org.n52.javaps.io.literal.LiteralTypeRepository;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 *            the accessible member type
 * @param <B>
 *            the binding type
 */
class LiteralInputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractInputBinding<M>>
        extends AbstractInputAnnotationParser<LiteralInput, M, B> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationParser.class);

    private final LiteralTypeRepository literalTypeRepository;

    LiteralInputAnnotationParser(Function<M, B> bindingFunction, LiteralTypeRepository literalTypeRepository) {
        super(bindingFunction);
        this.literalTypeRepository = Objects.requireNonNull(literalTypeRepository, "literalDataManager");
    }

    @SuppressWarnings({ "unchecked" })
    private List<String> getEnumValues(B binding) {
        if (!binding.isEnum()) {
            return Collections.emptyList();
        }
        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) binding.getType();
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).collect(toList());
    }

    @Override
    public TypedLiteralInputDescription createDescription(LiteralInput annotation,
            B binding) {
        // auto generate binding if it's not explicitly declared
        LiteralType<?> bindingType = getLiteralType(annotation, binding);
        List<String> allowedValues = getAllowedValues(annotation, binding);
        TypedProcessDescriptionFactory descriptionFactory = new TypedProcessDescriptionFactory();

        return descriptionFactory.literalInput().withIdentifier(annotation.identifier()).withTitle(annotation.title())
                .withAbstract(annotation.abstrakt()).withMinimalOccurence(annotation.minOccurs())
                .withMaximalOccurence(getMaxOccurence(annotation, allowedValues)).withType(bindingType)
                .withDefaultLiteralDataDomain(descriptionFactory.literalDataDomain()
                        .withValueDescription(allowedValues.isEmpty() ? OwsAnyValue.instance()
                                : new OwsAllowedValues(allowedValues.stream().map(OwsValue::new)))
                        .withDataType(bindingType.getDataType())
                        .withDefaultValue(getDefaultValue(annotation, allowedValues)).withUOM(annotation.uom()))
                .build();
    }

    @Override
    public Class<? extends LiteralInput> getSupportedAnnotation() {
        return LiteralInput.class;
    }

    public LiteralType<?> getLiteralType(LiteralInput annotation,
            B binding) {
        Type payloadType = binding.getPayloadType();
        @SuppressWarnings("unchecked") Class<? extends LiteralType<?>> bindingType =
                (Class<? extends LiteralType<?>>) annotation.binding();

        if (payloadType instanceof Class<?>) {
            return this.literalTypeRepository.getLiteralType(bindingType, (Class<?>) payloadType);
        } else {
            return this.literalTypeRepository.getLiteralType(bindingType);
        }
    }

    private long getMaxOccurence(LiteralInput annotation,
            List<String> allowedValues) {
        if (annotation.maxOccurs() == LiteralInput.ENUM_COUNT) {
            if (allowedValues.isEmpty()) {
                LOGGER.warn("Invalid maxOccurs \"ENUM_COUNT\" specified for for input {}, setting maxOccurs to {}",
                        annotation.identifier(), annotation.minOccurs());
                return annotation.minOccurs() > 0 ? annotation.minOccurs() : 1;
            } else {
                return allowedValues.size();
            }
        } else {
            return annotation.maxOccurs();
        }
    }

    private List<String> getAllowedValues(LiteralInput annotation,
            B binding) {
        List<String> enumValues = getEnumValues(binding);
        List<String> allowedValues = new ArrayList<>(Arrays.asList(annotation.allowedValues()));
        if (!enumValues.isEmpty()) {
            if (!allowedValues.isEmpty()) {
                allowedValues.stream().filter(x -> !enumValues.contains(x))
                        .peek(x -> LOGGER.warn("Invalid allowed value \"{}\" specified for for enumerated input {}", x,
                                annotation.identifier()))
                        .forEach(allowedValues::remove);
            } else {
                allowedValues = enumValues;
            }
        }
        return allowedValues;
    }

    private String getDefaultValue(LiteralInput annotation,
            List<String> allowedValues) {
        String defaultValue = annotation.defaultValue();
        if (!allowedValues.isEmpty() && !annotation.defaultValue().isEmpty()) {
            if (!allowedValues.contains(annotation.defaultValue())) {
                LOGGER.warn("Invalid default value \"{}\" specified for for enumerated input {}, ignoring.",
                        defaultValue, annotation.identifier());
                defaultValue = null;
            }
        }
        return defaultValue;
    }

}
