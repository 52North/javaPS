package org.n52.javaps.description.annotation.parser;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.ogc.ows.OwsAllowedValue;
import org.n52.iceland.ogc.ows.OwsAllowedValues;
import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.LiteralInputDescriptionBuilder;
import org.n52.javaps.description.annotation.LiteralDataInput;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.impl.LiteralDataDomainImpl;
import org.n52.javaps.description.impl.LiteralInputDescriptionImpl;
import org.n52.javaps.io.BasicXMLTypeFactory;
import org.n52.javaps.io.data.ILiteralData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
public abstract class LiteralDataInputAnnotationParser<M extends AccessibleObject & Member, B extends InputBinding<M, LiteralInputDescription>>
        implements InputAnnotationParser<LiteralDataInput, M, LiteralInputDescription, B> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationParser.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<String> getEnumValues(B binding) {
        if (!binding.isTypeEnum()) {
            return Collections.emptyList();
        }

        Class<? extends Enum<?>> type = (Class<? extends Enum<?>>) binding.getType();
        return Arrays.stream(type.getEnumConstants()).map(Enum::name).collect(toList());
    }

    @Override
    public LiteralInputDescription createDescription(LiteralDataInput annotation, B binding) {
        // auto generate binding if it's not explicitly declared
        Class<? extends ILiteralData> bindingType = getBindingType(annotation, binding);

        String defaultValue = annotation.defaultValue();
        // If InputType is enum
        // 1) generate allowedValues if not explicitly declared
        // 2) validate allowedValues if explicitly declared
        // 3) validate defaultValue if declared
        // 4) check for special ENUM_COUNT maxOccurs flag
        LiteralInputDescriptionBuilder<?, ?> builder = LiteralInputDescriptionImpl.builder();
        builder.withIdentifier(annotation.identifier())
                .withTitle(annotation.title())
                .withAbstract(annotation.abstrakt())
                .withMinimalOccurence(annotation.minOccurs());



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

            if (annotation.maxOccurs() == LiteralDataInput.ENUM_COUNT) {
                builder.withMaximalOccurence(annotation.maxOccurs());
            }

        } else if (annotation.maxOccurs() == LiteralDataInput.ENUM_COUNT) {
            builder.withMaximalOccurence(annotation.minOccurs());
            LOGGER.warn("Invalid maxOccurs \"ENUM_COUNT\" specified for for input {}, setting maxOccurs to {}", annotation.identifier(), annotation.minOccurs());
        }

        if (!allowedValues.isEmpty() && !annotation.defaultValue().isEmpty()) {
            if (!allowedValues.contains(annotation.defaultValue())) {
                LOGGER.warn("Invalid default value \"{}\" specified for for enumerated input {}, ignoring.", defaultValue, annotation.identifier());
                defaultValue = null;
            }
        }

        builder.withDefaultLiteralDataDomain(LiteralDataDomainImpl.builder()
                .withAllowedValues(new OwsAllowedValues(allowedValues.stream().map(OwsAllowedValue::new)))
                .withDataType(BasicXMLTypeFactory.getXMLDataTypeforBinding(bindingType))
                .withDefaultValue(defaultValue));

        return builder.build();
    }

    @Override
    public Class<? extends LiteralDataInput> getSupportedAnnotation() {
        return LiteralDataInput.class;
    }

    @Override
    public Class<? extends ILiteralData> getBindingType(LiteralDataInput annotation, B binding) {
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
