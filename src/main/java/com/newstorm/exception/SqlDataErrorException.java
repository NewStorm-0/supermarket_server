package com.newstorm.exception;

public class SqlDataErrorException extends BaseException{
    public SqlDataErrorException() {
    }

    public SqlDataErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlDataErrorException(String message) {
        super(message);
    }

    public SqlDataErrorException(Throwable cause) {
        super(cause);
    }
}
