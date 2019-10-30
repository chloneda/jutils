package com.chloneda.jutils.exception;

/**
 * @Created by chloneda
 * @Description: 运行时异常
 */
public class ChlonedaRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private long exceptionCode;

    public ChlonedaRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChlonedaRuntimeException(String message) {
        super(message);
    }

    public ChlonedaRuntimeException(Throwable cause) {
        super(cause);
    }

    public long getExceptionCode() {
        return this.exceptionCode;
    }

    public void setExceptionCode(long exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
