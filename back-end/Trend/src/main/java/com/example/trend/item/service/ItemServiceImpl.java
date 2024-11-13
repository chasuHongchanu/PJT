package com.example.trend.item.service;

import com.example.trend.exception.CustomException;
import com.example.trend.exception.ErrorCode;
import com.example.trend.item.dto.ItemRegistRequestDto;
import com.example.trend.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public int regist(ItemRegistRequestDto itemRegistDto) throws  CustomException{
        if (itemRegistDto.getItemName() == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return itemRepository.regist(itemRegistDto);
    }
}
