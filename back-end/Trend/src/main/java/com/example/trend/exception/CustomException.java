package com.example.trend.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private Exception exception;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Exception exception) {
        this.errorCode = errorCode;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return errorCode + " : " + errorCode.getMessage() + "\n" + exception.getMessage();
    }
}