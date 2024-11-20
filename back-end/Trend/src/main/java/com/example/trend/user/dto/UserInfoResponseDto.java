package com.example.trend.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class UserInfoResponseDto {
    private String userId;
    private String userNickname;
    private String userAddress;
    private String userEmail;
    private String userPhoneNumber;
    private String userProfileImg;
    private String userIntroduction;
    private double userRating;
    private String country;
    private Timestamp userCreatedAt;
}
