package com.eddy.evaluate.exception;


public class LoginFailureException extends RuntimeException {

    private int code = 401;

    public LoginFailureException(String message) {
        super(message);
        this.code = 401;
    }

    public int getCode() {
        return code;
    }
}
