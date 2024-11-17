package com.example.trend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPwRequestDto {
    private String userId;
    private String userEmail;
    private String newPassword;
}
