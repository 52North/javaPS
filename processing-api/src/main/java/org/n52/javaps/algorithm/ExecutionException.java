package org.n52.javaps.algorithm;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ExecutionException extends Exception {
    private static final long serialVersionUID = 976278712962163368L;

    public ExecutionException() {
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

    protected ExecutionException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
