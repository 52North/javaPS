package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Method;

import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.annotation.binding.InputMethodBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataInputMethodAnnotationParser extends LiteralDataInputAnnotationParser<Method, InputBinding<Method, LiteralInputDescription>> {
    @Override
    public InputBinding<Method, LiteralInputDescription> createBinding(Method member) {
        return new InputMethodBinding<>(member);
    }

}
