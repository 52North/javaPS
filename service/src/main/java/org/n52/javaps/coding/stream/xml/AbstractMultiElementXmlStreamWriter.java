package org.n52.javaps.coding.stream.xml;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.n52.javaps.coding.stream.StreamWriterKey;
import org.n52.javaps.coding.stream.UnsupportedStreamWriterInputException;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public abstract class AbstractMultiElementXmlStreamWriter extends AbstractElementXmlStreamWriter {

    private final Set<StreamWriterKey> keys;

    public AbstractMultiElementXmlStreamWriter(Class<?>... supportedClasses) {
        this.keys = Arrays.stream(supportedClasses).map(XmlStreamWriterKey::new).collect(toSet());
    }

    protected UnsupportedStreamWriterInputException unsupported(Object object) {
        return new UnsupportedStreamWriterInputException(object);
    }

    @Override
    public Set<StreamWriterKey> getKeys() {
        return Collections.unmodifiableSet(this.keys);
    }
}
