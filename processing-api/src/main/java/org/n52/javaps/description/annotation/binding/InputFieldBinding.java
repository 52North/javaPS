package org.n52.javaps.description.annotation.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.javaps.description.ProcessInputDescription;
import org.n52.javaps.io.data.IData;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 * @param <D>
 */
public class InputFieldBinding<D extends ProcessInputDescription> extends InputBinding<Field, D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputFieldBinding.class);

    public InputFieldBinding(Field field) {
        super(field);
    }

    @Override
    public Type getMemberType() {
        return getMember().getGenericType();
    }

    @Override
    public boolean validate() {
        if (!checkModifier()) {
            LOGGER.error("Field {} with input annotation can't be used, not public.", getMember());
            return false;
        }
        if (getDescription().getOccurence().isMultiple() || !isMemberTypeList()) {
            LOGGER.error("Field {} with input annotation can't be used, maxOccurs > 1 and field is not of type List", getMember());
            return false;
        }
        if (!checkType()) {
            LOGGER.error("Field {} with input annotation can't be used, unable to safely assign field using binding payload type", getMember());
            return false;
        }
        return true;
    }

    @Override
    public void set(Object annotatedObject, List<IData> boundInputList) {
        try {
            getMember().set(annotatedObject, unbindInput(boundInputList));
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Internal error processing inputs", ex);
        }
    }

}
