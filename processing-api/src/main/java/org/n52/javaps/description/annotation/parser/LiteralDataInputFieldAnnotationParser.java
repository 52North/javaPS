package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Field;

import org.n52.javaps.description.LiteralInputDescription;
import org.n52.javaps.description.annotation.binding.InputBinding;
import org.n52.javaps.description.annotation.binding.InputFieldBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataInputFieldAnnotationParser extends LiteralDataInputAnnotationParser<Field, InputBinding<Field, LiteralInputDescription>> {
    @Override
    public InputBinding<Field, LiteralInputDescription> createBinding(Field member) {
        return new InputFieldBinding<>(member);
    }

}
