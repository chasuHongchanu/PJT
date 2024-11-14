package com.example.trend.item.repository;

import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository{
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRepositoryImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public int regist(ItemRegistRequestDto itemRegistDto) {
        return itemMapper.insertItem(itemRegistDto);
    }
}
