package org.n52.javaps.coding.encode;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterFactory;
import org.n52.iceland.coding.encode.ResponseWriterKey;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class StreamingResponseWriterFactory implements ResponseWriterFactory{

    private static final Logger log = LoggerFactory.getLogger(StreamingResponseWriterFactory.class);

    @Override
    public Set<ResponseWriterKey> getKeys() {
        /* TODO implement org.n52.javaps.coding.encode.StreamingResponseWriterFactory.getKeys() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.encode.StreamingResponseWriterFactory.getKeys() not yet implemented");
    }

    @Override
    public ResponseWriter<?> create(ResponseWriterKey key) {
        /* TODO implement org.n52.javaps.coding.encode.StreamingResponseWriterFactory.create() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.encode.StreamingResponseWriterFactory.create() not yet implemented");
    }

}
