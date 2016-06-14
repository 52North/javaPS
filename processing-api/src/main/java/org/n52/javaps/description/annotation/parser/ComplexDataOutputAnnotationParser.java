package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.n52.javaps.description.ComplexOutputDescription;
import org.n52.javaps.description.annotation.ComplexDataOutput;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.annotation.binding.OutputFieldBinding;
import org.n52.javaps.description.annotation.binding.OutputMethodBinding;
import org.n52.javaps.description.impl.ComplexOutputDescriptionImpl;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <M>
 * @param <B>
 */
public interface ComplexDataOutputAnnotationParser<M extends AccessibleObject & Member, B extends OutputBinding<M, ComplexOutputDescription>>
        extends
        OutputAnnotationParser<ComplexDataOutput, M, ComplexOutputDescription, B> {

    @Override
    default ComplexOutputDescription createDescription(ComplexDataOutput annotation, B binding) {
        return ComplexOutputDescriptionImpl.builder()
                .withIdentifier(annotation.identifier())
                .withAbstract(annotation.abstrakt())
                .withTitle(annotation.title())
                .build();
    }

    @Override
    default Class<? extends ComplexDataOutput> getSupportedAnnotation() {
        return ComplexDataOutput.class;
    }

    @Override
    public default Class<? extends IData> getBindingType(ComplexDataOutput annotation, B binding) {
        return annotation.binding();
    }

    public static class ComplexDataOutputFieldAnnotationParser implements ComplexDataOutputAnnotationParser<Field, OutputBinding<Field, ComplexOutputDescription>> {
        @Override
        public OutputBinding<Field, ComplexOutputDescription> createBinding(Field member) {
            return new OutputFieldBinding<>(member);
        }

    }

    public class ComplexDataOutputMethodAnnotationParser implements ComplexDataOutputAnnotationParser<Method, OutputBinding<Method, ComplexOutputDescription>> {
        @Override
        public OutputBinding<Method, ComplexOutputDescription> createBinding(Method member) {
            return new OutputMethodBinding<>(member);
        }
    }

}
