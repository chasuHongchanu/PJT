package com.example.trend.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemSimpleInfo {
    private int itemId;
    private String itemName;
    private String itemImage;
    private int lessorActivityScore;
    private double itemLatitude;
    private double itemLongitude;
}
