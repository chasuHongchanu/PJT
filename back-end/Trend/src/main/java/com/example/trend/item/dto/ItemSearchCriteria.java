package com.example.trend.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemSearchCriteria {
    private Double latitude;
    private Double longitude;
    private Integer maxPrice;
    private Integer minPrice;
    private String mainCategory;
    private String subCategory;
    private String subSubCategory;
    private String keyword;
}
