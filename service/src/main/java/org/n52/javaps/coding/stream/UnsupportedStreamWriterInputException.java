package org.n52.javaps.coding.stream;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class UnsupportedStreamWriterInputException extends RuntimeException {
    private static final long serialVersionUID = 3114346491116147405L;

    public UnsupportedStreamWriterInputException(Object input) {
        super(String.format("Unsupported input: %s", input));
    }

}
