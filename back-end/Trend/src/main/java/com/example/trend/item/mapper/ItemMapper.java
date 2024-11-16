package com.example.trend.item.mapper;

import com.example.trend.item.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("""
            SELECT user_profile_img, user_nickname, u.user_id, user_rating, i.item_id, item_name, item_price, main_category, sub_category, sub_subcategory, i.country, province, district, town, item_content, available_rental_start_date, available_rental_end_date, view_count
            FROM item i
            JOIN user u
            ON i.user_id = u.user_id
            WHERE i.item_id = #{itemId}
            """)
    @Results({
            @Result(property = "lessorProfileImage", column = "user_profile_img"),
            @Result(property = "lessorNickname", column = "user_nickname"),
            @Result(property = "lessorId", column = "user_id"),
            @Result(property = "lessorReputation", column = "user_rating"),
            @Result(property = "itemCountry", column = "country"),
            @Result(property = "itemProvince", column = "province"),
            @Result(property = "itemDistrict", column = "district"),
            @Result(property = "itemTown", column = "town"),
    })
    ItemDetailResponseDto selectDetailByItemId(int itemId);

    @Select("""
            SELECT item_img
            FROM item_image
            WHERE item_id = #{itemId}
            """)
    List<String> selectItemNameImagesByItemId(int itemId, String userId);

    @Select("""
            SELECT COUNT(*)
            FROM wishlist
            WHERE user_id = #{userId} AND item_id = #{itemId};
            """)
    int isWishListItem(int itemId, String userId);

    @Select("""
            SELECT i.item_id, i.item_name, u.user_activity_score, i.item_latitude, i.item_longitude, im.item_img
            FROM item i
            JOIN user u
            ON i.user_id = u.user_id
            LEFT JOIN item_image im
            ON i.item_id = im.item_id
               AND im.item_img_id = (
                   SELECT MIN(item_img_id)
                   FROM item_image
                   WHERE item_id = i.item_id
               )
            WHERE sub_subcategory = #{subSubCategory}
              AND i.item_id != #{itemId}
            ORDER BY u.user_activity_score DESC
            LIMIT 5;
            """)
    @Results({
            @Result(property = "lessorActivityScore", column = "user_activity_score"),
            @Result(property = "itemImage", column = "item_img")
    })
    List<ItemSimpleInfo> selectSimilarItems(int itemId, String subSubCategory);

    @Select("""
            SELECT i.item_id, i.item_name, u.user_activity_score, i.item_latitude, i.item_longitude, im.item_img
            FROM item i
            JOIN user u
            ON i.user_id = u.user_id
            LEFT JOIN item_image im
            ON i.item_id = im.item_id
               AND im.item_img_id = (
                   SELECT MIN(item_img_id)
                   FROM item_image
                   WHERE item_id = i.item_id
               )
            WHERE district = #{district}
              AND town = #{town}
              AND i.item_id != #{itemId}
            ORDER BY u.user_activity_score DESC
            LIMIT 5;
            """)
    @Results({
            @Result(property = "lessorActivityScore", column = "user_activity_score"),
            @Result(property = "itemImage", column = "item_img")
    })
    List<ItemSimpleInfo> selectPeripheralItems(int itemId, String district, String town);

    @Update("""
            UPDATE item
            SET view_count = view_count + 1
            WHERE item_id = #{itemId}
            """)
    void updateViewCount(int itemId);

    @Select("""
            <script>
                SELECT item.item_id, item_img AS itemImageName, item_name, item_price, country, province, district, town
                FROM item
                LEFT JOIN item_image im
                ON item.item_id = im.item_id
                   AND im.item_img_id = (
                       SELECT MIN(item_img_id)
                       FROM item_image
                       WHERE item_id = item.item_id
                   )
                WHERE 1=1
                <if test="latitude != null and longitude != null">
                    AND ST_Distance_Sphere(
                        POINT(#{longitude}, #{latitude}),
                        POINT(item.item_longitude, item.item_latitude)
                    ) &lt;= 300000
                </if>
                <if test="minPrice != null">
                    AND item.item_price &gt;= #{minPrice}
                </if>
                <if test="maxPrice != null">
                    AND item.item_price &lt;= #{maxPrice}
                </if>
                <if test="mainCategory != null">
                    AND item.main_category = #{mainCategory}
                </if>
                <if test="subCategory != null">
                    AND item.sub_category = #{subCategory}
                </if>
                <if test="subSubCategory != null">
                    AND item.sub_subcategory = #{subSubCategory}
                </if>
                <if test="country != null">
                    AND item.country = #{country}
                </if>
                <if test="province != null">
                    AND item.province = #{province}
                </if>
                <if test="district != null">
                    AND item.district = #{district}
                </if>
                <if test="town != null">
                    AND item.town = #{town}
                </if>
                <if test="keyword != null and keyword != ''">
                    AND (
                        item.item_name LIKE CONCAT('%', #{keyword}, '%')
                        OR item.item_content LIKE CONCAT('%', #{keyword}, '%')
                    )
                </if>
            </script>
            """)
    List<ItemRetrieveResponseDto> searchItems(ItemSearchCriteria itemSearchCriteria);


    @Select("""
            SELECT
                u.user_id AS lessorId,
                u.user_nickname AS lessorNickname,
                u.user_rating AS lessorReputation,
                u.user_profile_img AS lessorProfileImg,
                COALESCE(t.trade_count, 0) AS lessorTradeCount,
                COALESCE(r.review_count, 0) AS lessorReviewCount
            FROM user u
            LEFT JOIN (
                SELECT lessor_id, COUNT(*) AS trade_count
                FROM item_trade
                GROUP BY lessor_id
            ) t ON u.user_id = t.lessor_id
            LEFT JOIN (
                SELECT lessor_id, COUNT(*) AS review_count
                FROM trade_review
                GROUP BY lessor_id
            ) r ON u.user_id = r.lessor_id
            
            WHERE u.user_id = #{userId};
            """)
    ItemLessorInfoDto selectItemLessorInfoByLessorId(String lessorId);

    @Select("""
            SELECT user_profile_img AS lesseeProfileImg,
                   user_nickname AS lesseeNickname,
                   review_created_at,
                   trade_review_content AS reviewContent
            FROM user u
            JOIN trade_review r
            ON u.user_id = r.lessee_id
            WHERE r.lessor_id = #{lessorId}
            """)
    List<TradeReviewDto> selectTradeReviewsByLessorId(String lessorId);

    @Select("""
            SELECT i.item_id, im.item_img AS item_image, i.item_name, item_price
            FROM item i
            LEFT JOIN item_image im
            ON i.item_id = im.item_id
               AND im.item_img_id = (
                   SELECT MIN(item_img_id)
                   FROM item_image
                   WHERE item_id = i.item_id
               )
            WHERE user_id = #{lessorId}
            """)
    List<ItemRetrieveResponseDto> selectLendItemsByLessorId(String lessorId);

    @Select("""
            SELECT article_id, article_title
            FROM article
            WHERE writer_id = #{lessorId}
            """)
    List<ArticleSimpleInfo> selectArticlesByLessorId(String lessorId);

    @Select("""
            SELECT article_image
            FROM article_image
            WHERE article_id = #{articleId}
            """)
    List<String> selectArticleImagesByArticleId(int articleId);

    @Select("""
            SELECT i.item_id, im.item_img AS item_image, i.item_name, item_price, country, province, district, town
            FROM item i
            LEFT JOIN item_image im
            ON i.item_id = im.item_id
               AND im.item_img_id = (
                   SELECT MIN(item_img_id)
                   FROM item_image
                   WHERE item_id = i.item_id
               )
            WHERE user_id = #{lessorId}
            """)
    List<ItemRetrieveResponseDto> selectLessorItemsByLessorId(String lessorId);
}