package com.example.trend.item.service;

import com.example.trend.item.dto.*;

import java.util.List;

public interface ItemService {
    List<ItemRetrieveResponseDto> searchItems(ItemSearchCriteria itemSearchCriteria);

    int regist(ItemRegistRequestDto itemRegistDto);

    ItemDetailResponseDto detail(int itemId, String userId);

    ItemLessorInfoDto getLessorInfo(String lessorId);

    List<ItemRetrieveResponseDto> getLessorItems(String lessorId);
}
