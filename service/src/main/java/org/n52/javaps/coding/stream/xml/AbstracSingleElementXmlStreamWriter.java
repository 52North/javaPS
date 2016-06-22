package org.n52.javaps.coding.stream.xml;

import java.util.Objects;

import javax.xml.stream.XMLStreamException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstracSingleElementXmlStreamWriter<S> extends AbstractMultiElementXmlStreamWriter {
    private final Class<? extends S> keyClass;

    public AbstracSingleElementXmlStreamWriter(Class<? extends S> keyClass) {
        super(keyClass);
        this.keyClass = Objects.requireNonNull(keyClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeElement(Object object)
            throws XMLStreamException {
        if (context() == null) {
            throw new IllegalStateException();
        }
        if (!keyClass.isAssignableFrom(object.getClass())) {
            throw unsupported(object);
        }

        write((S) object);
    }

    protected abstract void write(S object)
            throws XMLStreamException;
}
