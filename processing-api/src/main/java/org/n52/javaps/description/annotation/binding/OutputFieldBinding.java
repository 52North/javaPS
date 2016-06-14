package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.ProcessOutputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 * @param <D>
 */
public class OutputFieldBinding<D extends ProcessOutputDescription> extends OutputBinding<Field, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputFieldBinding.class);

    public OutputFieldBinding(Field field) {
        super(field);
    }

    @Override
    public Type getMemberType() {
        return getMember().getGenericType();
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Field {} with output annotation can't be used, not public.", getMember());
            return false;
        }
        if (!checkType()) {
            LOGGER.error("Field {} with output annotation can't be used, unable to safely construct binding using field type", getMember());
            return false;
        }
        return true;
    }

    @Override
    public IData get(Object annotatedInstance) {
        Object value;
        try {
            value = getMember().get(annotatedInstance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Internal error processing inputs", ex);
        }
        return value == null ? null : bindOutputValue(value);
    }

}
