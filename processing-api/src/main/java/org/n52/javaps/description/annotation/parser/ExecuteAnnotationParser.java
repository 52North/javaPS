package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Method;

import org.n52.javaps.description.annotation.Execute;
import org.n52.javaps.description.annotation.binding.ExecuteMethodBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ExecuteAnnotationParser implements AnnotationParser<Execute, Method, ExecuteMethodBinding> {
    @Override
    public ExecuteMethodBinding parse(Execute annotation, Method member) {
        ExecuteMethodBinding annotationBinding = new ExecuteMethodBinding(member);
        return annotationBinding.validate() ? annotationBinding : null;
    }

    @Override
    public Class<? extends Execute> getSupportedAnnotation() {
        return Execute.class;
    }

}
