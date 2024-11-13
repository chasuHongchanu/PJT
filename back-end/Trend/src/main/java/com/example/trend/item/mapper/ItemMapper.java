package com.example.trend.item.mapper;

import com.example.trend.item.dto.ItemRegistRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ItemMapper {
    @Insert("INSERT INTO item (item_name, user_id, item_category, item_price, item_address, item_content, available_rental_start_date, available_rental_end_date) " +
            "VALUES (#{itemName}, #{userId}, #{itemCategory}, #{itemPrice}, #{itemAddress}, #{itemContent}, #{availableRentalStartDate}, #{availableRentalEndDate})")
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int insertItem(ItemRegistRequestDto itemRegistDto);
}
