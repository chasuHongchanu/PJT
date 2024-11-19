package com.example.trend.trade.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeMyItemsResponseDto {
    private int itemId;
    private String itemName;
    private String itemPrice;
    private String availableRentalStartDate;
    private String availableRentalEndDate;
    private String thumbnail;
    private String country;
    private String province;
    private String district;
    private String town;
    private String itemStatus;
}
