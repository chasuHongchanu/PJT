package com.example.trend.exception;

public class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    @Override
    public String toString() {
        return errorCode + " : " + errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}