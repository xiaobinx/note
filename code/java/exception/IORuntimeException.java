package com.bq.http.exception;

/**
 * Created by xiaob on 2018/3/6.
 */

public class IORuntimeException extends RuntimeException {
    public IORuntimeException() {
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IORuntimeException(Throwable cause) {
        super(cause);
    }

}
