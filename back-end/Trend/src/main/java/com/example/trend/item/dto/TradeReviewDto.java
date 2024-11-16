package com.example.trend.item.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeReviewDto {
    private String lesseeProfileImage;
    private String lesseeNickname;
    private String reviewCreatedAt;
    private String reviewContent;
}
