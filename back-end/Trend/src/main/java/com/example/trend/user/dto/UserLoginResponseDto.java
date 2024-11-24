package com.example.trend.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLoginResponseDto {
    private String userId;
    private String nickName;
    private String profileImgUrl;
}
