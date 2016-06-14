package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.n52.javaps.description.ComplexInputDescription;
import org.n52.javaps.description.annotation.ComplexDataInput;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.annotation.binding.InputFieldBinding;
import org.n52.javaps.description.annotation.binding.InputMethodBinding;
import org.n52.javaps.description.impl.ComplexInputDescriptionImpl;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
public interface ComplexDataInputAnnotationParser<M extends AccessibleObject & Member, B extends InputBinding<M, ComplexInputDescription>>
        extends InputAnnotationParser<ComplexDataInput, M, ComplexInputDescription, B> {

    @Override
    default ComplexInputDescription createDescription(ComplexDataInput annotation, B binding) {
        return ComplexInputDescriptionImpl.builder()
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .withMinimalOccurence(annotation.minOccurs())
                .withMaximalOccurence(annotation.maxOccurs())
                .withMaximumMegabytes(annotation.maximumMegaBytes())
                .build();

    }

    @Override
    default Class<? extends ComplexDataInput> getSupportedAnnotation() {
        return ComplexDataInput.class;
    }

    @Override
    public default Class<? extends IData> getBindingType(ComplexDataInput annotation, B binding) {
        return annotation.binding();
    }

    public static class ComplexDataInputMethodAnnotationParser implements ComplexDataInputAnnotationParser<Method, InputBinding<Method, ComplexInputDescription>> {
        @Override
        public InputBinding<Method, ComplexInputDescription> createBinding(Method member) {
            return new InputMethodBinding<>(member);
        }
    }

    public static class ComplexDataInputFieldAnnotationParser implements ComplexDataInputAnnotationParser<Field, InputBinding<Field, ComplexInputDescription>> {
        @Override
        public InputBinding<Field, ComplexInputDescription> createBinding(Field member) {
            return new InputFieldBinding<>(member);
        }

    }

}
