package com.example.trend.item.service;

import com.example.trend.item.dto.ItemDetailResponseDto;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.dto.ItemRetrieveResponseDto;
import com.example.trend.item.dto.ItemSearchCriteria;

import java.util.List;

public interface ItemService {
    List<ItemRetrieveResponseDto> searchItems(ItemSearchCriteria itemSearchCriteria);

    int regist(ItemRegistRequestDto itemRegistDto);

    ItemDetailResponseDto detail(int itemId, String userId);
}
