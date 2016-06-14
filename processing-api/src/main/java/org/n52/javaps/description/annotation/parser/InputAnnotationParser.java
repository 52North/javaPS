package org.n52.javaps.description.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.description.annotation.binding.InputBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <A>
 * @param <M>
 * @param <D>
 */
public interface InputAnnotationParser<A extends Annotation, M extends AccessibleObject & Member, D extends ProcessInputDescription, B extends InputBinding<M, D>>
        extends DataAnnotationParser<A, M, D, B> {
}
