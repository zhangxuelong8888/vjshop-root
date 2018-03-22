package com.vjshop.exception;

/**
 * Created by moss-zc on 2017/6/20.
 *
 * @author yishop term
 * @since 0.1
 */
public class LucenceIndexException extends RuntimeException{
    public LucenceIndexException() {
        super();
    }

    public LucenceIndexException(String message) {
        super(message);
    }

    public LucenceIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public LucenceIndexException(Throwable cause) {
        super(cause);
    }

    protected LucenceIndexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
