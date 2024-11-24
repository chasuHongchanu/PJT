package com.example.trend.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponseDto {
    private String userProfileImgUrl;
    private String userNickname;

    public static UserUpdateResponseDto of(UserUpdateRequestDto requestDto) {
        return UserUpdateResponseDto.builder()
                .userProfileImgUrl(requestDto.getUserProfileImgUrl())
                .userNickname(requestDto.getUserNickname())
                .build();
    }
}
