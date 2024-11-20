package com.example.trend.trade.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeNotWrittenReview {
    private int tradeId;
    private int itemId;
    private String thumbnail;
    private String itemName;
    private String rentalEndDate;
}
