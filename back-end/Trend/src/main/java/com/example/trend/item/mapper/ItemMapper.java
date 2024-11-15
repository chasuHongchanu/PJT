package com.example.trend.item.mapper;

import com.example.trend.item.dto.ItemRegistRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ItemMapper {
    @Insert("""
            INSERT INTO item (user_id, item_name, main_category, sub_category, sub_subcategory, item_price, province, district, town, item_content, available_rental_start_date, available_rental_end_date, item_status)
                      VALUES (#{userId}, #{itemName}, #{itemMainCategory}, #{itemSubCategory}, #{itemSubsubCategory},
                                      #{itemPrice}, #{itemProvince}, #{itemDistrict}, #{itemTown}, #{itemContent},
                                      #{availableRentalStartDate}, #{availableRentalEndDate}, #{itemStatus})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int insertItem(ItemRegistRequestDto itemRegistDto);

    @Insert("")
    int insertItemImageName(int itemId, String itemImageName);
}
