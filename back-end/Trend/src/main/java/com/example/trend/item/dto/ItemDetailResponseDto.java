package com.example.trend.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDetailResponseDto {
    // from user table
    private String lessorProfileImage;
    private String lessorNickname;
    private String lessorId;
    private int lessorReputation;

    // from item table
    private int itemId;
    private String itemName;
    private int itemPrice;
    private String mainCategory;
    private String subCategory;
    private String subSubCategory;
    private String address;
    private String itemContent;
    private String availableRentalStartDate;
    private String availableRentalEndDate;
    private int viewCount;

    // from item_image table
    private List<String> itemImageNames;

    // from wishlist table
    // lessorId가 아니라, 로그인한 user의 id 값으로 조회해야 함을 주의
    private boolean isWishList;
    private int likesCount;
}
