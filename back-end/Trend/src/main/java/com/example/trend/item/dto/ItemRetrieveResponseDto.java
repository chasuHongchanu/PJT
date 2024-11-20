package com.example.trend.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemRetrieveResponseDto {
    private int itemId;
    private String itemImage;
    private String itemName;
    private int itemPrice;
    private String address;
}
