package com.example.trend.trade.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeReviewRequestDto {
    private int tradeId;
    private String userId;
    private int rating;
    private String reviewContent;
}
