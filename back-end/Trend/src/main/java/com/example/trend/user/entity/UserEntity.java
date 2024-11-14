package com.example.trend.user.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserEntity {
    private String userId;
    private String userNickname;
    private String userAddress;
    private String userEmail;
    private String userPhoneNumber;
    private String userProfileImg;
    private String userIntoduction;
    private Timestamp userRegistedDate;
    private double userActivityScore;
    private double userRating;

}
