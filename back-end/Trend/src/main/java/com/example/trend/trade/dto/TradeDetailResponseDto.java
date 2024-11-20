package com.example.trend.trade.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TradeDetailResponseDto {
    private int itemId;
    private String tradeRentalStartDate;
    private String tradeRentalEndDate;
    private String itemName;
    private int itemPrice;
    private String address;
    private String thumbnail;
    private String lessorId;
    private String lesseeId;
    private String lessorNickname;
    private String lesseeNickname;
    private String paymentAccountNumber;
    private int tradePrice;
    private int tradeDeposit;
    private List<String> itemConditionImages;
    private String tradeState;
    private String paymentStatus;
}
