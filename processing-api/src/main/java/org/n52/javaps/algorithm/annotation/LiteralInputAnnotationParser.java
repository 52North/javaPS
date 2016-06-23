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

import static java.util.stream.Collectors.toList;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.io.BasicXMLTypeFactory;
import org.n52.javaps.io.data.ILiteralData;
import org.n52.javaps.ogc.ows.OwsAllowedValues;
import org.n52.javaps.ogc.ows.OwsValue;
import org.n52.javaps.ogc.wps.description.LiteralDataDomainImpl;
import org.n52.javaps.ogc.wps.description.LiteralInputDescription;
import org.n52.javaps.ogc.wps.description.LiteralInputDescription.Builder;
import org.n52.javaps.ogc.wps.description.LiteralInputDescriptionImpl;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
class LiteralInputAnnotationParser<M extends AccessibleObject & Member, B extends AbstractInputBinding<M, LiteralInputDescription>>
       extends AbstractInputAnnotationParser<LiteralInput, M, LiteralInputDescription, B> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationParser.class);

    LiteralInputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<String> getEnumValues(B binding) {
        if (!binding.isEnum()) {
            return Collections.emptyList();
        }
        Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) binding.getType();
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).collect(toList());
    }

    @Override
    public LiteralInputDescription createDescription(LiteralInput annotation, B binding) {
        // auto generate binding if it's not explicitly declared
        Class<? extends ILiteralData> bindingType = getBindingType(annotation, binding);

        String defaultValue = annotation.defaultValue();
        // If InputType is enum
        // 1) generate allowedValues if not explicitly declared
        // 2) validate allowedValues if explicitly declared
        // 3) validate defaultValue if declared
        // 4) check for special ENUM_COUNT maxOccurs flag
        Builder<?, ?> builder = LiteralInputDescriptionImpl.builder();
        builder.withIdentifier(annotation.identifier())
                .withTitle(annotation.title())
                .withAbstract(annotation.abstrakt())
                .withMinimalOccurence(annotation.minOccurs())
                .withBindingClass(bindingType);



        List<String> enumValues = getEnumValues(binding);
        List<String> allowedValues = new ArrayList<>(Arrays.asList(annotation.allowedValues()));
        if (!enumValues.isEmpty()) {
            if (!allowedValues.isEmpty()) {
                allowedValues.stream()
                        .filter(x -> !enumValues.contains(x))
                        .peek(x -> LOGGER.warn("Invalid allowed value \"{}\" specified for for enumerated input {}", x, annotation.identifier()))
                        .forEach(allowedValues::remove);
            } else {
                allowedValues = enumValues;
            }
        }

         if (annotation.maxOccurs() == LiteralInput.ENUM_COUNT) {
            if (allowedValues.isEmpty()) {
                LOGGER.warn("Invalid maxOccurs \"ENUM_COUNT\" specified for for input {}, setting maxOccurs to {}", annotation.identifier(), annotation.minOccurs());
                builder.withMaximalOccurence(annotation.minOccurs() > 0 ? annotation.minOccurs() : 1);
            } else {
                builder.withMaximalOccurence(allowedValues.size());
            }
        } else {
            builder.withMaximalOccurence(annotation.maxOccurs());
        }

        if (!allowedValues.isEmpty() && !annotation.defaultValue().isEmpty()) {
            if (!allowedValues.contains(annotation.defaultValue())) {
                LOGGER.warn("Invalid default value \"{}\" specified for for enumerated input {}, ignoring.", defaultValue, annotation.identifier());
                defaultValue = null;
            }
        }

        builder.withDefaultLiteralDataDomain(LiteralDataDomainImpl.builder()
                .withValueDescription(new OwsAllowedValues(allowedValues.stream().map(OwsValue::new)))
                .withDataType(BasicXMLTypeFactory.getXMLDataTypeforBinding(bindingType))
                .withDefaultValue(defaultValue)
                .withUOM(annotation.uom()));

        return builder.build();
    }

    @Override
    public Class<? extends LiteralInput> getSupportedAnnotation() {
        return LiteralInput.class;
    }

    @Override
    public Class<? extends ILiteralData> getBindingType(LiteralInput annotation, B binding) {
        Type payloadType = binding.getPayloadType();
        Class<? extends ILiteralData> bindingType = annotation.binding();
        if (bindingType == null || ILiteralData.class.equals(bindingType)) {
            if (payloadType instanceof Class<?>) {
                bindingType = BasicXMLTypeFactory.getBindingForPayloadType((Class<?>) payloadType);
                if (bindingType == null) {
                    LOGGER.error("Unable to locate binding class for {}; binding not found.", payloadType);
                }
            } else if (binding.isMemberTypeList()) {
                LOGGER.error("Unable to determine binding class for {}; List must be parameterized with a type matching a known binding payload to use auto-binding.", payloadType);
            } else {
                LOGGER.error("Unable to determine binding class for {}; type must fully resolved to use auto-binding", payloadType);
            }
        }
        return bindingType;
    }

}
