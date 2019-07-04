package com.eddy.evaluate.exception;


public class InvalidParamsException extends RuntimeException {

    private int code = 401;

    public InvalidParamsException(String message, int code) {
        super(message);
        this.code = code;
    }

    public InvalidParamsException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}
