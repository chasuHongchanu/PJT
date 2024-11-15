package com.example.trend.user.entity;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private String userId;
//    private String userPassword;
    private String userNickname;
    private String userAddress;
    private String userEmail;
    private String userPhoneNumber;
    private String userProfileImg;
    private String userIntroduction;
    private double userActivityScore;
    private double userRating;
    private String country;
    private Timestamp userCreatedAt;


}
