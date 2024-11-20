package com.example.trend.trade.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeReviewsForMe {
    private int tradeReviewId;
    private String userProfileImg;
    private String userId;
    private String reviewCreatedAt;
    private String tradeReviewContent;
}
