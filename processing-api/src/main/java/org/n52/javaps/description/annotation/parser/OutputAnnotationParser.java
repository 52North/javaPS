package org.n52.javaps.description.annotation.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;

import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.description.annotation.binding.OutputBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 * @param <A>
 * @param <M>
 * @param <D>
 * @param <B>
 */
public interface OutputAnnotationParser<A extends Annotation, M extends AccessibleObject & Member, D extends ProcessOutputDescription, B extends OutputBinding<M, D>>
        extends DataAnnotationParser<A, M, D, B> {
}
