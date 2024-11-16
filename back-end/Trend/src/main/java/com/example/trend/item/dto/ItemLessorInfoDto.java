package com.example.trend.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemLessorInfoDto {
    // from user table
    private String lessorId;
    private String lessorProfileImg;
    private String lessorNickname;
    private double lessorReputation;

    // from trade_review table
    private int lessorReviewCount;
    private List<TradeReviewDto> tradeReviews;

    // from item_trade table
    private List<ItemRetrieveResponseDto> lessorLendItems;
    private int lessorTradeCount;

    // from article table
    private List<ArticleSimpleInfo> lessorArticles;
}
