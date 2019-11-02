package com.chloneda.jutils.exception;

/**
 * @Created by chloneda
 * @Description:
 */
public class ChlonedaException extends Exception {
    private static final long serialVersionUID = 1L;

    private String exceptionCode;

    public ChlonedaException(String message) {
        super(message);
    }

    public ChlonedaException(String message, Throwable cause) {
        super(message, cause);
        if (cause instanceof ChlonedaException) {
            this.exceptionCode = ((ChlonedaException) cause).exceptionCode;
        }

    }

    public ChlonedaException(Throwable cause) {
        super(cause);
        if (cause instanceof ChlonedaException) {
            this.exceptionCode = ((ChlonedaException) cause).exceptionCode;
        }

    }

    public String getExceptionCode() {
        return this.exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
