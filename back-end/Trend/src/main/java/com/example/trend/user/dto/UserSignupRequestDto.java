package com.example.trend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    @NotBlank(message = "국가를 선택해주세요.")
    private String country;
    @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
    private String userPhoneNumber;
    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String userAddress;
}
