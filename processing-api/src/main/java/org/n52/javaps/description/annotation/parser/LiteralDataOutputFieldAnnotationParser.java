package org.n52.javaps.description.annotation.parser;

import java.lang.reflect.Field;

import org.n52.javaps.description.LiteralOutputDescription;
import org.n52.javaps.description.annotation.binding.OutputBinding;
import org.n52.javaps.description.annotation.binding.OutputFieldBinding;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralDataOutputFieldAnnotationParser extends LiteralDataOutputAnnotationParser<Field, OutputBinding<Field, LiteralOutputDescription>> {

    @Override
    public OutputBinding<Field, LiteralOutputDescription> createBinding(Field member) {
        return new OutputFieldBinding<>(member);
    }

}
