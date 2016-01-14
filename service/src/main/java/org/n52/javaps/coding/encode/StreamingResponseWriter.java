package org.n52.javaps.coding.encode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.n52.iceland.coding.encode.ResponseProxy;
import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StreamingResponseWriter<T> implements ResponseWriter<T> {

    private MediaType contentType = MediaTypes.APPLICATION_XML;

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    @Override
    public void write(T t, OutputStream out, ResponseProxy responseProxy)
            throws IOException, OwsExceptionReport {
        /* TODO implement org.n52.javaps.coding.encode.StreamingResponseWriter.write() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.encode.StreamingResponseWriter.write() not yet implemented");
    }

    @Override
    public boolean supportsGZip(T t) {
        /* TODO implement org.n52.javaps.coding.encode.StreamingResponseWriter.supportsGZip() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.encode.StreamingResponseWriter.supportsGZip() not yet implemented");
    }

    @Override
    public Set<ResponseWriterKey> getKeys() {
        /* TODO implement org.n52.javaps.coding.encode.StreamingResponseWriter.getKeys() */
        throw new UnsupportedOperationException("org.n52.javaps.coding.encode.StreamingResponseWriter.getKeys() not yet implemented");
    }

}
