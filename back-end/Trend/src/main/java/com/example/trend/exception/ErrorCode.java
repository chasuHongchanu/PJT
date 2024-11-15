package com.example.trend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  // 400 BAD_REQUEST
  INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "인자값이 잘못되었습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "권한이 없습니다."),
  MISSING_ITEM_PRICE(HttpStatus.BAD_REQUEST, "가격은 필수 값입니다."),
  INVALID_RENTAL_PERIOD(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 빨라야합니다."),

  // 500 INTERNAL_SERVER_ERROR
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
  DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred"),

  // 유저
  // > 회원가입
  USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  FAIL_TO_SAVE_USER(HttpStatus.BAD_REQUEST, "회원 가입에 실패했습니다."),
  // > 로그인
  NOT_FOUND_MATCHED_USER(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 틀렸습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효한 refresh token이 아닙니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 유저가 존재하지 않습니다."),
  // > JWT token
  FAIL_TO_SAVE_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token 저장에 실패했습니다."),
  MISSING_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token이 만료되었습니다. 재로그인 하세요."),
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않거나 만료되었습니다. 액세스 토큰을 갱신하세요.");



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
