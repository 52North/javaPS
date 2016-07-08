package org.n52.javaps.io;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class DecodingException extends Exception{
    private static final long serialVersionUID = 7441634905552922457L;


    public DecodingException() {
    }

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodingException(Throwable cause) {
        super(cause);
    }

}
