package com.example.trend.item.mapper;

import com.example.trend.item.dto.ItemRegistRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper {
    @Insert("")
    int insertItem(ItemRegistRequestDto itemRegistDto);
}
