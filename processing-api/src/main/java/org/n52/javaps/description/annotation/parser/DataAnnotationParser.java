package org.n52.javaps.description.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

import org.n52.javaps.description.DataDescription;
import org.n52.javaps.description.annotation.binding.DataBinding;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <A>
 * @param <M>
 * @param <D>
 * @param <B>
 */
public interface DataAnnotationParser<A extends Annotation, M extends AccessibleObject & Member, D extends DataDescription, B extends DataBinding<M, D>>
        extends AnnotationParser<A, M, B> {

    B createBinding(M member);

     @Override
    default B parse(A annotation, M member) {
        B binding = createBinding(member);
        binding.setBindingType(getBindingType(annotation, binding));
        binding.setDescription(createDescription(annotation, binding));
        return binding.validate() ? binding : null;
    }

    D createDescription(A annotation, B binding);

    Class<? extends IData> getBindingType(A annotation, B binding);
}
