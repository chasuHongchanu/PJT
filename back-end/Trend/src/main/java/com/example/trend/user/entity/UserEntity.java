package com.example.trend.user.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.sql.Timestamp;

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
