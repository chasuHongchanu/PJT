package com.example.trend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  // 400 BAD_REQUEST
  INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "인자값이 잘못되었습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "권한이 없습니다."),

  // 500 INTERNAL_SERVER_ERROR
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred");

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
