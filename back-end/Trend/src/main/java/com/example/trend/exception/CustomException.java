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
        if(this.exception != null) {
            return errorCode + " : " + errorCode.getMessage() + "\n" + "errorMessage: " + exception.getMessage();
        }
        else {
            return errorCode + " : " + errorCode.getMessage();
        }
    }
}