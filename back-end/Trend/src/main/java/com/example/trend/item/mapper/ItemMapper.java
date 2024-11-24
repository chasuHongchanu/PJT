package com.example.trend.item.mapper;

import com.example.trend.item.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Insert("""
            INSERT INTO item (user_id,
                              item_name,
                              main_category,
                              sub_category,
                              sub_subcategory,
                              item_price,
                              address,
                              item_latitude,
                              item_longitude,
                              item_content,
                              available_rental_start_date,
                              available_rental_end_date,
                              item_status)
                      VALUES (#{userId},
                              #{itemName},
                              #{itemMainCategory},
                              #{itemSubCategory},
                              #{itemSubsubCategory},
                              #{itemPrice},
                              #{address},
                              #{latitude},
                              #{longitude},
                              #{itemContent},
                              #{availableRentalStartDate},
                              #{availableRentalEndDate},
                              "대여 가능")
            """)
    @Options(useGeneratedKeys = true, keyProperty = "itemId")
    int insertItem(ItemRequestDto itemRegistDto);

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
            SELECT user_profile_img,
                   user_nickname,
                   u.user_id,
                   user_rating,
                   i.item_id,
                   item_name,
                   item_price,
                   main_category,
                   sub_category,
                   sub_subcategory,
                   address,
                   item_content,
                   available_rental_start_date,
                   available_rental_end_date,
                   view_count,
                   (SELECT COUNT(*) FROM wishlist WHERE item_id = #{itemId}) AS likesCount

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
            SELECT i.item_id, i.item_name, u.user_activity_score, i.item_latitude AS latitude, i.item_longitude AS longitude, i.thumbnail
            FROM item i
            JOIN user u
            ON i.user_id = u.user_id
            WHERE i.sub_subcategory = (SELECT sub_subcategory
                                       FROM item
                                       WHERE item_id = #{itemId})
              AND i.item_id != #{itemId}
              AND i.item_deleted_at IS NULL
            ORDER BY u.user_activity_score DESC
            LIMIT 3;
            """)
    @Results({
            @Result(property = "lessorActivityScore", column = "user_activity_score"),
            @Result(property = "itemImage", column = "thumbnail")
    })
    List<ItemSimpleInfo> selectSimilarItems(int itemId);

    @Select("""
            SELECT i.item_id, i.item_name, u.user_activity_score, i.item_latitude AS latitude, i.item_longitude AS longitude, i.thumbnail
            FROM item i
            JOIN user u
            ON i.user_id = u.user_id
            WHERE i.item_id != #{itemId}
                  AND i.item_deleted_at IS NULL
                  AND ST_Distance_Sphere(
                        POINT((SELECT item_longitude FROM item WHERE item_id = #{itemId}),
                              (SELECT item_latitude FROM item WHERE item_id = #{itemId})),
                        POINT(i.item_longitude, i.item_latitude)
                      ) <= 300000
            ORDER BY u.user_activity_score DESC
            LIMIT 3;
            """)
    @Results({
            @Result(property = "lessorActivityScore", column = "user_activity_score"),
            @Result(property = "itemImage", column = "thumbnail")
    })
    List<ItemSimpleInfo> selectPeripheralItems(int itemId);

    @Update("""
            UPDATE item
            SET view_count = view_count + 1
            WHERE item_id = #{itemId}
            """)
    void updateViewCount(int itemId);

    @Select("""
            <script>
                SELECT item.item_id,
                       item.thumbnail AS itemImage,
                       item_name,
                       item_price,
                       address,
                       item_latitude AS latitude,
                       item_longitude AS longitude
                FROM item
                WHERE 1=1
                AND item.item_deleted_at IS NULL
                <if test="itemSearchCriteria.latitude != null and itemSearchCriteria.longitude != null">
                    AND ST_Distance_Sphere(
                        POINT(#{itemSearchCriteria.longitude}, #{itemSearchCriteria.latitude}),
                        POINT(item.item_longitude, item.item_latitude)
                    ) &lt;= 300000
                </if>
                <if test="itemSearchCriteria.minPrice != null">
                    AND item.item_price &gt;= #{itemSearchCriteria.minPrice}
                </if>
                <if test="itemSearchCriteria.maxPrice != null">
                    AND item.item_price &lt;= #{itemSearchCriteria.maxPrice}
                </if>
                <if test="itemSearchCriteria.mainCategory != null">
                    AND item.main_category = #{itemSearchCriteria.mainCategory}
                </if>
                <if test="itemSearchCriteria.subCategory != null">
                    AND item.sub_category = #{itemSearchCriteria.subCategory}
                </if>
                <if test="itemSearchCriteria.subSubCategory != null">
                    AND item.sub_subcategory = #{itemSearchCriteria.subSubCategory}
                </if>
                <if test="itemSearchCriteria.country != null">
                    AND address LIKE CONCAT('%', #{itemSearchCriteria.country}, '%')
                </if>
                <if test="itemSearchCriteria.province != null">
                    AND address LIKE CONCAT('%', #{itemSearchCriteria.province}, '%')
                </if>
                <if test="itemSearchCriteria.district != null">
                    AND address LIKE CONCAT('%', #{itemSearchCriteria.district}, '%')
                </if>
                <if test="itemSearchCriteria.town != null">
                    AND address LIKE CONCAT('%', #{itemSearchCriteria.town}, '%')
                </if>
                <if test="itemSearchCriteria.keyword != null and itemSearchCriteria.keyword != ''">
                    AND (
                        item.item_name LIKE CONCAT('%', #{itemSearchCriteria.keyword}, '%')
                        OR item.item_content LIKE CONCAT('%', #{itemSearchCriteria.keyword}, '%')
                    )
                </if>
                LIMIT #{size}
                OFFSET #{offset}
            </script>
            """)
    List<ItemRetrieveResponseDto> searchItems(@Param("itemSearchCriteria") ItemSearchCriteria itemSearchCriteria,
                                              @Param("offset") int offset,
                                              @Param("size") int size);

    @Select("""
            <script>
                SELECT COUNT(*)
                FROM item
                WHERE 1=1
                AND item.item_deleted_at IS NULL
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
                    AND address LIKE CONCAT('%', #{country}, '%')
                </if>
                <if test="province != null">
                    AND address LIKE CONCAT('%', #{province}, '%')
                </if>
                <if test="district != null">
                    AND address LIKE CONCAT('%', #{district}, '%')
                </if>
                <if test="town != null">
                    AND address LIKE CONCAT('%', #{town}, '%')
                </if>
                <if test="keyword != null and keyword != ''">
                    AND (
                        item.item_name LIKE CONCAT('%', #{keyword}, '%')
                        OR item.item_content LIKE CONCAT('%', #{keyword}, '%')
                    )
                </if>
            </script>
            """)
    int countItems(ItemSearchCriteria itemSearchCriteria);

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
            LIMIT #{size}
            OFFSET #{offset}
            """)
    List<TradeReviewDto> selectTradeReviewsByLessorId(String lessorId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM user u
            JOIN trade_review r
            ON u.user_id = r.lessee_id
            WHERE r.lessor_id = #{lessorId}
            """)
    int countLessorReviews(String lessorId);

    @Select("""
            SELECT article_id, article_title
            FROM article
            WHERE writer_id = #{lessorId}
            LIMIT #{size}
            OFFSET #{offset}
            """)
    List<ArticleSimpleInfo> selectArticlesByLessorId(String lessorId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM article
            WHERE writer_id = #{lessorId}
            """)
    int countLessorArticles(String lessorId);

    @Select("""
            SELECT article_image
            FROM article_image
            WHERE article_id = #{articleId}
            """)
    List<String> selectArticleImagesByArticleId(int articleId);

    @Select("""
            SELECT i.item_id, i.thumbnail AS itemImage, i.item_name, item_price, address
            FROM item i
            WHERE user_id = #{lessorId}
            AND i.item_deleted_at IS NULL
            LIMIT #{size}
            OFFSET #{offset}
            """)
    List<ItemRetrieveResponseDto> selectLessorItemsByLessorId(String lessorId, int offset, int size);

    @Select("""
            SELECT COUNT(*)
            FROM item i
            WHERE user_id = #{lessorId}
            AND i.item_deleted_at IS NULL
            """)
    int countLessorItems(String lessorId);

    @Delete("""
            DELETE
            FROM item_image
            WHERE item_id = #{itemId}
            """)
    void deleteItemImage(int itemId);

    @Update("""
            UPDATE item
            SET item_name = #{itemName},
                main_category = #{itemMainCategory},
                sub_category = #{itemSubCategory},
                sub_subcategory = #{itemSubsubCategory},
                item_price = #{itemPrice},
                address = #{address},
                item_latitude = #{latitude},
                item_longitude = #{longitude},
                item_content = #{itemContent},
                available_rental_start_date = #{availableRentalStartDate},
                available_rental_end_date = #{availableRentalEndDate},
                item_status = #{itemStatus}
            WHERE item_id = #{itemId}
            """)
    int updateItem(ItemRequestDto itemUpdateDto);

    @Update("""
            UPDATE item
            SET item_deleted_at = CURRENT_TIMESTAMP()
            WHERE item_id = #{itemId}
            """)
    int deleteByItemId(int itemId);

    @Update("""
            UPDATE item
            SET thumbnail = #{thumbnail}
            WHERE item_id = #{itemId}
            """)
    void insertThumbnail(String thumbnail, int itemId);

    @Insert("""
            INSERT wishlist (user_id, item_id)
            VALUES (#{userId}, #{itemId})
            """)
    int insertWishList(String userId, String itemId);

    @Delete("""
            DELETE FROM wishlist
            WHERE user_id = #{userId}
            AND item_id = #{itemId}
            """)
    int deleteWishList(String userId, String itemId);

    @Select("""
            SELECT user_id,
                   item_name,
                   main_category AS itemMainCategory,
                   sub_category AS itemSubCategory,
                   sub_subcategory AS itemSubsubCategory,
                   item_price,
                   address,
                   item_latitude AS latitude,
                   item_longitude AS longitude,
                   item_content,
                   available_rental_start_date,
                   available_rental_end_date
            FROM item
            WHERE item_id = #{itemId}
            """)
    ItemRequestDto selectUpdateViewInfo(int itemId);
}