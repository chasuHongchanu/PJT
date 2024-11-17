package com.example.trend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  // 물품
  // 400 BAD_REQUEST
  MISSING_ITEM_PRICE(HttpStatus.BAD_REQUEST, "가격은 필수 값입니다."),
  INVALID_RENTAL_PERIOD(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 빨라야 합니다."),
  INVALID_PRICE_RANGE(HttpStatus.BAD_REQUEST, "최대 가격이 최소 가격보다 커야 합니다."),
  FAIL_TO_DELETE_ITEM(HttpStatus.BAD_REQUEST, "물품 삭제에 실패했습니다."),


  // 유저
  // > 회원가입
  USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  FAIL_TO_SAVE_USER(HttpStatus.BAD_REQUEST, "회원 가입에 실패했습니다."),
  // > 로그인
  NOT_FOUND_LOGIN_USER(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 틀렸습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효한 refresh token이 아닙니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 유저가 존재하지 않습니다."),
  // > JWT token
  FAIL_TO_SAVE_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token 저장에 실패했습니다."),
  MISSING_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token이 만료되었습니다. 재로그인 하세요."),
  NOT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 존재하지 않거나 올바른 형태가 아닙니다."),
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않거나 만료되었습니다. 액세스 토큰을 갱신하세요."), FAIL_TO_UPDATE_USER(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보 업데이트에 실패했습니다."), FAIL_TO_HASING_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 해싱에 실패했습니다.");



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
