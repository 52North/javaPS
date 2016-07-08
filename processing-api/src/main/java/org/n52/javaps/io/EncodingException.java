package org.n52.javaps.io;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class EncodingException extends Exception{
    private static final long serialVersionUID = 8198126087527025494L;

    public EncodingException() {
    }

    public EncodingException(String message) {
        super(message);
    }

    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncodingException(Throwable cause) {
        super(cause);
    }

}
