package com.example.trend.trade.mapper;

import com.example.trend.trade.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TradeMapper {
    @Select("""
            SELECT item_id,
                   available_rental_start_date,
                   available_rental_end_date,
                   item_name,
                   item_price,
                   address,
                   thumbnail,
            (SELECT user_nickname FROM user WHERE user_id = #{lessorId}) AS lessor_nickname,
            (SELECT user_nickname FROM user WHERE user_id = #{lesseeId}) AS lessee_nickname
            FROM item
            WHERE item_id = #{itemId}
            """)
    TradeReservationResponseDto selectReservationInfo(TradeReservationRequestDto tradeReservationRequestDto);

    @Insert("""
            INSERT
            INTO item_trade (item_id, lessor_id, lessee_id, trade_price, trade_deposit, payment_account_number, rental_start_date, rental_end_date)
            VALUES (#{itemId}, #{lessorId}, #{lesseeId}, #{tradePrice}, #{tradeDeposit}, #{paymentAccountNumber}, #{tradeRentalStartDate}, #{tradeRentalEndDate})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "tradeId")
    int insertReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto);

    @Update("""
            UPDATE item_trade
            SET trade_price = #{tradePrice},
                trade_deposit = #{tradeDeposit},
                rental_start_date = #{tradeRentalStartDate},
                rental_end_date = #{tradeRentalEndDate}
            WHERE trade_id = #{tradeId}
            """)
    int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto);

    @Select("""
            SELECT
                i.item_id,
                i.item_name,
                i.item_price,
                i.address,
                i.thumbnail,

                t.rental_start_date AS trade_rental_start_date,
                t.rental_end_date AS trade_rental_end_date,
                t.trade_price,
                t.trade_deposit,
                t.payment_account_number,
                t.trade_state,
                t.payment_status,

                u1.user_id AS lessor_id,
                u2.user_id AS lessee_id,
                u1.user_nickname AS lessor_nickname,
                u2.user_nickname AS lessee_nickname
      
            FROM item_trade t
            JOIN item i ON t.item_id = i.item_id
            JOIN user u1 ON t.lessor_id = u1.user_id
            JOIN user u2 ON t.lessee_id = u2.user_id
            WHERE t.trade_id = #{tradeId}
            """)
    TradeDetailResponseDto selectTradeDetail(int tradeId);

    @Select("""
            SELECT condition_img
            FROM item_condition_image
            WHERE trade_id = #{tradeId}
            """)
    List<String> selectItemConditionImages(int tradeId);

    @Insert("""
            <script>
                INSERT INTO item_condition_image (trade_id, condition_img) VALUES
                <foreach collection='itemConditionImageNames' item='itemConditionImageName' separator=','>
                    (#{tradeId}, #{itemConditionImageName})
                </foreach>
            </script>
            """)
    int insertItemConditionImages(@Param("tradeId") int tradeId, @Param("itemConditionImageNames") List<String> itemConditionImageNames);

    @Update("""
            UPDATE item_trade
            SET trade_state = "대여 중"
            WHERE trade_id = #{tradeId}
            """)
    void updateTradeState(int tradeId);

    @Update("""
            UPDATE item_trade
            SET payment_status = "입금 완료"
            WHERE trade_id = #{tradeId}
            """)
    int updatePaymentStatus(int tradeId);

    @Delete("""
            DELETE
            FROM item_trade
            WHERE trade_id = #{tradeId}
            """)
    int deleteReservation(int tradeId);

    @Select("""
            SELECT t.item_id,
                   i.item_name,
                   i.item_price,
                   i.address,
                   i.thumbnail
            FROM item_trade t
            JOIN item i
            ON t.item_id = i.item_id
            WHERE t.trade_id = #{tradeId}
            """)
    TradeReviewResponseDto selectTradeInfoForReview(int tradeId);

    @Insert("""
            INSERT
            INTO trade_review(trade_id, author_user_id, lessor_id, lessee_id, trade_review_content, trade_review_rating)
            SELECT trade_id, #{userId}, lessor_id, lessee_id, #{reviewContent}, #{rating}
            FROM item_trade
            WHERE trade_id = #{tradeId}
            """)
    int insertReview(TradeReviewRequestDto tradeReviewRequestDto);

    @Update("""
            UPDATE user
            SET user_rating = user_rating + (0.05 * #{rating})
            WHERE user_id = (
                SELECT lessor_id
                FROM item_trade
                WHERE trade_id = #{tradeId}
            )
            """)
    void updateLessorRating(int tradeId, int rating);

    @Update("""
            UPDATE item_trade
            SET trade_state = "반납 완료"
            WHERE trade_id = #{tradeId}
            """)
    int updateTradeStateToReturn(int tradeId);

    @Update("""
            UPDATE item
            SET item_status = "예약 중"
            WHERE item_id = #{itemId}
            """)
    void updateItemStatusToReservation(int itemId);

    @Update("""
            UPDATE item
            SET item_status = "대여 중"
            WHERE item_id = (
                SELECT item_id
                FROM item_trade
                WHERE trade_id = #{tradeId}
            )
            """)
    void updateItemStatusToLend(int tradeId);

    @Update("""
            UPDATE item
            SET item_status = "대여 가능"
            WHERE item_id = (
                SELECT item_id
                FROM item_trade
                WHERE trade_id = #{tradeId}
            )
            """)
    void updateItemStatusToLendPossible(int tradeId);

    @Select("""
            SELECT item_id,
                   item_name,
                   item_price,
                   available_rental_start_date,
                   available_rental_end_date,
                   thumbnail,
                   address,
                   item_status
            FROM item
            WHERE user_id = #{userId}
            AND (item_status = "대여 가능" OR item_status = "예약 중")
            """)
    List<TradeMyItemsResponseDto> selectRegistItems(String userId);

    @Select("""
            SELECT i.item_id,
                   t.trade_id,
                   item_name,
                   item_price,
                   t.rental_start_date AS availableRentalStartDate,
                   t.rental_end_date AS availableRentalEndDate,
                   thumbnail,
                   address,
                   item_status,
                   trade_state,
                   payment_status
            FROM item i
            JOIN item_trade t
            ON i.item_id = t.item_id
            WHERE t.lessor_id = #{userId}
            """)
    List<TradeMyItemsResponseDto> selectLendItems(String userId);

    @Select("""
            SELECT i.item_id,
                   t.trade_id,
                   item_name,
                   item_price,
                   t.rental_start_date AS availableRentalStartDate,
                   t.rental_end_date AS availableRentalEndDate,
                   thumbnail,
                   address,
                   item_status,
                   trade_state,
                   payment_status
            FROM item i
            JOIN item_trade t
            ON i.item_id = t.item_id
            WHERE t.lessee_id = #{userId}
            """)
    List<TradeMyItemsResponseDto> selectLeaseItems(String userId);

    @Select("""
            SELECT i.item_id,
                   item_name,
                   item_price,
                   available_rental_start_date,
                   available_rental_end_date,
                   thumbnail,
                   address,
                   item_statusㅕ
            FROM item i
            JOIN item_trade t
            ON i.item_id = t.item_id
            WHERE t.lessor_id = #{userId}
            AND t.trade_state = "반납 완료"
            """)
    List<TradeMyItemsResponseDto> selectReturnLendItems(String userId);

    @Select("""
            SELECT i.item_id,
                   item_name,
                   item_price,
                   available_rental_start_date,
                   available_rental_end_date,
                   thumbnail,
                   address,
                   item_status
            FROM item i
            JOIN item_trade t
            ON i.item_id = t.item_id
            WHERE t.lessee_id = #{userId}
            AND t.trade_state = "반납 완료"
            """)
    List<TradeMyItemsResponseDto> selectReturnLeaseItems(String userId);

    @Select("""
            SELECT i.item_id,
                   item_name,
                   item_price,
                   available_rental_start_date,
                   available_rental_end_date,
                   thumbnail,
                   address,
                   item_status
            FROM item i
            JOIN wishlist w
            ON i.item_id = w.item_id
            WHERE w.user_id = #{userId}
            """)
    List<TradeMyItemsResponseDto> selectwishListItems(String userId);

    @Select("""
            SELECT u.user_id,
                   u.user_profile_img,
                   r.trade_review_id,
                   r.review_created_at,
                   r.trade_review_content
            FROM user u
            JOIN trade_review r
            ON u.user_id = r.lessee_id
            WHERE u.user_id = #{userId}
            AND r.author_user_id != #{userId}
            """)
    List<TradeReviewsForMe> selectLendReviewsForMe(String userId);

    @Select("""
            SELECT u.user_id,
                   u.user_profile_img,
                   r.trade_review_id,
                   r.review_created_at,
                   r.trade_review_content
            FROM user u
            JOIN trade_review r
            ON u.user_id = r.lessor_id
            WHERE u.user_id = #{userId}
            AND r.author_user_id != #{userId}
            """)
    List<TradeReviewsForMe> selectLeaseReviewsForMe(String userId);

    @Select("""
            SELECT u.user_id,
                   u.user_profile_img,
                   r.trade_review_id,
                   r.review_created_at,
                   r.trade_review_content
            FROM user u
            JOIN trade_review r
            ON u.user_id = r.lessee_id
            WHERE r.author_user_id = #{userId}
            """)
    List<TradeReviewsForMe> selectMyReviews(String userId);

    @Select("""
            SELECT i.item_id,
                   i.thumbnail,
                   i.item_name,
                   t.rental_end_date,
                   t.trade_id
            FROM item i
            JOIN item_trade t
            ON t.item_id = i.item_id
            WHERE (t.lessee_id = #{userId} OR t.lessor_id = #{userId})
            AND t.trade_id NOT IN (
                SELECT trade_id
                FROM trade_review
                WHERE author_user_id = #{userId}
            )
            """)
    List<TradeNotWrittenReview> selectNotWrittenReviews(String userId);
}
