package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Method;

import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.annotation.binding.OutputMethodBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataOutputMethodAnnotationParser extends LiteralDataOutputAnnotationParser<Method, OutputBinding<Method, LiteralOutputDescription>> {
    @Override
    public OutputBinding<Method, LiteralOutputDescription> createBinding(Method member) {
        return new OutputMethodBinding<>(member);
    }

}
