package com.newstorm.exception;

public class JwtException extends BaseException{
    public JwtException() {
        super();
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtException(String message) {
        super(message);
    }

    public JwtException(Throwable cause) {
        super(cause);
    }
}
