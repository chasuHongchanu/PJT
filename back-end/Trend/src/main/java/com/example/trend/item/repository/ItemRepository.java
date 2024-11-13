package com.example.trend.item.repository;

import com.example.trend.item.dto.ItemRegistRequestDto;

public interface ItemRepository {
    int regist(ItemRegistRequestDto itemRegistDto);
}
