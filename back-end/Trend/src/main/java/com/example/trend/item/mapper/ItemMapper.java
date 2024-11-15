package com.example.trend.item.mapper;

import com.example.trend.item.dto.ItemRegistRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ItemMapper {
    @Insert("""
            INSERT INTO item (user_id, item_name, main_category, sub_category, sub_subcategory, item_price, country, province, district, town, item_content, available_rental_start_date, available_rental_end_date, item_status)
                      VALUES (#{userId}, #{itemName}, #{itemMainCategory}, #{itemSubCategory}, #{itemSubsubCategory},
                                      #{itemPrice}, #{itemCountry}, #{itemProvince}, #{itemDistrict}, #{itemTown}, #{itemContent},
                                      #{availableRentalStartDate}, #{availableRentalEndDate}, #{itemStatus})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int insertItem(ItemRegistRequestDto itemRegistDto);

    @Insert("""
            INSERT INTO item_image (item_id, item_img)
            VALUES (#{itemId}, #{itemImageName})
            """)
    void insertItemImageName(int itemId, String itemImageName);

    @Update("""
            UPDATE user
            SET user_activity_score = user_activity_score + 1
            WHERE user_id = #{userId}
            """)
    void updateUserActivityScore(String userId);
}
