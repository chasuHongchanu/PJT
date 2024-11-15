package com.example.trend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank(message = "ID는 필수 입력값입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String userPassword;
}
