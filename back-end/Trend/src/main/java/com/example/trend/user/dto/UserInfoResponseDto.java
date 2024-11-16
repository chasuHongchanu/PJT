package com.example.trend.user.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class UserInfoResponseDto {
    private String userNickname;
    private String userAddress;
    private String userEmail;
    private String userPhoneNumber;
    private String userProfileImg;
    private String userIntroduction;
    private double userRating;
    private String country;
    private Timestamp userCreatedAt;
    // 물품 거래와 리뷰 작성 이후 구현
//    private List<TradeReviewDto> tradeReviewDtos;
}
