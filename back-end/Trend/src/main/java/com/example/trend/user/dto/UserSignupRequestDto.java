package com.example.trend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserSignupRequestDto {
    @NotBlank(message = "ID는 필수 입력 사항입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    private String userPassword;
    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    private String userNickname;
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String userEmail;
    @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
    private String userPhoneNumber;
    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String userAddress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
