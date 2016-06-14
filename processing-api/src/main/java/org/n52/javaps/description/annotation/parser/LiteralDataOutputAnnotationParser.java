package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.annotation.LiteralDataOutput;
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
public abstract class LiteralDataOutputAnnotationParser<M extends AccessibleObject & Member, B extends OutputBinding<M, LiteralOutputDescription>>
        implements OutputAnnotationParser<LiteralDataOutput, M, LiteralOutputDescription, B> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiteralDataOutputAnnotationParser.class);

    @Override
    public Class<? extends LiteralDataOutput> getSupportedAnnotation() {
        return LiteralDataOutput.class;
    }

    @Override
    public Class<? extends IData> getBindingType(LiteralDataOutput annotation, B binding) {
        Type payloadType = binding.getPayloadType();
        Class<? extends ILiteralData> bindingType = annotation.binding();
        if (bindingType == null || ILiteralData.class.equals(bindingType)) {
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
    public LiteralOutputDescription createDescription(LiteralDataOutput annotation, B binding) {
        String dataType = BasicXMLTypeFactory.getXMLDataTypeforBinding((Class<? extends ILiteralData>) binding.getBindingType());
        return LiteralOutputDescriptionImpl.builder()
                .withTitle(annotation.title())
                .withAbstract(annotation.abstrakt())
                .withIdentifier(annotation.identifier())
                .withDefaultLiteralDataDomain(LiteralDataDomainImpl.builder().withDataType(dataType))
                .build();

    }

}
