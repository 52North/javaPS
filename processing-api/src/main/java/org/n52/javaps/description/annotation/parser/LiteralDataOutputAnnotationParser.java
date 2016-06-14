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
import java.lang.reflect.Type;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.annotation.LiteralOutput;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.impl.LiteralDataDomainImpl;
import org.n52.javaps.description.impl.LiteralOutputDescriptionImpl;
import org.n52.javaps.io.BasicXMLTypeFactory;
import org.n52.javaps.io.data.IData;
import org.n52.javaps.io.data.ILiteralData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataOutputAnnotationParser<M extends AccessibleObject & Member, B extends OutputBinding<M, LiteralOutputDescription>>
        extends OutputAnnotationParser<LiteralOutput, M, LiteralOutputDescription, B> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiteralDataOutputAnnotationParser.class);

    public LiteralDataOutputAnnotationParser(Function<M, B> bindingFunction) {
        super(bindingFunction);
    }

    @Override
    public Class<? extends LiteralOutput> getSupportedAnnotation() {
        return LiteralOutput.class;
    }

    @Override
    public Class<? extends IData> getBindingType(LiteralOutput annotation, B binding) {
        Type payloadType = binding.getPayloadType();
        Class<? extends ILiteralData> bindingType = annotation.binding();
        if (bindingType == null || bindingType == ILiteralData.class) {
            if (payloadType instanceof Class<?>) {
                bindingType = BasicXMLTypeFactory.getBindingForPayloadType((Class<?>) payloadType);
                if (bindingType == null) {
                    LOGGER.error("Unable to locate binding class for {}; binding not found.", payloadType);
                }
            } else {
                LOGGER.error("Unable to determine binding class for {}; type must fully resolved to use auto-binding", payloadType);
            }
        }
        return bindingType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LiteralOutputDescription createDescription(LiteralOutput annotation, B binding) {
        String dataType = BasicXMLTypeFactory.getXMLDataTypeforBinding((Class<? extends ILiteralData>) binding.getDescription().getBindingClass());
        return LiteralOutputDescriptionImpl.builder()
                .withTitle(annotation.title())
                .withAbstract(annotation.abstrakt())
                .withIdentifier(annotation.identifier())
                .withDefaultLiteralDataDomain(LiteralDataDomainImpl.builder().withDataType(dataType))
                .withBindingClass(null)
                .build();

    }
}
