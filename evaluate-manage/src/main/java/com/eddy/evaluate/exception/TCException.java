package com.eddy.evaluate.exception;


import com.eddy.evaluate.util.JsonResult;


public class TCException extends RuntimeException {

    private static final int ERROR_CODE = 400;

    private int errorCode = ERROR_CODE;

    public TCException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TCException(String message) {
        super(message);
        this.errorCode = ERROR_CODE;
    }

    public TCException() {
        super(JsonResult.actionFailure().getErrorMessage());
        this.errorCode = ERROR_CODE;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
