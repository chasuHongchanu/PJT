package com.example.trend.item.service;

import com.example.trend.item.dto.ItemDetailResponseDto;
import com.example.trend.item.dto.ItemRegistRequestDto;

public interface ItemService {
    int regist(ItemRegistRequestDto itemRegistDto);

    ItemDetailResponseDto detail(int itemId, String userId);
}
