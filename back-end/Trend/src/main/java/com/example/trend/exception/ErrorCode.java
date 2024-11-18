package com.example.trend.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  // 물품
  // 400 BAD_REQUEST
  MISSING_ITEM_PRICE(HttpStatus.BAD_REQUEST, "가격은 필수 값입니다."),
  INVALID_RENTAL_PERIOD(HttpStatus.BAD_REQUEST, "시작일은 종료일보다 빨라야 합니다."),
  INVALID_PRICE_RANGE(HttpStatus.BAD_REQUEST, "최대 가격이 최소 가격보다 커야 합니다."),
  FAIL_TO_DELETE_ITEM(HttpStatus.BAD_REQUEST, "물품 삭제에 실패했습니다."),
  NO_SUCH_ITEM(HttpStatus.BAD_REQUEST, "물품이 존재하지 않습니다."),
  NO_SUCH_LESSOR(HttpStatus.BAD_REQUEST, "판매자가 존재하지 않습니다."),
  
  // 거래
  NO_ITEM_CONDITION_IMAGES(HttpStatus.BAD_REQUEST, "사용 전 물품 사진 등록은 필수입니다."),

  // 401 Unauthorized
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다."),

  // 500 INTERNAL_SERVER_ERROR
  FAIL_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 에러가 발생했습니다."),
  JSON_PROCCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 에러가 발생했습니다."),

  // 유저
  // > 회원가입
  USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  FAIL_TO_SAVE_USER(HttpStatus.BAD_REQUEST, "회원 가입에 실패했습니다."),

  // > 로그인
  NOT_FOUND_LOGIN_USER(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 틀렸습니다."),
  NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 유저가 존재하지 않습니다."),
  FAIL_TO_UPDATE_USER(HttpStatus.INTERNAL_SERVER_ERROR, "유저 정보 업데이트에 실패했습니다."), FAIL_TO_HASING_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 해싱에 실패했습니다."),

  // Password reset
  FAIL_TO_RESET_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 리셋에 실패했습니다."),

  // > JWT token
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효한 refresh token이 아닙니다."),
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않거나 만료되었습니다. 액세스 토큰을 갱신하세요."),
  FAIL_TO_SAVE_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token 저장에 실패했습니다."),
  MISSING_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token이 존재하지 않습니다."),
  REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token이 만료되었습니다. 재로그인 하세요."),
  NOT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 존재하지 않거나 올바른 형태가 아닙니다."),
  FAIL_TO_DELETE_USER(HttpStatus.BAD_REQUEST, "유저정보 삭제에 실패했습니다."),
  FAIL_TO_DELETE_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰 삭제에 실패했습니다."),

  // Course
  FAIL_TO_REGIST_COURSE(HttpStatus.INTERNAL_SERVER_ERROR, "코스 등록에 실패했습니다."), FAIL_TO_REGIST_COURSE_SPOT(HttpStatus.INTERNAL_SERVER_ERROR, "추천 관광지 목록 등록에 실패했습니다."),
  FAIL_TO_UPDATE_COURSE(HttpStatus.BAD_REQUEST, "게시글 수정에 실패했습니다."),
  FAIL_TO_DELETE_COURSE(HttpStatus.BAD_REQUEST, "게시글 삭제에 실패했습니다."), FAIL_TO_LIKE_COURSE(HttpStatus.BAD_REQUEST, "코스 좋아요 실패."), FAIL_TO_UNLIKE_COURSE(HttpStatus.BAD_REQUEST, "코스 좋아요 취소 실패."), FAIL_TO_SELECT_LIKE(HttpStatus.BAD_REQUEST, "좋아요 조회 실패.");



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
